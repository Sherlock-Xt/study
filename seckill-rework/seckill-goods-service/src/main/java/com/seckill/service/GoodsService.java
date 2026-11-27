package com.seckill.service;

import com.seckill.model.Goods;

import java.math.BigDecimal;
import java.util.List;

/**
 * 许彤
 * 2020/12/30
 */
public interface GoodsService {
    List<Goods> queryAllGoodsList();

    Goods queryGoodsElt(Integer goodsId);

    Integer seckill(Integer goodsId, String randomName, BigDecimal buyPrice ,Integer uid);
}
