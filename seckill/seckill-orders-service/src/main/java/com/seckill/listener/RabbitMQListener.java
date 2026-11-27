package com.seckill.listener;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.model.Orders;
import com.seckill.service.OrderService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/29
 */
@Component
public class RabbitMQListener {
    @Resource
    OrderService orderService;
    @Resource
    RedisTemplate redisTemplate;
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @RabbitListener(bindings = {@QueueBinding(value = @Queue("seckillQueue"),exchange = @Exchange(name = "seckillExchange",type = "fanout"))})
    public void noSeckillMessage(String orderMessage){
        Orders orders = JSONObject.parseObject(orderMessage,Orders.class);
        int result = orderService.addSeckillOrders(orders);
        if (result==0){
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);
            redisTemplate.opsForZSet().remove(Constants.ORDERS, orderMessage);
        }
    }
}
