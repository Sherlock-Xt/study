package rabbitmq.exchange.direct;

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

            String message = "Test Message";
            /**
             * 声明一个队列
             * 参数 1 为队列名称取值任意，如果这个队列名称已经存在则放弃创建直接使用，如果这个队列名不存在则完成创建并使用这个队列
             * 参数 2 是否持久化的队列 true为持久化
             * 参数 3 为是否排外的true表示排外，如果有一个消费连接了当前队列那么在断开连接之前不允许其他消费者连接这个队列
             * 参数 4 为是否自动删除的true为自动删除的，当队列中没有消息也没有消费者监听时会自动删除这个队列
             * 注意：
             *   可选代码，但是要在发送消息前确保队列已经存在
             */
            String queueName = "directQueue";

            channel.queueDeclare(queueName,true ,false ,false , null);
            /**
             * 声明一个交换机
             * 参数 1 为交换机的名称取值任意 如果这个交换机名称已经存在则放弃创建直接使用，如果这个交换机名不存在则完成创建并使用这个交换机
             * 参数 2 为交换机类型，取值为direct 、fanout、topic、headers
             * 参数 3 为是否是持久化的交换机
             * 注意：
             *   可选代码，在绑定队列前确保这个交换机已经存在
             */
            String exchangeName = "directExchange";
            channel.exchangeDeclare(exchangeName, "direct",true);
            /**
             * 将队列与交换机进行绑定
             * 参数 1 为队列名称
             * 参数 2 为交换机名称
             * 参数 3 为绑定时的BindingKey
             * 注意：
             *   发送消息前需要确保交换机和队列成功绑定否则会丢失消息
             */
            channel.queueBind(queueName, exchangeName, "directKey");
            /**
             * 发送消息 MQ
             *  参数 1 为交换机 名称
             *  参数 2 为消息的RoutingKey
             *  参数 3 为消息的属性通常为null即可
             *  参数 4 为具体的消息数据，类型为字节数组，可以是简单的字符串也可是对象，但是建议使用Json替代Java对象
             *
             *  注意：
             *    1、发送消息前需要确保队列和交换机玩车绑定
             *    2、消息所携带的RoutingKey需要某个BindingKey完全一致
             */
            channel.basicPublish(exchangeName, "directKey", null, message.getBytes());

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
