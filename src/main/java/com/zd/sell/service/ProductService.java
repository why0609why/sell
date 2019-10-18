package com.zd.sell.service;

import com.zd.sell.dto.CartDTO;
import com.zd.sell.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ProductInfo的service层接口
 */
public interface ProductService {

    /**
     * 返回productId查询商品指定id的ProductInfo
     * @param productId
     * @return
     */
    ProductInfo findOne(String productId);


    /**
     * 返回所有上架的ProductInfo
     * @return
     */
    List<ProductInfo> findUpAll();


    /**
     * 分页返回所有的商品
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);


    /**
     * 插入一个商品
     * @param productInfo
     * @return
     */
    ProductInfo save(ProductInfo productInfo);


    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);
}
