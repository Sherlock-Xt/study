package com.seckill.controller;

import com.seckill.commons.Constants;
import com.seckill.commons.ReturnObject;
import com.seckill.model.Goods;
import com.seckill.service.GoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 许彤
 * 2020/12/30
 */
@RestController
public class GoodsController {
    @Resource
    GoodsService goodsService;

    @RequestMapping(value = "/queryAllGoodsList")
    public Object queryAllGoodsList(){
        List<Goods> goodsList = goodsService.queryAllGoodsList();
        return goodsList;
    }

    @RequestMapping(value = "/queryGoodsElt")
    public Object queryGoodsElt(Integer goodsId){
        Goods goods = goodsService.queryGoodsElt(goodsId);
        return goods;
    }

    @RequestMapping(value = "/seckill")
    public Object seckill(Integer goodsId, String randomName , BigDecimal buyPrice ,Integer uid){
        ReturnObject ro = new ReturnObject();
        Integer result = goodsService.seckill(goodsId, randomName ,buyPrice ,uid);
        switch (result){
            case 0:
                ro.setCode(Constants.OK);
                ro.setMessage("秒杀成功");
                break;
            case 1:
                ro.setCode(Constants.ERROR);
                ro.setMessage("商品信息错误");
                break;
            case 2:
                ro.setCode(Constants.ERROR);
                ro.setMessage("商品已被抢完");
                break;
            case 3:
                ro.setCode(Constants.ERROR);
                ro.setMessage("您已购买过此商品");
                break;
            case 4:
                ro.setCode(Constants.ERROR);
                ro.setMessage("服务器繁忙。。。");
                break;
            default:
                ro.setCode(Constants.ERROR);
                ro.setMessage("网络错误。。。");
                break;
        }

        return ro;
    }


}
