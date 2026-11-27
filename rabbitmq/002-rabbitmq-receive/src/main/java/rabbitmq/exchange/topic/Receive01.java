package rabbitmq.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 许彤
 * 2020/12/23
 */
public class  Receive01 {
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
             * 声明一个队列
             * 没有指定任何的参数那么就会生成一个名字随机的队列，这个队列是非持久化的，排外的、自动删除的
             * 当队列使用完成后，会自定删除这个队列，没有消费者连接时就会删除队列
             * getQueue() 返回这个随机队列的名称
             */

            String queueName = channel.queueDeclare().getQueue();

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
             * 将队列与交换机进行绑定
             * 参数 1 为队列名称
             * 参数 2 为交换机名称
             * 参数 3 为绑定时的BindingKey，由于交换机类型为fanout因此绑定时不需要绑定RoutingKey
             * 注意：
             *   接收消息前需要确保交换机和队列成功绑定否则会丢失消息
             */
            channel.queueBind(queueName, exchangeName, "aa");
            /**
             * 监听队列并接收消息
             * 参数 1 为需要监听的队列名称
             * 参数 2 为是否自动确认消息，true为自动消息确认，消息被接收后无论是否成功完成处理都会将消息从队列中移除
             * 参数 3 为消息处理的回调方法，我们可以利用这个对象的方法来处理MQ中的消息
             *  注意：
             *    1、接收消息时，basicConsume方法会启动一个子线程，会持续性的监听队列接收消息，因此为了能够成功的接收消息
             *    那么通道和连接对象不能关闭否则可能会导致消息无法接收、
             *    2、在消息接收前需要确保队列已经存在
             */
            channel.basicConsume(queueName, true,new DefaultConsumer(channel){
                /**
                 * 消息的处理方法，我们需要在这里完成消息的具体处理，消息的处理分为很多种
                 * 例如 将消息写入数据库，写入Redis或控制台输出等等
                 */
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("Receive01---BendingKey:aa"+message);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
