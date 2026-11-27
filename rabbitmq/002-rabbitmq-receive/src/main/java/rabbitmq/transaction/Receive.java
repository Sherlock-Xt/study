package rabbitmq.transaction;

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

            String queueName = "transactionQueue";

            channel.queueDeclare(queueName,true ,false ,false , null);

            String exchangeName = "directExchange";
            channel.exchangeDeclare(exchangeName, "direct",true);

            channel.queueBind(queueName, exchangeName, "transactionKey");

            /**
             * 事务对消息的接收暂时没有任何的影响
             */

            channel.txSelect();

            channel.basicConsume(queueName, true,new DefaultConsumer(channel){

                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println(message);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
