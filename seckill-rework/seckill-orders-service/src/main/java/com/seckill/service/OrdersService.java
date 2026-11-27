package com.seckill.service;

import com.seckill.commons.ReturnObject;
import com.seckill.model.Orders;

/**
 * 许彤
 * 2020/12/30
 */
public interface OrdersService {
    int addOrders(Orders orders);

    ReturnObject<Orders> getOrderResult(Integer goodsId, Integer uid);
}
