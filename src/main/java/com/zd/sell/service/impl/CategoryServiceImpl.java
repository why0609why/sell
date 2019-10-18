package com.zd.sell.service.impl;

import com.zd.sell.dao.ProductCategoryRepository;
import com.zd.sell.pojo.ProductCategory;
import com.zd.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 对种类的操作
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * 注入ProductCategory的dao层对象
     */
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    /**
     * 返回id查询一个ProductCategory
     * @param categoryId
     * @return
     */
    @Override
    public ProductCategory findOne(Integer categoryId) {
        Optional<ProductCategory> opt = productCategoryRepository.findById(categoryId);
        return opt.get();
    }


    /**
     * 返回全部的ProductCategory
     * @return
     */
    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }


    /**
     * 返回Productgory表中CategoryType字段的值在categoryTypeList这个list中的所有ProductCategory对象
     * @param categoryTypeList
     * @return
     */
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryTypeList);
    }



    /**
     * 增添一个ProductCategory，返回增添的ProductCategory对象
     * @param productCategory
     * @return
     */
    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}
