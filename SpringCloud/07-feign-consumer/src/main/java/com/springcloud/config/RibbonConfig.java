package com.springcloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 许彤
 * 2020/12/26
 */
@Configuration
public class RibbonConfig {

    @Bean
    public IRule iRule(){
        return new RoundRobinRule();
    }
}
