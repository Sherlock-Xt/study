package com.masterslave.datasource;

/**
 * 许彤
 * 2021/1/4
 */
public class MyThreadLocal {
    private static final ThreadLocal<String> local = new ThreadLocal<String>();
    public static void setDataSource(String dataSourceName){
        local.set(dataSourceName);
    }
    public static String getDataSource(){
        return local.get();
    }
}
