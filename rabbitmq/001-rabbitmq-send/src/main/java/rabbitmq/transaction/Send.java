package rabbitmq.transaction;

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

            String queueName = "transactionQueue";

            channel.queueDeclare(queueName,true ,false ,false , null);

            String exchangeName = "directExchange";
            channel.exchangeDeclare(exchangeName, "direct",true);

            channel.queueBind(queueName, exchangeName, "transactionKey");
            /**
             * 开启事务，事务可以保证当前通道中的当前事务中所有的消息要么全部写入到MQ要么全部都不写入MQ
             * 如果开启了事务必须要显示的调用Channel中的txCommit()方法提交事务或txRollback()方法回滚事务
             * 注意：
             *   事务只能保证这些消息要么全部发送成功或全部不能发送成功，但是它不能保证消息一定会进入想到MQ中
             */
            channel.txSelect();
            String message = "Transaction Test Message";

            channel.basicPublish(exchangeName, "transactionKey", null, message.getBytes());
            //提交事务将当前通道中当前事务中的所有消息全部写入到MQ服务器，同时清空当内存中标前事务中的这些已经提交的消息
            //并结束当前事务
            channel.txCommit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel!=null){
                try {
                    //关闭前回滚事务，释放当前事务中所有没有提交的消息，并结束是当前事务
                    //建议至少要在关闭通道前完成事务的回滚用于确保可以清空内存顺利的被JVM进行垃圾回收
                    channel.txRollback();
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
