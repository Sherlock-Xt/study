package com.springcloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 许彤
 * 2020/12/26
 */
@Component
public class ErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //返回error表示 异常过滤器，当网关工程中的某个过滤器出现异常后则自动执行这个过滤器
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        //设置响应的状态码，如果使用ZuulException设置响应状态码，第三方应用可能会收到错误信息
        //因此我们如果不希望他们看到错误信息可以设置200
        ZuulException exception = (ZuulException) context.getThrowable();
        response.setStatus(exception.nStatusCode);
        //设置响应类型
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter out = response.getWriter()){
            out.println("出错了："+exception.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
