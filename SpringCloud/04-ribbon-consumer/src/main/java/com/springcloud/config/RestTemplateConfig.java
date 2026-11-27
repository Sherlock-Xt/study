package com.springcloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
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

    /**
     * 定义一个ribbon的负载均衡策略bean到spring的上下文中
     */
    @Bean
    public IRule iRule(){
        //定义一个随机的负载均衡策略，用于改变ribbon的默认负载均衡策略
        return new RandomRule();
    }
}
