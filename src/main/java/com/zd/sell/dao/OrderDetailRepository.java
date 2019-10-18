package com.zd.sell.dao;

import com.zd.sell.pojo.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单详情表
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    /**
     *  根据大订单id查询小订单表中所有大订单id的所有小订单
     * @param orderId
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);
}