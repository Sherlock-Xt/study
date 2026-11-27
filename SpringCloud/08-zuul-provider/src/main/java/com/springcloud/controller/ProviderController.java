package com.springcloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 许彤
 * 2020/12/24
 */
@RestController
public class ProviderController {

    @RequestMapping(value = "/test")
    public String test(){
        return "服务提供者";
    }

    @RequestMapping(value = "/test02")
    public String test02(){
        return "服务提供者02";
    }
}
