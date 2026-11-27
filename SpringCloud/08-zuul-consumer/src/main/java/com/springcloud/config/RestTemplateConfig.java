package com.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 许彤
 * 2020/12/24
 */
@Configuration
public class RestTemplateConfig {
    /**
     * @LoadBalanced 标记当前的RestTemplate对象支持Ribbon的负载均衡，默认负载均衡策略为轮询策略
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
