package rabbitmq.exchange.topic;

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

            /**
             * Fanout类型交换机是消息一对多模式，通常用于广播，由于接收者的数量不能确认，而却接收者监听的队列也不能够相同
             * 所以在发送时不需要绑定队列，只需要确保交换机存在即可，如果是广播模式消费者需要完全接收消息那么就必须要在消费
             * 发送前启动接收者完成队列的监听。
             * 注意：
             *   Fanout交换机速度比Direct交换机要快，因此很多项目会使用Fanout替代Direct，如果不希望Fanout丢失消息
             *   那么就不要创建随机的队列，在发送和接收时同时绑定队列和交换机即可
             */

            String message = "Test Message";

            /**
             * 声明一个交换机
             * 参数 1 为交换机的名称取值任意 如果这个交换机名称已经存在则放弃创建直接使用，如果这个交换机名不存在则完成创建并使用这个交换机
             * 参数 2 为交换机类型，取值为direct 、fanout、topic、headers
             * 参数 3 为是否是持久化的交换机
             * 注意：
             *   可选代码，在绑定队列前确保这个交换机已经存在
             */
            String exchangeName = "topicExchange";
            channel.exchangeDeclare(exchangeName, "topic",true);

            /**
             * 发送消息 MQ
             *  参数 1 为交换机 名称
             *  参数 2 为消息的RoutingKey
             *  参数 3 为消息的属性通常为null即可
             *  参数 4 为具体的消息数据，类型为字节数组，可以是简单的字符串也可是对象，但是建议使用Json替代Java对象
             */
            channel.basicPublish(exchangeName, "aa.aa.aa", null, message.getBytes());

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
