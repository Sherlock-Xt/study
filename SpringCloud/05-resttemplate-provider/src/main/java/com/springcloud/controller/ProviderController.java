package com.springcloud.controller;

import com.springcloud.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 许彤
 * 2020/12/25
 */
@RestController
public class ProviderController {

    @RequestMapping(value = "getObject")
    public Object getObject(){
        User user = new User();
        user.setName("zhangsan");
        user.setAge(18);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return user;
    }

    @RequestMapping(value = "getList")
    public Object getList(){
        User user = new User();
        user.setName("zhangsan");
        user.setAge(18);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        user = new User();
        user.setName("lisi");
        user.setAge(17);
        userList.add(user);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return userList;
    }

    @RequestMapping(value = "getParam")
    public Object getParams(String name,Integer age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return user;
    }

    @PostMapping(value = "postTest")
    public Object postTest(String name,Integer age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return user;
    }

    @PutMapping(value = "putTest")
    public Object putTest(String name,Integer age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return user;
    }

    @DeleteMapping(value = "deleteTest")
    public Object deleteTest(String name,Integer age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        /**
         * 返回响应数据，spring会将user转换为对应json对象格式的字符串返还给消费者
         */
        return user;
    }
}
