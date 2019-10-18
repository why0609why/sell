package com.zd.sell.service.impl;

import com.zd.sell.dao.ProductInfoRepository;
import com.zd.sell.dto.CartDTO;
import com.zd.sell.enums.ProductStatusEnum;
import com.zd.sell.enums.ResultEnum;
import com.zd.sell.exception.SellException;
import com.zd.sell.pojo.ProductInfo;
import com.zd.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 对产品的操作
 */
@Service
public class ProductServiceImpl implements ProductService {

    /**
     * 注入ProductInfo的dao层对象
     */
    @Autowired
    private ProductInfoRepository productInfoRepository;

    /**
     * 返回productId查询商品指定id的ProductInfo
     *
     * @param productId
     * @return
     */
    @Override
    public ProductInfo findOne(String productId) {
        Optional<ProductInfo> byId = productInfoRepository.findById(productId);
        return byId.get();
    }


    /**
     * 返回全部的productStatus状态为0（此处使用枚举）的ProductInfo
     *
     * @return
     */
    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }


    /**
     * 分页返回所有的商品
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }


    /**
     * 插入一个商品
     *
     * @param productInfo
     * @return
     */
    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }


    /**
     * 加库存
     *
     * @param cartDTOList 购物车的形式，购物车其实是一个集合，里面放的对象是每个商品的id和买了几件。
     */
    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        //遍历购物车里面的每一个商品
        for (CartDTO cartDTO : cartDTOList) {
            //根据商品id拿到productInfo表中指定的商品
            Optional<ProductInfo> opt = productInfoRepository.findById(cartDTO.getProductId());
            ProductInfo productInfo = opt.get();
            //如果没有找到商品自然要报错
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //找到的话就把库存加回去
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            //把库存更新到商品中
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    /**
     * 减库存
     *
     * @param cartDTOList 要减库存的所有商品
     */
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        //遍历每个商品
        for (CartDTO cartDTO : cartDTOList) {
            //根据购物车中的商品id获得商品对象
            Optional<ProductInfo> opt = productInfoRepository.findById(cartDTO.getProductId());
            ProductInfo productInfo = opt.get();
            //如果没有找到商品就返回
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }


            //减去的去存
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            //若库存不正确，就报错
            if (result < 0) {
                throw new SellException(ResultEnum.PROCUCT_STOCK_ERROR);
            }
            //设置新库存
            productInfo.setProductStock(result);
            //把设置完库存的商品放到数据库中
            productInfoRepository.save(productInfo);


        }


    }


}
