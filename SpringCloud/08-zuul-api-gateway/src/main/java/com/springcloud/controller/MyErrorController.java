package com.springcloud.controller;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 许彤
 * 2020/12/26
 */
//自定义全局错误页面控制器并实现异常控制的的接口
    @RestController
public class MyErrorController implements ErrorController {
    //如果当前网关出现异常后将自动执行这个方法来获取异常的响应结果
    //方法的返回为返回错误结果的具体请求路径
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public String error(){
        ZuulException exception = (ZuulException) RequestContext.getCurrentContext().getThrowable();
        return "全局错误页面信息："+exception.getMessage();
    }

}
