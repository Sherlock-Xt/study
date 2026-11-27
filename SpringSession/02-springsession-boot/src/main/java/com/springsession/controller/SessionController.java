package com.springsession.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 许彤
 * 2020/12/21
 */
@Controller
public class SessionController {

    @RequestMapping(value = "/set")
    @ResponseBody
    public Object set(HttpSession session){
        session.setAttribute("key", "boot session 数据");
        return "boot session 设置成功";
    }


    @RequestMapping(value = "/get")
    @ResponseBody
    public Object get(HttpSession session){
        Object key = session.getAttribute("key");
        return key;
    }
}
