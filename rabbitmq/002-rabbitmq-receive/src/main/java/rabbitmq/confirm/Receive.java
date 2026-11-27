package rabbitmq.confirm;

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

        Connection connection = null;

        Channel channel = null;

        try {

            connection = factory.newConnection();

            channel = connection.createChannel();

            String queueName = "confirmQueue";

            channel.queueDeclare(queueName,true ,false ,false , null);

            String exchangeName = "confirmExchange";
            channel.exchangeDeclare(exchangeName, "direct",true);

            channel.queueBind(queueName, exchangeName, "confirmKey");

            /**
             * 参数 2 为消息是否自动确认，true表示自动确认，false表示手动确认
             *   自动确认只要消息成功接收无论是否成功完成处理都会将消息直接队列中移除，这样容易丢失消息
             *   如果是手动确认 那么就必须要在消息处理完成后将消息从队列中移除，这样可能会造成消息的重复处理
             *   例如消息已经处理完成（成功写入数据库）但是没有及时移除就会造成消息重复消费，因此要做好消息防重复处理
             */
            //开启事务，如果启动了事务那么消息手动确认的方法所对应的命令不会提交到MQ中，需要等待事务的提交。
            //这是手动消息确认时消息自从队列中移除的实际以事务提交为准
            //channel.txSelect();
            channel.basicConsume(queueName, false,new DefaultConsumer(channel){

                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    /*String str = null;
                    str.toString();*/
                    String message = new String(body);
                    System.out.println(message);
                    /**
                     * 手动确认消息，确认后将消息从队列中移除
                     * 参数 1 为需要确认的消息编号
                     * 参数 2 为是否批量确认，true为同时确认小于等于当前编号的所有消息
                     */

                    Channel channelreceive = this.getChannel();
                    long deliveryTag = envelope.getDeliveryTag();
                    channelreceive.basicAck(deliveryTag, false);
                    //channelreceive.txCommit();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
