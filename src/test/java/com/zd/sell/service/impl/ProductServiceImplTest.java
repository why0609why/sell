package com.zd.sell.service.impl;

import com.zd.sell.enums.ProductStatusEnum;
import com.zd.sell.pojo.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    /**
     * 注入ProductServiceImpl对象
     */
    @Autowired
    private ProductServiceImpl productService;

    /**
     * 测试ProductServiceImpl类的通过id查询一个ProductInfo方法
     */
    @Test
    public void findOne() {
        ProductInfo one = productService.findOne("123456");
        Assert.assertEquals("123456", one.getProductId());
    }


    /**
     * 测试查询全部上架的商品的方法
     */
    @Test
    public void findUpAll() {
        List<ProductInfo> upAll = productService.findUpAll();
        System.out.println(upAll.size());
    }

    @Test
    public void findAll() {
        PageRequest request = new PageRequest(0, 2);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        System.out.println(productInfoPage.getTotalElements());

    }


    /**
     * 测试插入一个ProductInfo
     */
    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123458");
        productInfo.setProductName("矿泉水");
        productInfo.setProductPrice(new BigDecimal(1.00));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("凉水");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productInfo.setCategoryType(2);

        ProductInfo save = productService.save(productInfo);
        Assert.assertNotEquals(null,save);
    }
}