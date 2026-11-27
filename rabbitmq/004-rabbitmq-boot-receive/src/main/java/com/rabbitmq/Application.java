package com.rabbitmq;

import com.rabbitmq.service.ReceiveService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Resource
    private ReceiveService receiveService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //注意：如果开始了监听器接收，那么就不能也不需要使用普通的接收方法来接收消息
        //receiveService.directReceive();
    }
}
