package com.masterslave;

import com.masterslave.model.Stu;
import com.masterslave.service.StuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Resource
    private StuService stuService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Stu stu = new Stu();
        stu.setName("guanyu");
        stuService.addStu(stu);
        stuService.queryAll();
    }
}
