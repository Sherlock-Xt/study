package com.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.mapper.GoodsMapper;
import com.seckill.model.Goods;
import com.seckill.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
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
 * 2020/12/30
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
        return goodsMapper.queryAllGoodsList();
    }

    @Override
    public Goods queryGoodsElt(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    /**
     *
     * @param goodsId 商品id
     * @param randomName 商品随机名
     * @param buyPrice 购买金额
     * @return 秒杀信息
     * 0 秒杀成功
     * 1 商品信息错误
     * 2 库存不足
     * 3 客户购买过此商品了
     * 4 被限流
     */
    @Override
    public Integer seckill(Integer goodsId, String randomName, BigDecimal buyPrice ,Integer uid) {
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        String goodStore = (String) redisTemplate.opsForValue().get(Constants.GOODS_STORE + randomName);
        if (goodStore==null||"".equals(goodStore)){
            return 1;
        }
        if (Integer.valueOf(goodStore)<=0){
            return 2;
        }
        String purchaseLimits = (String) redisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS + goodsId + uid);
        if (purchaseLimits!=null&&!"".equals(purchaseLimits)){
            return 3;
        }

        Long currentLimit = redisTemplate.opsForValue().increment(Constants.CURRENT_LIMITING);
        if (currentLimit!=null&&currentLimit>=1000){
            redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
            return 4;
        }

        Map<String,Object> goodsMap = new HashMap<>();
        goodsMap.put("goodsId", goodsId);
        goodsMap.put("uid", uid);
        goodsMap.put("buyPrice", buyPrice);
        while (true) {
            Object result = redisTemplate.execute(new SessionCallback() {
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    List<String> keys = new ArrayList<>();
                    keys.add(Constants.GOODS_STORE + randomName);
                    keys.add(Constants.PURCHASE_LIMITS + goodsId + uid);
                    redisTemplate.watch(keys);

                    String goodsStore = (String) redisTemplate.opsForValue().get(Constants.GOODS_STORE + randomName);

                    if (Integer.valueOf(goodsStore) <= 0) {
                        redisTemplate.unwatch();
                        return 2;
                    }

                    String purchaseLimit = (String) redisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS + goodsId + uid);
                    if (purchaseLimit != null && !"".equals(purchaseLimit)) {
                        redisTemplate.unwatch();
                        return 3;
                    }

                    redisTemplate.multi();

                    redisTemplate.opsForValue().decrement(Constants.GOODS_STORE + randomName);
                    redisTemplate.opsForValue().set(Constants.PURCHASE_LIMITS + goodsId + uid, "1");

                    redisTemplate.opsForZSet().add(Constants.ORDERS, JSONObject.toJSONString(goodsMap),System.currentTimeMillis());

                    redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
                    return redisTemplate.exec();
                }
            });

            if (result instanceof Integer){
                redisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
                return (Integer) result;
            }

            List resultList = (List) result;

            if (resultList!=null&&!resultList.isEmpty()){
                break;
            }
        }

        amqpTemplate.convertAndSend("seckillExchange", "", JSONObject.toJSONString(goodsMap));

        return 0;
    }
}
