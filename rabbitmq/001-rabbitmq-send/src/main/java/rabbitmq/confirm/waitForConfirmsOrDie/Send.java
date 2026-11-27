package rabbitmq.confirm.waitForConfirmsOrDie;

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
             * 开启发送确认者模式
             */
            channel.confirmSelect();
            String message = "Confirm Test Message";

            channel.basicPublish(exchangeName, "confirmKey", null, message.getBytes());
            try {
                /**
                 * 批量发送者确认模式
                 *    批量发送者模式所有消息发送完成后，调用一次确认方法,如果MQ服务返回了确认信息则当前方法正常结束
                 *    否则就会抛出异常InterruptedException 来中断线程的阻塞，这时我们就需要补发消息
                 *  注意：
                 *     1、这个方法可以指定给一个long类型timeout参数用于指定阻塞线程的最大时间，会抛出一个 TimeoutException异常
                 *        抛出这个异常我们也不能确定消息是否发送成功需要吧补发消息
                 *     2、消息补发会造成MQ服务器中出现重复的消息，这时消费者必须要拥有消息防重复的能力
                 *     3、批量确认模式不需要每次都调用阻塞的方法，因此在消息确认的效率上比普通模式要快，但是一旦触发补发机制
                 *     由于我们不能清楚的知道哪条消息没有发送成功，因此我们需要将所有的消息进行补发，那么补发效率低于普通模式
                 */
                channel.waitForConfirmsOrDie(50000L);
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
