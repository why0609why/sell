package com.zd.sell.service;

import com.zd.sell.pojo.ProductCategory;

import java.util.List;

/**
 * ProductCategory的service层接口
 */
public interface CategoryService {

    /**
     * 返回id查询一个ProductCategory
     * @param categoryId
     * @return
     */
    ProductCategory findOne(Integer categoryId);


    /**
     * 返回全部的ProductCategory
     * @return
     */
    List<ProductCategory> findAll();


    /**
     * 返回Productgory表中CategoryType字段的值在categoryTypeList这个list中的所有ProductCategory对象
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);


    /**
     * 增添一个ProductCategory，返回增添的ProductCategory对象
     * @param productCategory
     * @return
     */
    ProductCategory save(ProductCategory productCategory);

}
