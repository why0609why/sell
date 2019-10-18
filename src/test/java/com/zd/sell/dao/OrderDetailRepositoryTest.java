package com.zd.sell.dao;

import com.zd.sell.pojo.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456789321");
        orderDetail.setOrderId("111111132");
        orderDetail.setProductIcon("http://11111.jpg");
        orderDetail.setProductId("312312312321");
        orderDetail.setProductName("皮蛋");
        orderDetail.setProductPrice(new BigDecimal(1.1113));
        orderDetail.setProductQuantity(2);

        OrderDetail save = orderDetailRepository.save(orderDetail);
        Assert.assertNotEquals(null, save);
    }

    @Test
    public void findByOrderId(){
        List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId("111111132");
        Assert.assertNotEquals(byOrderId.size(), 0);
    }

}