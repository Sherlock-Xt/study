package com.rabbitmq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/24
 */
@Service
public class SendService {
    //注入一个AMQP的模板工具类，可以利用这个类来完成对RabbitMQ的基本操作
    @Resource
    AmqpTemplate amqpTemplate;
    //注入一个RabbitMQ的模板工具类，可以利用这个对象来完成对RabbitMQ的特殊操作例如事务等等
    @Resource
    RabbitTemplate rabbitTemplate;

    public void directSend(String message){
        amqpTemplate.convertAndSend("directBootExchange", "directBootKey", message);

    }

    public void fanoutSend(String message){
        amqpTemplate.convertAndSend("fanoutBootExchange", "", message);

    }

    public void topicSend(String message){
        amqpTemplate.convertAndSend("topicBootExchange", "aa.aa.aa", message);

    }

}
