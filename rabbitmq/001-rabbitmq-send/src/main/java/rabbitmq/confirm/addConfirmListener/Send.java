package rabbitmq.confirm.addConfirmListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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

            /**
             *  异步监听确认
             *    addConfirmListener 在底层启动一个子线程，这个子线成会自动持续的调用参数对象中的方法来完成消息的确认
             *   注意：
             *     1、由于底层会启动子线程因此，通道和连接是不能关闭的
             *     2、由于是异步监听因此建议在发送就启动监听器
             */
            channel.addConfirmListener(new ConfirmListener() {
                // 消息发送成功后的回调方法  参数 1 为消息的编号 参数 2为消息的是否批量发送成功
                public void handleAck(long l, boolean b) throws IOException {
                    System.out.println("消息发送成功,编号为："+l+",是否批量发送成功："+b);
                }
                //消息发送失败后的回调方法 参数 1 为消息的编号 参数 2为消息的是否批量发送失败
                public void handleNack(long l, boolean b) throws IOException {
                    System.out.println("消息发送失败,编号为："+l+",是否批量发送成功："+b);
                }
            });
            for (int i = 0; i < 100; i++) {

                String message = "Confirm Test Message"+i;
                channel.basicPublish(exchangeName, "confirmKey", null, message.getBytes());

            }

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
