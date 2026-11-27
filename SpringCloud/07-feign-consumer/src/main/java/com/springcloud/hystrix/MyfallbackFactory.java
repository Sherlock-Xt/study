package com.springcloud.hystrix;

/**
 * 许彤
 * 2020/12/26
 */

import com.springcloud.model.User;
import com.springcloud.service.ProviderService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义异常熔断器工厂类,并实现异常熔断工厂接口，
 * 注意：
 *   父接口中的泛型用于决定为哪个生命式服务消费接口提供异常熔断
 */
@Component
public class MyfallbackFactory implements FallbackFactory<ProviderService> {
    //用于创建或返回熔断器类,可以利用参数throwable来获取异常的信息
    @Override
    public ProviderService create(Throwable throwable) {
        return new ProviderService() {
            @Override
            public String test() {
                return "test服务熔断了，异常信息为："+throwable.getMessage()+",异常类型为："+throwable.getClass();
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
        };
    }
}
