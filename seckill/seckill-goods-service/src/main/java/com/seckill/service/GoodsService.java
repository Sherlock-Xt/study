package com.seckill.service;

import com.seckill.commons.ReturnObject;
import com.seckill.model.Goods;

import java.math.BigDecimal;
import java.util.List;

/**
 * 许彤
 * 2020/12/28
 */

public interface GoodsService {

    List<Goods> queryAllGoodsList();

    Goods queryGoodsElt(Integer id);

    Integer seckill(Integer goodsId, String randomName, Integer uid , BigDecimal buyPrice);
}
