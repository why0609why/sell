package com.zd.sell.dao;

import com.zd.sell.pojo.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品种类dao
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    /**
     * 返回Productgory表中CategoryType字段的值在categoryTypeList这个list中的所有ProductCategory对象
     * jpa根据方法名生成的方法，里面包括自动封装，自动生成sql语句
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
