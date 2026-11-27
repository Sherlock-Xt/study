package com.seckill.service;

import com.seckill.commons.ReturnObject;
import com.seckill.model.Orders;

/**
 * 许彤
 * 2020/12/29
 */
public interface OrderService {

    int addSeckillOrders(Orders orders);

    ReturnObject<Orders> getOrderResult(Integer goodsId, Integer uid);
}
