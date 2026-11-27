package com.springcloud.service;

import com.springcloud.hystrix.Myfallback;
import com.springcloud.hystrix.MyfallbackFactory;
import com.springcloud.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 许彤
 * 2020/12/26
 */
/**
 *  @FeignClient 用于标记当前接口是SpringCloud中的Feign的声明式服务消费接口
 *  Spring会底层会为这个借口创建一个动态代理对象，并将这个对象定义到Spring的应用上下文容器中
 *  属性
 *    name和value 用于指定需要访问的具体的服务在注册中心的服务名
 *    fallback 用于指定当前声明式服务消费接口服务熔断处理类的class，这个类必须是当前接口的子类
 *    fallbackFactory 作用以及使用方法与fallback完全一致区别在于fallbackFactory可以获取到熔断时的异常信息
 */
//@FeignClient(name = "07-feign-provider",fallback = Myfallback.class)
@FeignClient(name = "07-feign-provider",fallbackFactory = MyfallbackFactory.class)
public interface ProviderService {
    /**
     * 声明式服务消费方法
     *  @RequestMapping("/test") 在这里的作用是用于通知Spring底层该使用什么样的方式
     *  访问哪个具体的请求 参数为需要访问的服务提供者的具体请求路径
     */
    @RequestMapping(value = "/test")
    String test();
    /**
     *  由于服务提供者返回的是一个符合Json对象格式的字符串数据，因此声明式服务消费方法
     *  可以使用String 来接收数据或Map集合或实体类或Object封装响应数据
     *  只要返回值不是String那么Spring底层都会将这个Json对象数据转换成对应的Java类型
     *  注意：
     *    如果使用了Object接收数据那么，封装后的具体类型是Map集合
     */
    @RequestMapping(value = "/getUser")
    User getUser();
    /**
     * 由于服务提供者返回的是一个符合Json对象数组格式的字符串数据，因此声明式服务消费方法
     * 可以使用String 来接收数据或List集合或Set集合封装响应数据
     * 只要返回值不是String那么Spring底层都会将这个Json对象数组数据转换成对应的Java类型
     * 注意：
     *   这里我们可以为List或Set集合指定泛型来封装每一个元素的数据，如果没有指定泛型类型
     *   那么默认使用Map集合来封装元素数据
     */
    @RequestMapping(value = "/getUserList")
    List<User> getUserList();

    /**
     *  @RequestParam("name") 用于标记这个方法的形参的数据将作为请求的参数传递（get方式）
     *  属性 name 用于标记这个请求参数的具体参数名
     */
    @RequestMapping(value = "/param01")
    User getParam01(@RequestParam("name") String name, @RequestParam("age") Integer age);
    /**
     *
     *这么做不好，消费者不建议这么做如果要传递对象数据，建议传递Json对象字符串
     */
    @RequestMapping(value = "/param02")
    User getParam02(User param);
}
