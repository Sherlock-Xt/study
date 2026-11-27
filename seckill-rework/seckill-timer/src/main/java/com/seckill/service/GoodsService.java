package com.seckill.service;

import com.seckill.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 许彤
 * 2020/12/30
 */
@FeignClient(name = "seckill-goods-service")
public interface GoodsService {
    @RequestMapping(value = "/queryAllGoodsList")
    List<Goods> queryAllGoodsList();

}
