package com.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer//激活EurekaServer
public class EurekaServer9100 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer9100.class, args);
    }

}
