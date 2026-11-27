package com.springcloud.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

/**
 * 许彤
 * 2020/12/25
 */
/**
 *  自定义异常熔断器类，并集成熔断器的抽象父类
 *   父类的泛型用于决定当前触发熔断后的服务降级返回的具体数据内容
 */
public class MyHystrixCommand extends HystrixCommand<String> {
    private RestTemplate restTemplate;
    private String url;


    public MyHystrixCommand(RestTemplate restTemplate,String url) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")));
        this.restTemplate = restTemplate;
        this.url = url;
    }

    /**
     * run方法来自抽象父类中的抽象方法，这个方法不能手动调用
     * 这个方法的主要作用是用于执行远程服务并返回远程服务的响应结果
     * 这个方法有Spring自动调用，如果这个方法抛出异常则自动触发服务的熔断  ，自动调用   getFallback方法来进行服务的降级
     */
    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 服务降级方法，这个方法不能手动调用的，如果run方法抛出异常则自动调用这个方法利用这个方法的返回值
     * 来替代远程服务的真实返回内容
     */
    @Override
    protected String getFallback() {
        return "远程服务出发了熔断";
    }
}
