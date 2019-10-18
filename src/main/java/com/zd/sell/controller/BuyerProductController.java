package com.zd.sell.controller;


import com.zd.sell.VO.ProductInfoVO;
import com.zd.sell.VO.ProductVO;
import com.zd.sell.VO.ResultVO;
import com.zd.sell.pojo.ProductCategory;
import com.zd.sell.pojo.ProductInfo;
import com.zd.sell.service.CategoryService;
import com.zd.sell.service.ProductService;
import com.zd.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 返回带有格式的所有种类的所有商品
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(){
        //查询所有的上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //查询所有上架的类目
        List<Integer> categoryList = new ArrayList<>();
        //先将上架的商品的种类放到list中
        for (ProductInfo productInfo : productInfoList) {
            categoryList.add(productInfo.getCategoryType());
        }

        //查询所有的上架的商品的种类集合
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryList);


        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategorytype(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());


            List<ProductInfoVO> productInfoVOList = new ArrayList<>();

            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    //BeanUtils工具可以把左面的对象的属性赋给右边相同属性名称的属性
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }


        return ResultVOUtil.success(productVOList);
    }

}
