package com.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard//激活hystrix仪表盘
public class Dashboard {

    public static void main(String[] args) {
        SpringApplication.run(Dashboard.class, args);
    }

}
