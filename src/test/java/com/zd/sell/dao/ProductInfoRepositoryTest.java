package com.zd.sell.dao;

import com.zd.sell.pojo.ProductInfo;
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
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    /**
     * 测试插入一个商品
     */
    @Test
    public void saveTest(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("小米粥");
        productInfo.setProductPrice(new BigDecimal(4.00));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("好喝");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);

        productInfoRepository.save(productInfo);
    }

    /**
     * 测试通过商品状态查询商品
     */
    @Test
    public void testFindByProductStatus(){
        List<ProductInfo> byProductStatus = productInfoRepository.findByProductStatus(0);
        System.out.println(byProductStatus.size());
    }

}