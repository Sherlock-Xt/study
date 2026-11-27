package com.springcloud.controller;

import com.springcloud.model.User;
import com.springcloud.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 许彤
 * 2020/12/24
 */

/**
 * 服务消费者，用户访问远程服务。为用户返回相应数据
 */
@RestController
public class ConsumerController {
    @Resource
    ProviderService providerService;

    @RequestMapping(value = "/test")
    public String test(){
        String result = providerService.test();
        return "服务消费者----"+result;
    }

    @RequestMapping(value = "/getUser")
    public String getUser(){
        User user = providerService.getUser();
        return "服务消费者----"+user;
    }

    @RequestMapping(value = "/getUserList")
    public String getUserList(){
        List<User> userList = providerService.getUserList();
        return "服务消费者----"+userList;
    }

    @RequestMapping(value = "/param01")
    public String param01(){
        User user = providerService.getParam01("wangwu",18);
        return "服务消费者----"+user;
    }

    @RequestMapping(value = "/param02")
    public String param02(){
        User paramUser = new User();
        paramUser.setName("zhaoliu");
        paramUser.setAge(19);
        User user = providerService.getParam02(paramUser);
        return "服务消费者----"+user;
    }
}
