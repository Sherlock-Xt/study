package com.masterslave.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.masterslave.datasource.MyDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 许彤
 * 2021/1/4
 */
@Configuration
public class MyDataSourceConfig {
    /*<!--配置主库3307的数据源-->
    <bean id="masterDruidDataSource" class="com.alibaba.druid.pool.DruidDataSource" >
        <property name="driverClassName" value="com.mysql.jdbc.Driver" />
        <property name="url" value="jdbc:mysql://192.168.152.128:3307/workdb" />
        <property name="username" value="root" />
        <property name="password" value="xutong3254" />
    </bean>
    */
    @Bean
    public DruidDataSource masterDruidDataSource(DataSourceConfig dataSourceConfig){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceConfig.getMasterDriverClassname());
        druidDataSource.setUrl(dataSourceConfig.getMasterUrl());
        druidDataSource.setUsername(dataSourceConfig.getMasterUsername());
        druidDataSource.setPassword(dataSourceConfig.getMasterPassword());
        return druidDataSource;
    }
    /*
    <!--配置从库3309的数据源-->
    <bean id="slaveDruidDataSource" class="com.alibaba.druid.pool.DruidDataSource" >
        <property name="driverClassName" value="com.mysql.jdbc.Driver" />
        <property name="url" value="jdbc:mysql://192.168.152.128:3309/workdb" />
        <property name="username" value="root" />
        <property name="password" value="xutong3254" />
    </bean>
    */
    @Bean
    public DruidDataSource slaveDruidDataSource(DataSourceConfig dataSourceConfig){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceConfig.getSlaveDriverClassname());
        druidDataSource.setUrl(dataSourceConfig.getSlaveUrl());
        druidDataSource.setUsername(dataSourceConfig.getSlaveUsername());
        druidDataSource.setPassword(dataSourceConfig.getSlavePassword());
        return druidDataSource;
    }
    /*
    <!--配置自定义数据源的bean-->
    <bean id="myDataSource" class="com.masterslave.datasource.MyDataSource" >
        <!--配置默认数据源为主库，如果没有切换数据源则直接使用主库，可选属性-->
        <property name="defaultTargetDataSource" ref="masterDruidDataSource" />
        <!--注入一个目标数据源，它是一个map集合类型-->
        <property name="targetDataSources" >
            <!--注入map集合-->
            <map>
                <!--
    注入集合中的一个元素
            属性
    key：用于指定元素在map集合中的key，取值任意
    value-ref：用于指定元素在map集合中的具体value，是一个引用类型，指定某个bean的id，这个bean需要是一个DataSource类型
                -->
                <entry key="master" value-ref="masterDruidDataSource"/>
                <entry key="slave" value-ref="slaveDruidDataSource"/>
            </map>
        </property>
    </bean>
    */
    @Bean
    public MyDataSource myDataSource(DruidDataSource masterDruidDataSource, DruidDataSource slaveDruidDataSource){
        MyDataSource myDataSource = new MyDataSource();
        myDataSource.setDefaultTargetDataSource(masterDruidDataSource);
        Map<Object,Object> map = new HashMap<>();
        map.put("master", masterDruidDataSource);
        map.put("slave", slaveDruidDataSource);
        myDataSource.setTargetDataSources(map);
        return myDataSource;
    }
    /*
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager" >
        <property name="dataSource" ref="myDataSource" />
    </bean>
    */
    @Bean
    public DataSourceTransactionManager transactionManager(MyDataSource myDataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(myDataSource);
        return dataSourceTransactionManager;
    }
    /*
    <tx:annotation-driven />

    <!--配置sql连接工厂-->
    <bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean" >
        <property name="dataSource" ref="myDataSource" />
    </bean>
    */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(MyDataSource myDataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myDataSource);
        return sqlSessionFactoryBean;
    }
    /*
    <!--配置包扫描配置-->
    <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer" >
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactoryBean" />
        <property name="basePackage" value="com.masterslave.mapper" />
    </bean>*/
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage("com.masterslave.mapper");
        return mapperScannerConfigurer;
    }
}
