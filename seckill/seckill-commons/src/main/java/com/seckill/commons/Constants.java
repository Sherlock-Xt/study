package com.seckill.commons;

/**
 * 常量类，通常用于定义一些系统中所有需要的常量数据
 */
public final class Constants {
    private Constants(){}

    public static final int OK=1;
    public static final int ERROR=0;
    //商品库存在Redis中的统一Key前缀
    public static final String GOODS_STORE="GOODS_STORE:";
    //用户限购在Redis中的统一Key前缀
    public static final String  PURCHASE_LIMITS="PURCHASE_LIMITS:";
    //限流计数器在Redis中的key
    public static final String CURRENT_LIMITING="CURRENT_LIMITING";
    //订单备份数据在Redis中的key
    public static final String ORDERS="ORDERS";
    //订单结果在Redis中的统一Key前缀
    public static final String ORDERS_RESULT="ORDERS_RESULT:";

}

