package com.zd.sell.service.impl;

import com.zd.sell.converter.OrderMaster2OrderDTOConverter;
import com.zd.sell.dao.OrderDetailRepository;
import com.zd.sell.dao.OrderMasterRepository;
import com.zd.sell.dto.CartDTO;
import com.zd.sell.dto.OrderDTO;
import com.zd.sell.enums.OrderStatusEnum;
import com.zd.sell.enums.PayStatusEnum;
import com.zd.sell.enums.ResultEnum;
import com.zd.sell.exception.SellException;
import com.zd.sell.pojo.OrderDetail;
import com.zd.sell.pojo.OrderMaster;
import com.zd.sell.pojo.ProductInfo;
import com.zd.sell.service.OrderService;
import com.zd.sell.service.ProductService;
import com.zd.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 对订单的操作
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    /**
     * OrderDTO对象其实是由OrderMaster和List<OrderDetail>组成的，这个方法就是分别把OrderMaster存到OrderMaster表中，把List<OrderDetail>
     * 拆分成OrderDetail然后插入到OrderDetail表中，说人话就是插入大订单、插入大订单对应的小订单
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        //大订单ID应该是在创建订单这个方法的时候就生成的
        String orderId = KeyUtil.genUniqueKey();
        //大订单的总金额
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        //要扣库存的购物车集合
        List<CartDTO> cartDTOList = new ArrayList<>();


        //1. 查询商品（数量、价格），因为商品的价格不能由controller传过来
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2. 计算总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //生成小订单ID
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            //给小订单设置大订单ID
            orderDetail.setOrderId(orderId);
            //用产品信息给小订单的其余属性赋值
            BeanUtils.copyProperties(productInfo, orderDetail);

            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
            //插入小订单
            orderDetailRepository.save(orderDetail);
        }

        //3. 写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        //用orderDTO给大订单剩余属性赋值
        BeanUtils.copyProperties(orderDTO, orderMaster);
        //给大订单赋值大订单id
        orderMaster.setOrderId(orderId);
        //给大订单赋值大订单产品总金额
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterRepository.save(orderMaster);

        //4. 扣库存
        productService.decreaseStock(cartDTOList);

        return orderDTO;


    }


    /**
     * 返回根据大订单ID查询的OrderDTO对象，其中包括了单个大订单，和大订单相关联的小订单。
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDTO findOne(String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        OrderMaster orderMaster = byId.get();
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //返回所有的指定大订单单号的小订单
        List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(byOrderId)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(byOrderId);
        return orderDTO;
    }


    /**
     * 返回买家的所有大订单，其中不包括小订单，你想嘛，看一个人下了几单，然后只需要显示大单就行了，如果看小单那就是看单个了
     *
     * @param buyerOpenid 买家的微信ID
     * @param pageable    分页对象
     * @return 返回的是OrderDTO的分页对象
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        //通过buyerOpenid返回orderMaster的分页对象
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        //把List<orderMaster>转换为List<OrderDTO>
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        //返回OrderDTO的分页对象
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    /**
     * 取消订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态，必须是新下单的才行，也就是订单状态为0的时候才能取消订单。
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }


        //修改订单状态，
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        //因为操作的bean是orderDTO，而且入库的时候操作的是orderMaster，所以这里把orderDTO转换成orderMaster
        BeanUtils.copyProperties(orderDTO, orderMaster);
        //更新orderMaster表，因为orderMaster存放的是订单状态，用户取消订单之后一定要改变订单状态。
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        //若更新orderMaster表失败(失败的时候返回的是orderMaster对象是Null),抛异常
        if (updateResult == null) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }


        //返回库存
        //orderDTO一定是有orderMaster对象和List<OrderDetail>对象的，前者主要记录谁的订单，后者主要记录买了什么
        //也就是说如果list为空，就表明这个订单什么都没买，那一定是不对的
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        //这里把orderDTO中的属性值造一个购物车(List<CartDTO>)出来，用来增加库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        //更新一个购物车的库存
        productService.increaseStock(cartDTOList);



        //如果已支付, 需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
//            payService.refund(orderDTO);
        }

        return orderDTO;
    }


    /**
     * 完结订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        //要完结订单的话，订单一定得是新订单
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        //再到orderMaster表中，把orderDTO对应的orderMaster的订单状态改成完结
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //推送微信模版消息
//        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }


    /**
     * 将新订单变为已支付订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态，此时的orderDTO的订单状态必须是新订单
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
