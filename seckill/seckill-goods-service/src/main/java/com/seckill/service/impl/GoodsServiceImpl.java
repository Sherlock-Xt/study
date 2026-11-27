package com.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.commons.ReturnObject;
import com.seckill.mapper.GoodsMapper;
import com.seckill.model.Goods;
import com.seckill.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 许彤
 * 2020/12/28
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    RedisTemplate redisTemplate;
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    @Resource
    AmqpTemplate amqpTemplate;
    @Override
    public List<Goods> queryAllGoodsList() {
        //获取所有的秒杀商品信息
        //实际工作时这里并发量可能很高，必要时可以从Redis中获取数据
        //而且不能时获取所有的数据，应该根据系统时间获取正在开始活动或即将开始活动的商品
        return goodsMapper.queryAllGoodsList();
    }

    @Override
    public Goods queryGoodsElt(Integer id) {
        return goodsMapper.queryGoodsElt(id);
    }

    /**
     * 秒杀的主业务逻辑方法，需要在这里完成 限流 限购 防止超卖 防止掉单 减库存等操作
     * 这个请求一定并发量非常高，所以要优先操作redis，而不是数据库
     * @param goodsId 商品id
     * @param randomName 商品的随机名
     * @param uid 用户id
     * @return
     * 0 表示 秒杀成功
     * 1 表示 商品信息错误
     * 2 表示 没有库存了
     * 3 表示 用户已经购买过这个商品了
     * 4 表示 服务器繁忙
     */
    @Override
    public Integer seckill(Integer goodsId, String randomName, Integer uid , BigDecimal buyPrice) {
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        ReturnObject ro = new ReturnObject();
        String strStore = (String) redisTemplate.opsForValue().get(Constants.GOODS_STORE + randomName);
        //过滤非法请求和优先判断库存是否已经清空
        if (strStore==null||"".equals(strStore)){
            return 1;
        }
        if (Integer.valueOf(strStore)<=0){
            return 2;
        }
        //限购
        String purchaseLimits = (String) redisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS + goodsId + uid);
        if (purchaseLimits!=null&&!"".equals(purchaseLimits)){
            ro.setCode(Constants.ERROR);
            ro.setMessage("不能重复购买!");
            return 3;
        }
        //限流
        /*String currentLimits = (String) redisTemplate.opsForValue().get(Constants.CURRENT_LIMITING);*/
        //为了防止高并发时一起进入服务器导致限流个人数没有正确变化情况的发生，直接使用redis的更新数据自动使用单线程的机制解决问题，上面的直接查询方法有可能造成查询的是脏数据，舍弃使用
        Long currentLimits = redisTemplate.opsForValue().increment(Constants.CURRENT_LIMITING);
        if (currentLimits>=1000){
            //服务器人数上限，不能让用户再继续访问，直接返回方法
            redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
            ro.setCode(Constants.ERROR);
            ro.setMessage("系统繁忙!");
            return 4;
        }
        //定义订单备份数据，把用户id和商品id保存到map集合中备份
        Map<String,Object> orderMap = new HashMap();
        orderMap.put("uid", uid);
        orderMap.put("goodsId", goodsId);
        orderMap.put("buyPrice", buyPrice);
        //调用redis的执行方法，利用回调参数来实现事务的操作，这个方法会返回一个object类型的对象表示执行结果
        //result的返回值有两种，一种是Integer，代表逻辑错误，一种是list，如果长度为0代表事务提交失败，如果大于0代表事务提交成功
        while (true) {
            Object result = redisTemplate.execute(new SessionCallback() {
                //回调的执行方法，这个方法来完成具体的redis操作，参数一为模板对象
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    List<String> keys = new ArrayList<>();
                    keys.add(Constants.GOODS_STORE + randomName);
                    keys.add(Constants.PURCHASE_LIMITS + goodsId +uid);
                    //设置key的监控,如果当前线程的事务在提交时，发现监控的key所对应的数据被其他线程修改了，那么当前线程的事务会放弃提交
                    redisTemplate.watch(keys);
                    //再次判断库存是否为空
                    String goodsStore = (String) redisTemplate.opsForValue().get(Constants.GOODS_STORE + randomName);
                    if (Integer.valueOf(goodsStore) <= 0) {
                        redisOperations.unwatch();//释放key监控
                        return 2;
                    }
                    String purchaseLimits = (String) redisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS + goodsId + uid);
                    if (purchaseLimits != null && !"".equals(purchaseLimits)) {
                        redisOperations.unwatch();//释放key监控
                        return 3;
                    }
                    //如果程序能执行到这里，说明库存一定有货物且顾客一定没购买过,就可以开始减少库存并添加购买记录了
                    //开启事务
                    redisTemplate.multi();
                    //减少库存
                    redisTemplate.opsForValue().decrement(Constants.GOODS_STORE + randomName);
                    //添加购买记录
                    redisTemplate.opsForValue().set(Constants.PURCHASE_LIMITS + goodsId + uid, "1");
                    //使用orders作为key，使用订单的用户id和订单id的map的json字符串做value，使用系统的时间做分数，将订单的备份数据写入redis，保证唯一性，由于在事务中，所以只要购买记录添加成功，数据备份一定会成功，如果存在掉单，则使用timer的防掉单将数据再次写入MQ中，备份数据删除的唯一理由是，订单已经成功的持久化到数据库中
                    redisTemplate.opsForZSet().add(Constants.ORDERS, JSONObject.toJSONString(orderMap), System.currentTimeMillis());
                    //用户离场，限流-1
                    redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
                    //执行事务的提交，会返回一个集合，如果集合的长度大于0则表示提交事务成功，减少库存和添加购买记录成功
                    //反之，如果集合的长度等于0，则代表事务提交失败,原因有二，可能库存变化，可能多线程购买
                    return redisTemplate.exec();
                }
            });

            //判断result的类型是否为integer
            if (result instanceof Integer) {
                redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
                return (Integer) result;
            }

            //判断result集合的长度
            List resultList = (List) result;
            if (resultList!=null&&!resultList.isEmpty()) {
                break;
            }
        }
        //订单消息入队
        amqpTemplate.convertAndSend("seckillExchange", "", JSONObject.toJSONString(orderMap));

        return 0;
    }
}
