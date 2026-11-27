package com.consumer.controller;

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
        /**
         * getForEntity 是使用Http协议以Get方式提交请求访问某个服务对应服务的@RequestMapping或@GetMapping
         * 参数 1 为需要访问的服务的具体请求地址路径
         * 参数 2 为本次请求的响应数据的具体封装类型，如果服务提供者返回的数据是基本数据格式（字符串或数字等），那么我们可以使用
         *        String.class或对应包装类.class来指定参数2完成数据封装
         *        如果服务提供者返回的是一个符合Json对象格式的字符串数据那么我们可以使用String.class或实体类.class或Map.class
         *        来封装数据，这里其实是一个将Json对象格式数据的字符串转换成对应Java对象的过程
         *        如果服务提供者返回的是一个符合Json对象数组格式的字符串数据那么我们可以使用String.class或List.class或Set.class
         *        来封装响应数据，这里其实是一个将Json对象数组转换成Java集合的过程
         * 返回值：返回一个ResponseEntity的对象，这个对象封装者本次响应的所有信息，包括响应状态码、响应的头文件信息，、
         *        以及响应体（具体响应数据），可以为这个对象指定泛型，泛型类型取决于getForEntity方法的参数2
         */
        String url = "http://localhost:8081/test";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        System.out.println(result.getStatusCode());//获取响应状态码以及状态信息
        System.out.println(result.getStatusCodeValue());//获取响应状态码
        System.out.println(result.getHeaders());//获取响应头
        System.out.println(result.getBody());//获取响应体(其实就是响应数据)
        return "第一个spring cloud 服务消费者----"+result.getBody();
    }

}
