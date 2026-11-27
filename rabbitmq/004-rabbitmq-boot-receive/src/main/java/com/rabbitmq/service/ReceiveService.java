package com.rabbitmq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/24
 */
@Service
public class ReceiveService {

    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    RabbitTemplate rabbitTemplate;

    public void directReceive(){
        /**
         * 监听队列并接收消息
         * 这个接收不能自动持续性接收消息每次运行只能接收一次消息
         * 解决方案
         *   1、使用死循环不断调用receiveAndConvert方法，缺点是会阻塞当前线程,如果队列中没有消息则返回null
         *   2、为了不阻塞线程可以将receiveAndConvert写入到子线程中，单仍然会返回null
         *   3、使用消息监听器 （推荐）
         */
        String message = (String) amqpTemplate.receiveAndConvert("directBootQueue");
        System.out.println(message);
    }

    /**
     * @RabbitListener 用于标记当前方法是一消息监听方法，用于持续性监听队列接收消息
     *   属性
     *     queues 用于指定当前监听器需要监听的1个或多个队列，取值为String数组，用于指定队列名称
     *            这个属性所指定的队列必须要事先创建好并成功绑定队列
     * 注意：
     *   1、方法的形参为本次接收消息的具体消息数据
     *   2、如果当前方法正常结束Spring会确认消息，如果以异常结束，则不会确认消息
     */
    @RabbitListener(queues = {"directBootQueue"})
    public void directListener(String message){

        System.out.println(message);

    }

    /**
     * @RabbitListener 用于标记当前方法是一消息监听方法，用于持续性监听队列接收消息
     *   属性
     *    bindings 用于绑定队列的 取值为 @QueueBinding数组类型
     *       @QueueBinding 将某个队列与交换机进行绑定
     *         属性
     *           value  需要绑定的具体队列 取值为 @Queue类型  必选属性
     *              @Queue 用于声明一个队列并使用这个队列
     *                属性
     *                  name和value用于指定这个队列的名称取值任意，如果不指定队列名称则创建一个随机的队列
     *                  durable  是否持久化的队列 取值为String类型的 true 或 false
     *                  exclusive  是否排外 取值为String类型的 true 或 false
     *                  autoDelete  是否自动删除 取值为String类型的 true 或 false
     *           exchange 需要绑定的具体交换机  取值为 @Exchange 必选属性
     *              @Exchange 用于声明一个交换机并使用过这个交换机
     *                 属性
     *                   name和value 用于指定这个交换机的名称取值任意
     *                   type 用于指定这个交换机的类型取值为String的 direct 、fanout、topic、headers 默认值为direct
     *                   durable  是否持久化的交换机 取值为String类型的 true 或 false
     *                   autoDelete  是否自动删除 取值为String类型的 true 或 false
     *           key 绑定时的BindingKey  可选属性
     */
    /*@RabbitListener(bindings = {@QueueBinding(value = @Queue,exchange = @Exchange(name = "fanoutBootExchange",type = "fanout"))})
    public void fanoutListener01(String message){
        System.out.println("fanoutListener01"+message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue,exchange = @Exchange(name = "fanoutBootExchange",type = "fanout"))})
    public void fanoutListener02(String message){
        System.out.println("fanoutListener02"+message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue,exchange = @Exchange(name = "fanoutBootExchange",type = "fanout"))})
    public void fanoutListener03(String message){
        System.out.println("fanoutListener03"+message);
    }*/

    @RabbitListener(bindings = {@QueueBinding(value = @Queue,key = "aa",exchange = @Exchange(name = "topicBootExchange",type = "topic"))})
    public void topicListener01(String message){
        System.out.println("topicListener01,bindingKey:aa"+message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue,key = "aa.*",exchange = @Exchange(name = "topicBootExchange",type = "topic"))})
    public void topicListener02(String message){
        System.out.println("topicListener02,bindingKey:aa.*"+message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue,key = "aa.#",exchange = @Exchange(name = "topicBootExchange",type = "topic"))})
    public void topicListener03(String message){
        System.out.println("topicListener03,bindingKey:aa.#"+message);
    }



}
