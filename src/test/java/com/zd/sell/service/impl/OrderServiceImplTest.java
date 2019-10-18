package com.zd.sell.service.impl;

import com.zd.sell.dto.OrderDTO;
import com.zd.sell.pojo.OrderDetail;
import com.zd.sell.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BuyerOpenid = "110110";
    private final String ORDER_ID = "1570959401540133726";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("小明");
        orderDTO.setBuyerAddress("幸福大街");
        orderDTO.setBuyerPhone("12312312");
        orderDTO.setBuyerOpenid(BuyerOpenid);
        List<OrderDetail> list = new ArrayList<>();

        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123457");
        o1.setProductQuantity(4);
        list.add(o1);

        orderDTO.setOrderDetailList(list);
        OrderDTO orderDTO1 = orderService.create(orderDTO);

    }

    @Test
    public void findOne() {

        OrderDTO one = orderService.findOne(ORDER_ID);
        System.out.println(one);

    }

    @Test
    public void findList() {
        PageRequest pageRequest = new PageRequest(0,2);
        Page<OrderDTO> list = orderService.findList(BuyerOpenid, pageRequest);



    }

    @Test
    public void cancel() {

        OrderDTO one = orderService.findOne("1570954348269256198");
        OrderDTO cancel = orderService.cancel(one);
        System.out.println(cancel);


    }

    @Test
    public void finish() {

        OrderDTO one = orderService.findOne("1570954348269256198");
        OrderDTO finish = orderService.finish(one);
        System.out.println(finish);

    }

    @Test
    public void paid() {
        OrderDTO one = orderService.findOne("1570954348269256198");
        OrderDTO paid = orderService.paid(one);
        System.out.println(paid);
    }
}