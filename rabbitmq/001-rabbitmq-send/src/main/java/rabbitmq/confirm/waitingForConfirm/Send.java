package rabbitmq.confirm.waitingForConfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 许彤
 * 2020/12/23
 */
public class Send {
    public static void main(String[] args) {
        /**
         * 创建连接工厂对象，用于配置rabbitmq的连接信息，利用这个对象来创建连接对象
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.152.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("xutong3254");
        //定义连接对象
        Connection connection = null;
        //定义通道对象
        Channel channel = null;

        try {
            //创建连接
            connection = factory.newConnection();
            //创建通道，用于完成消息的发送和接收操作
            channel = connection.createChannel();

            String queueName = "confirmQueue";

            channel.queueDeclare(queueName,true ,false ,false , null);

            String exchangeName = "confirmExchange";
            channel.exchangeDeclare(exchangeName, "direct",true);

            channel.queueBind(queueName, exchangeName, "confirmKey");
            /**
             * 开启事务，事务可以保证当前通道中的当前事务中所有的消息要么全部写入到MQ要么全部都不写入MQ
             * 如果开启了事务必须要显示的调用Channel中的txCommit()方法提交事务或txRollback()方法回滚事务
             * 注意：
             *   事务只能保证这些消息要么全部发送成功或全部不能发送成功，但是它不能保证消息一定会进入想到MQ中
             */
            channel.confirmSelect();
            String message = "Confirm Test Message";

            channel.basicPublish(exchangeName, "confirmKey", null, message.getBytes());
            try {
                /**
                 * 普通发送者确认模式
                 *    普通发送者模式每次只能确认一条消息，如果同时需要发送多条消息，那么每次发送完成后都需要调用一次确认模式
                 *    但是即使不调用确认方法不会影响消息发送。这个方法会阻塞当前线程等待MQ服务返回确认信息，如果MQ服务器返回
                 *    确认收到消息那么当前方法会返回一个true，否则返回一个false，没有收到消息则需要补发
                 *  注意：
                 *     1、这个方法会抛出一个异常 InterruptedException ，抛出这个异常表示wait被意外中断，时我们不能确定MQ服务器是否
                 *        接收成功，这是需要补发消息
                 *     2、这个方法可以指定给一个long类型timeout参数用于指定阻塞线程的最大时间，会抛出一个 TimeoutException异常
                 *        抛出这个异常我们也不能确定消息是否发送成功需要吧补发消息
                 *     3、消息补发会造成MQ服务器中出现重复的消息，这时消费者必须要拥有消息防重复的能力
                 */
                boolean flag = channel.waitForConfirms(50000L);
                //进入if表示mq返回的是false，意思是没有接收到发送的消息，需要再补发一次，可以用递归调用或者循环(if改成while)
                if (!flag){
                    System.out.println("补发消息");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel!=null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
