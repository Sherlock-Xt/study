package com.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.commons.ReturnObject;
import com.seckill.mapper.OrdersMapper;
import com.seckill.model.Orders;
import com.seckill.service.OrdersService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 许彤
 * 2020/12/30
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    @Resource
    OrdersMapper ordersMapper;
    @Resource
    RedisTemplate redisTemplate;
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    @Override
    public int addOrders(Orders orders) {
        try {
            orders.setBuyNum(1);
            orders.setStatus(1);
            orders.setOrderMoney(orders.getBuyPrice().multiply(new BigDecimal(orders.getBuyNum())));
            orders.setCreateTime(new Date());
            ordersMapper.insertSelective(orders);
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);
            redisTemplate.opsForValue().set(Constants.ORDERS_RESULT+orders.getGoodsId()+orders.getUid(), JSONObject.toJSONString(orders));
        } catch (DuplicateKeyException e) {
            System.out.println(e.getClass());
        }

        return 0;
    }

    @Override
    public ReturnObject<Orders> getOrderResult(Integer goodsId, Integer uid) {
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        ReturnObject<Orders> ro = null;
        String ordersStr = (String) redisTemplate.opsForValue().get(Constants.ORDERS_RESULT + goodsId + uid);
        if (ordersStr==null||"".equals(ordersStr)){
            ro = new ReturnObject<>();
            ro.setCode(Constants.ERROR);
            ro.setMessage("订单不存在");
            return ro;
        }
        Orders orders = JSONObject.parseObject(ordersStr, Orders.class);
        ro = new ReturnObject<>();
        ro.setCode(Constants.OK);
        ro.setResult(orders);
        return ro;
    }
}
