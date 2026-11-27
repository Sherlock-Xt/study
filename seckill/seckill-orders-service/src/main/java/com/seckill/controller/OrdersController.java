package com.seckill.controller;

import com.seckill.commons.ReturnObject;
import com.seckill.model.Orders;
import com.seckill.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/29
 */
@RestController
public class OrdersController {
    @Resource
    OrderService orderService;

    @RequestMapping(value = "/getOrderResult")
    public Object getOrderResult(Integer goodsId ,Integer uid){
        ReturnObject<Orders> ro = orderService.getOrderResult(goodsId,uid);
        return ro;
    }
}
