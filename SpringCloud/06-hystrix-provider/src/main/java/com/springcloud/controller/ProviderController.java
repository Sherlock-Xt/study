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
        String str = null;
        str.toString();
        return "服务提供者";
    }

    @RequestMapping(value = "/time")
    public String time(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务提供者";
    }
}
