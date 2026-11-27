package com.springcloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 许彤
 * 2020/12/26
 */
/**
 * 自定义过滤器，并集成过滤器的抽象父类
 * 并将当前类定义到Spring的应用上下文容器中
 */
@Component
public class AuthFilter extends ZuulFilter {
    //返回过滤器的类型，不同的过滤器类型执行的时机不同
    public String filterType() {
        //返回pre 表示当前过滤器为前置过滤器，会在请求进入远程服务之前执行，通常用于实现权限验证（接口安全）
        return "pre";
    }

    //过滤器的序号，当有多个同类型的过滤器时返回值越小执行优先级越高
    public int filterOrder() {
        return 0;
    }

    //是否启用当前过滤器返回true表示启用返回false表示禁用
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的具体执行方法，我们会在这个方法中来实现过滤器的具体逻辑代码
     * （在这个实现接口安全的认证）
     * @return 目前版本没有特殊作用 返回null即可
     * @throws ZuulException
     */
    public Object run() throws ZuulException {
        System.out.println(10/0);
        //获取上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        //获取用户请求对象
        HttpServletRequest request = context.getRequest();
        //获取请求中的身份识别码：如果针对第三应用命名通常为appId或appKey，如果针对普通用户通常命名为token
        String appId = request.getParameter("appId");
        if (appId==null||!appId.equals("123")){
            //设置为false 表示请求不在继续执行立即为用户返回响应
            context.setSendZuulResponse(false);
            //设置响应状态 为401 表示权限不足
            context.setResponseStatusCode(401);
            //设置响应头
            context.addZuulResponseHeader("content-type", "text/html;charset=utf-8");
            //设置响应数据
            context.setResponseBody("非法的请求！");
            System.out.println("-----------------");
        }else {
            System.out.println("请求合法-----------");
        }
        return null;
    }
}
