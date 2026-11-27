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
        /**
         *  参数 1 为本次请求的具体请求路径 其中02-EUREKA-CLIENT-PROVIDER 为需要访问的服务在注册中心中的具体服务名
         *         这个服务名不区分大小写
         *  注意：
         *    由于每个服务在注册中心可能是集群部署的所以可能会有多个服务IP地址
         *    这时如果我们需要通过注册中心来访问服务（发现服务），那么必须要求我们
         *    RestTemplate对象支持负载均衡，负载均衡的具体配置参见RestTemplateConfig类
         */
        String url = "http://03-eureka-cluster-provider/test";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        System.out.println(result.getStatusCode());//获取响应状态码以及状态信息
        System.out.println(result.getStatusCodeValue());//获取响应状态码
        System.out.println(result.getHeaders());//获取响应头
        System.out.println(result.getBody());//获取响应体(其实就是响应数据)
        return "第一个spring cloud 服务消费者----"+result.getBody();
    }

}
