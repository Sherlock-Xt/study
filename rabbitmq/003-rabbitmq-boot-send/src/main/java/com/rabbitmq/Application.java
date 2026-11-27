package com.rabbitmq;

import com.rabbitmq.service.SendService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.swing.plaf.metal.MetalIconFactory;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Resource
    SendService sendService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
            sendService.topicSend("Boot fanout Test Message");
    }
}
