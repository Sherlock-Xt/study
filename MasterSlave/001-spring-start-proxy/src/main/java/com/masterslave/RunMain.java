package com.masterslave;

import com.masterslave.model.Stu;
import com.masterslave.service.StuService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 许彤
 * 2021/1/4
 */
public class RunMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StuService stuService = (StuService) context.getBean("stuService");
        Stu stu = new Stu();
        stu.setName("liubei");
        stuService.addStu(stu);
        //stuService.queryAll();
    }
}
