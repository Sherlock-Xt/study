package com.masterslave.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义动态数据源，并继承动态数据源的抽象父类
 */
public class MyDataSource extends AbstractRoutingDataSource {
    /**
     * 用于动态切换数据源,根据返回值（数据源）动态切换
     * @return
     */
    protected Object determineCurrentLookupKey() {
        //获取数据源名称
        return MyThreadLocal.getDataSource();
    }
}
