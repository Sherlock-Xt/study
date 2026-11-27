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
 * 2020/12/28
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
    /**
     * 设置一个定时器，由于是实验，目前设置每5秒钟执行一次，而实际工作中初始化数据可能每天只执行一次，或者每周、月都有可能
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void initSeckillDataToRedis(){
        //以下方法是查询所有的商品，实际的秒杀只是查询即将开始秒杀的商品
        List<Goods> goodsList = goodsService.queryAllGoodsList();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        for (Goods goods : goodsList) {
            //redis写入数据的规则是，如果数据不存在就创建并写入，如果数据存在，就覆盖原有的数据，作为保存库存的数据，我们不需要redis每次都覆盖数据，因此我们将原有的set方法替换为setIfAbsent方法,该方法的意思是，如果不存在就创建，如果存在就不变
            redisTemplate.opsForValue().setIfAbsent(Constants.GOODS_STORE+goods.getRandomName(), goods.getStore()+"");
        }
        System.out.println("=======redis初始化数据完成========");
    }


    @Scheduled(cron = "0/5 * * * * *")
    public void reCheckOrder(){
        //获取当前系统时间的毫秒值
        long systemTime = System.currentTimeMillis();
        //获取5分钟以前的毫秒值
        long endTime = systemTime - 1000 * 60 * 5;
        //从redis里面取5分钟之前所有的订单
        //因为是测试，5秒钟执行一次，所以时间设置为5分钟，但是实际工作中，我们的定时器间隔和要查询的订单的时间基本一致，例如：定时器30min执行一次，每次获取系统时间30min前的订单
        Set<String> orders = redisTemplate.opsForZSet().rangeByScore(Constants.ORDERS, 0, endTime);
        for (String order : orders) {
            System.out.println(order);
            amqpTemplate.convertAndSend("seckillExchange", "", order);
        }
        System.out.println("=======掉单处理完成======");
    }
}
