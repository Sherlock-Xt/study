package com.springcloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 许彤
 * 2020/12/24
 */

/**
 * 由于是服务的提供者，那么要求控制方法返回Json数据因此使用@RestController
 */
@RestController
public class ProviderController {

    @RequestMapping(value = "/test")
    public String test(){
        return "spring cloud 第一个服务提供者";
    }
}
