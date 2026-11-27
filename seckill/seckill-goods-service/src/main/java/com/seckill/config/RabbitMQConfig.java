package com.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 许彤
 * 2020/12/29
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue seckillQueue(){
        return new Queue("seckillQueue");
    }

    @Bean
    public FanoutExchange seckillExchange(){
        return new FanoutExchange("seckillExchange");
    }

    @Bean
    public Binding seckillBinding(Queue seckillQueue,FanoutExchange seckillExchange){
        return BindingBuilder.bind(seckillQueue).to(seckillExchange);
    }
}
