package com.zd.sell.dao;

import com.zd.sell.pojo.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123459");
        orderMaster.setBuyerName("小红");
        orderMaster.setBuyerPhone("12345678949");
        orderMaster.setBuyerAddress("吕梁野山坡");
        orderMaster.setBuyerOpenid("110111");
        orderMaster.setOrderAmount(new BigDecimal(110.11));

        OrderMaster save = orderMasterRepository.save(orderMaster);
        Assert.assertNotEquals(null,save);
    }

    @Test
    public void findByOpenId(){
        PageRequest pageRequest = new PageRequest(0,1);

        Page<OrderMaster> result = orderMasterRepository.findByBuyerOpenid("110111",pageRequest);
        System.out.println(result.getTotalElements());

    }
}