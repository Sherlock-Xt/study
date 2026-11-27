package com.seckill.controller;

import com.seckill.commons.ReturnObject;
import com.seckill.model.Orders;
import com.seckill.service.OrdersService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/30
 */
@RestController
public class OrdersController {

    @Resource
    OrdersService ordersService;

    @RequestMapping(value = "/getOrderResult")
    public Object getOrderResult(Integer goodsId ,Integer uid){
        ReturnObject<Orders> ro = ordersService.getOrderResult(goodsId ,uid);
        return ro;
    }
}
