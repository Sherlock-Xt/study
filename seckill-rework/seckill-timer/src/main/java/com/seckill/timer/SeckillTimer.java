package com.seckill.timer;

import com.seckill.commons.Constants;
import com.seckill.model.Goods;
import com.seckill.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 许彤
 * 2020/12/30
 */
@Component
public class SeckillTimer {
    @Resource
    GoodsService goodsService;
    @Resource
    RedisTemplate redisTemplate;
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    @Resource
    AmqpTemplate amqpTemplate;

    @Scheduled(cron = "0/5 * * * * *")
    public void initSeckillData(){
        List<Goods> goodsList = goodsService.queryAllGoodsList();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        for (Goods goods : goodsList) {
            redisTemplate.opsForValue().setIfAbsent(Constants.GOODS_STORE
            +goods.getRandomName(), goods.getStore()+"");
        }
        System.out.println("=======redis初始化完成=======");
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void checkOrders(){
        long systemTime = System.currentTimeMillis();
        long endTime = systemTime - 1000 * 5 * 60;
        Set<String> orders = redisTemplate.opsForZSet().rangeByScore(Constants.ORDERS, 0, endTime);
        for (String order : orders) {
            System.out.println(order);
            amqpTemplate.convertAndSend("seckillExchange", "", order);
        }

        System.out.println("=======掉单处理完成=======");
    }

}
