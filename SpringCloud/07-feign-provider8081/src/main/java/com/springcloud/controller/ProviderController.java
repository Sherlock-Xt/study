package com.springcloud.controller;

import com.springcloud.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 许彤
 * 2020/12/24
 */
@RestController
public class ProviderController {

    @RequestMapping(value = "/test")
    public String test(){
        //System.out.println(10/0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务提供者8081";
    }

    @RequestMapping(value = "/getUser")
    public User getUser(){
        User user = new User("zhangsan",18);
        return user;
    }

    @RequestMapping(value = "/getUserList")
    public List<User> getUserList(){
        List<User> userList = new ArrayList<>();
        User user = new User("zhangsan",18);
        userList.add(user);
        user = new User("lisi",19);
        userList.add(user);
        return userList;
    }

    @RequestMapping(value = "/param01")
    public User param01(String name,Integer age){
        User user = new User(name,age);
        return user;
    }

    /**
     * 由于消费者传递过来的数据是一个User对象，那么Spring会将这个对象封装到请求体中并标记为@RequestBody
     * 因此服务提供者在接收数据时必须使用@RequestBody 来标记一个实体类或Map集合来封装请求中的数据
     * 注意：
     *   这么做不好，消费者不建议这么做如果要传递对象数据，建议传递Json对象字符串
     * @param user
     * @return
     */
    @RequestMapping(value = "/param02")
    public User param02(@RequestBody User user){

        return user;
    }

}
