package rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 许彤
 * 2020/12/23
 */

public class Receive {
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

            String message = "测试消息";
            /**
             * 声明一个队列
             * 参数 1 为队列名称取值任意，如果这个队列名称已经存在则放弃创建直接使用，如果这个队列名不存在则完成创建并使用这个队列
             * 参数 2 是否持久化的队列 true为持久化
             * 参数 3 为是否排外的true表示排外，如果有一个消费连接了当前队列那么在断开连接之前不允许其他消费者连接这个队列
             * 参数 4 为是否自动删除的true为自动删除的，当队列中没有消息也没有消费者监听时会自动删除这个队列
             * 注意：
             *   可选代码，但是要在监听队列前确保队列已经存在
             */
            String queueName = "myQueue";
            channel.queueDeclare(queueName,true ,false ,false , null);
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
                    System.out.println(message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }/*finally {
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
        }*/
    }

}
