package com.zd.sell.dao;

import com.zd.sell.pojo.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单主表dao
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /**
     * 按照买家的OpenId来分页查询订单
     * @param buyerOpenid 买家openId
     * @param pageable  分页
     * @return
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}

