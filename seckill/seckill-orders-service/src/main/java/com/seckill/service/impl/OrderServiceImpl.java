package com.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.commons.ReturnObject;
import com.seckill.mapper.OrdersMapper;
import com.seckill.model.Orders;
import com.seckill.service.OrderService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

/**
 * 许彤
 * 2020/12/29
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    OrdersMapper ordersMapper;
    @Resource
    RedisTemplate redisTemplate;
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public int addSeckillOrders(Orders orders) {
        orders.setBuyNum(1);
        orders.setCreateTime(new Date());
        orders.setStatus(1);
        orders.setOrderMoney(orders.getBuyPrice().multiply(new BigDecimal(orders.getBuyNum())));
        try {
            //这行代码可能会抛出异常，如果抛出异常就说明该条数据已经存在了，异常的类型为DuplicateKeyException，表示违反唯一性约束异常
            ordersMapper.insertSelective(orders);
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);
            //使用统一前缀ORDERS_RESULT+商品ID+用户ID当做Key，orders的json字符串做value，将数据存入到redis，用于通知用户进行支付，这个数据不一定会执行成功，因为此条数据执行可能耗费时间较长，用户不一定能等到该方法执行完再离开抢购页面，所以该条数据必须要设置超时时间
            Duration timeout = Duration.ofSeconds(60*30);//设置超时时间，为30min
            redisTemplate.opsForValue().set(Constants.ORDERS_RESULT+orders.getGoodsId()+orders.getUid(), JSONObject.toJSONString(orders),timeout);
        } catch (DuplicateKeyException e) {
            System.out.println(e.getClass());
        }

        return 0;
    }

    @Override
    public ReturnObject<Orders> getOrderResult(Integer goodsId, Integer uid) {
        ReturnObject<Orders> ro = null;
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        String ordersResult = (String) redisTemplate.opsForValue().get(Constants.ORDERS_RESULT + goodsId + uid);
        if (ordersResult==null||"".equals(ordersResult)){
            ro = new ReturnObject<>();
            ro.setCode(Constants.ERROR);
            ro.setMessage("订单不存在");
            return ro;
        }
        Orders orders = JSONObject.parseObject(ordersResult, Orders.class);
        ro = new ReturnObject<>();
        ro.setCode(Constants.OK);
        ro.setMessage("订单获取成功！");
        ro.setResult(orders);
        return ro;
    }
}
