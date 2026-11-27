package com.springcloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 许彤
 * 2020/12/26
 */
@RestController
public class TestController {

    @RequestMapping(value = "/api/local")
    public String test(){
        return "网关工程中的控制器方法";
    }
}
