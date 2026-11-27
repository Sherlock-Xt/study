package com.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;


/**
 * 许彤
 * 2020/12/24
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue directBootQueue(){
        //创建一个队列，本次选用的重载方法是：durable(持久化的)-true,exclusive(排外的)-false,autoDelete(自动删除的)-false
        return new Queue("directBootQueue");
    }

    @Bean
    public DirectExchange directBootExchange(){
        //创建一个队列，本次选用的重载方法是：只有一个交换机名称
        return new DirectExchange("directBootExchange");
    }

    /**
     * 如果方法的形参的名称以及参数类型，与某个携带了@Bean的方法的名称以及返回类型完全相同那么Spring的就自动将
     * 这个方法的返回值注入到当前方法的形参中
     * @param directBootQueue
     * @param directBootExchange
     * @return
     */
    @Bean
    public Binding directBinding(Queue directBootQueue,DirectExchange directBootExchange){
        return BindingBuilder.bind(directBootQueue).to(directBootExchange).with("directBootKey");
    }

    /*@Bean
    public Queue fanoutBootQueue(){
        //创建一个队列，本次选用的重载方法是：durable(持久化的)-true,exclusive(排外的)-false,autoDelete(自动删除的)-false
        return new Queue(UUID.randomUUID().toString());
    }*/
}
