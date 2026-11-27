package com.masterslave.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 许彤
 * 2021/1/4
 */
@Configuration
//@Component
public class SlaveDataSourceConfig {

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
    <!--配置主库3307的sql连接工厂-->
    */
    @Bean
    public SqlSessionFactoryBean slaveSqlSessionFactoryBean(DruidDataSource slaveDruidDataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(slaveDruidDataSource);
        return sqlSessionFactoryBean;
    }
    /*
    <!--配置主库3307的包扫描配置-->
    */
    @Bean
    public MapperScannerConfigurer slaveMapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("slaveSqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage("com.masterslave.slave.mapper");
        return mapperScannerConfigurer;
    }
    /*
    <bean id="masterTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager" >
        <property name="dataSource" ref="masterDruidDataSource" />
    </bean>
    */
    /*@Bean
    public DataSourceTransactionManager masterTransactionManager(DruidDataSource masterDruidDataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(masterDruidDataSource);
        return dataSourceTransactionManager;
    }*/
    /*
    <!--将自定义的事务管理器对象配置到spring的事务注解驱动中-->
    <tx:annotation-driven transaction-manager="masterTransactionManager" />*/

}
