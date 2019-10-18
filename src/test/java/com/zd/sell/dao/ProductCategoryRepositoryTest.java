package com.zd.sell.dao;

import com.zd.sell.pojo.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

//    @Test
//    public Optional<ProductCategory> findOneTest(){
//
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryId(2);
//        Example<ProductCategory> example = Example.of(productCategory);
//        return repository.findOne(example);
//    }

    @Test
    //测试中的事务回滚，只是用来测试写的对不对，就算你成功也会被回滚
    @Transactional
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(4);
        repository.save(productCategory);
    }

    @Test
    public void update(){
//        ProductCategory one = repository.getOne(2);
//        one.setCategoryId(3);
//        repository.save(one);
        Optional<ProductCategory> opt = repository.findById(2);
        ProductCategory productCategory = opt.get();
        productCategory.setCategoryType(10);
        repository.save(productCategory);

    }

    @Test
    public void testfindByCategoryTypeIn(){
        List<ProductCategory> prs = repository.findByCategoryTypeIn(Arrays.asList(2, 10, 11));
        System.out.println(prs);
    }



}