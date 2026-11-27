package com.springcloud.hystrix;

import com.springcloud.model.User;
import com.springcloud.service.ProviderService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 许彤
 * 2020/12/26
 */
/**
 * 自定义服务熔断器类，并实现某个声明式服务消费接口
 * 用于为这个接口提供服务熔断
 * 注意：
 *   这个类必须定义到Spring的应用上下文中
 *   当声明式服务消费接口中的某个方法出现异常后Spring会自动调用这个类中的对应方法来进行降级处理
 */
@Component
public class Myfallback implements ProviderService {

    @Override
    public String test() {
        return "test服务出现异常，熔断服务";
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public User getParam01(String name, Integer age) {
        return null;
    }

    @Override
    public User getParam02(User param) {
        return null;
    }
}
