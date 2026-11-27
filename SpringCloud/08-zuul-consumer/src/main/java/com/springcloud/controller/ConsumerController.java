package com.springcloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 许彤
 * 2020/12/24
 */

/**
 * 服务消费者，用户访问远程服务。为用户返回相应数据
 */
@RestController
public class ConsumerController {
    //注入一个基于HttpClient访问的服务的对象，这个对象默认没有定义在Spring的应用上下文中
    //需要手动定义配置，具体参考RestTemplateConfig类
    @Resource
    RestTemplate restTemplate;

    @RequestMapping(value = "/test")
    public String test(){
        //通过注册中心访问远程服务，适用于内部服务之间的调用
        String url = "http://08-zuul-provider/test";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return "没有使用网关服务消费者----"+result.getBody();
    }

    @RequestMapping(value = "/test02")
    public String test02(){
        //经过网关访问某个具体服务，给某个第三方调用
        String url = "http://localhost:9000/myapi/api-zuul/test?appId=123";
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> result = rt.getForEntity(url, String.class);
        return "使用网关服务消费者----"+result.getBody();
    }

}
