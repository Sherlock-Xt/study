package com.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.springcloud.hystrix.MyHystrixCommand;
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
    @Resource
    RestTemplate restTemplate;

    /**
     *  @HystrixCommand 注解的作用，标记当前方法支持Hystrix的服务熔断，如果当前方法出现异常或执行超时都会触发
     *  服务的熔断立即为用户返回响应
     *  属性
     *    fallbackMethod 作用是用于指定一个服务的降级方法，如果当期控制器方法出发服务熔断后将使用这个方法的返回值来替代
     *    当前控制器的真实返回内容，取值为当前类某个方法名称
     */
    @HystrixCommand(fallbackMethod = "myError")
    @RequestMapping(value = "/test")
    public String test(){
        /**
         *
         */
        String url = "http://06-hystrix-provider/test";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return "服务消费者----"+result.getBody();
    }

    /**
     *  @HystrixCommand 注解的作用，标记当前方法支持Hystrix的服务熔断，如果当前方法出现异常或执行超时都会出发
     *  服务的熔断立即为用户返回响应
     *  属性
     *   commandProperties 设置熔断的属性 取值为 @HystrixProperty数组
     *     @HystrixProperty 用于设置熔断器的某个属性
     *       属性
     *          name 用于指定某个具体的属性名，execution.isolation.thread.timeoutInMilliseconds 设置熔断的默认执行超时时间
     *
     *          value 用于指定某个熔断器的具体属性值，例如 6000表示为超时时间为6秒，如果当前控制方法在6秒钟内执行完成，则正常结束
     *          否则将触发服务的熔断
     *  注意：
     *    Hystrix的超时熔断默认时间为1000毫秒
     */
    @HystrixCommand(fallbackMethod = "myError",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "4000")
    })
    @RequestMapping(value = "/time")
    public String testTimeOut(){
        /**
         *
         */
        String url = "http://06-hystrix-provider/time";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return "服务消费者----"+result.getBody();
    }

    /**
     *  @HystrixCommand 注解的作用，标记当前方法支持Hystrix的服务熔断，如果当前方法出现异常或执行超时都会出发
     *  服务的熔断立即为用户返回响应
     *  属性
     *    ignoreExceptions 异常的忽略，当我们配置了某几个需要忽略的异常信息以后，如果当前控制器方法抛出了这个异常
     *    那么Hystrix是不会触发服务熔断，取值为Throwable 以及任意子类的class数组
     *  注意：
     *    建议不要使用Exception.class 或 RuntimeTimeException.class
     */
    @HystrixCommand(fallbackMethod = "myError",ignoreExceptions = {NullPointerException.class})
    @RequestMapping(value = "/pass")
    public String passException(){
        /**
         *
         */
        String str = null;
        str.toString();
        String url = "http://06-hystrix-provider/time";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return "服务消费者----"+result.getBody();
    }

    @HystrixCommand(fallbackMethod = "myError")
    @RequestMapping(value = "/myHystrix")
    public String myHystrix(){
        /**
         *
         */
        String url = "http://06-hystrix-provider/test";
        /*ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);*/
        //自定义熔断器类
        MyHystrixCommand command = new MyHystrixCommand(restTemplate, url);
        //执行自定义熔断器类的execute方法，后台底层会自动调用对象中的run方法来访问远程服务
        String execute = command.execute();
        return "服务消费者----"+execute;
    }

    /**
     * 服务降级方法，如果某个标记了 @HystrixCommand注解的控制器方法如果触发了服务的熔断，那么将自动调用这个方法
     * 利用这个方法的返回值来进行服务的降级处理。（如果需要执行这个方法要求 @HystrixCommand注解必须指定fallbackMethod属性值为
     * 这个方法的名字）
     * 虽然这个服务降级方法返回的内容不是用户最终所期待的那个结果，但是至少好过返回错误或执行超时导致故障蔓延的好
     *
     * 参数 Throwable 为触发服务熔断时的异常类型
     * 注意：
     *   1、如果是远程服务出现了异常则返回org.springframework.web.client.HttpServerErrorException$InternalServerError
     *      表示服务器内部错误
     *   2、如果是执行超时则返回com.netflix.hystrix.exception.HystrixTimeoutException 异常类
     *      表示自行超时
     *   3、如果是控制器方法内部错误则返回一个具体异常类型例如 NullPointerException异常
     *   4、我们可以根据服务的具体的异常类型来判断具体是什么问题然后有真不性的返回不同的响应数据
     */
    public String myError(Throwable throwable){
        System.out.println(throwable.getClass());
        return "服务器熔断了！";
    }
}
