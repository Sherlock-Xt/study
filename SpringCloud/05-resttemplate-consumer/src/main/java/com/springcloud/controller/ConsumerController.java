package com.springcloud.controller;

import com.springcloud.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/test")
    public String test(){
        /**
         * 由于服务提供者返回的数据是一个符合Json对象格式的字符串数据，
         * 因此消费者指定getForEntity方法的参数2 为String.class或实体类.class或Map.class
         * 来封装响应的数据
         * 注意：
         *    Spring底层其实是将一个Json转换成Java的对象
         */
        String url = "http://localhost:8081/getObject";
        ResponseEntity<User> result = restTemplate.getForEntity(url, User.class);
        System.out.println(result.getStatusCode());//获取响应状态码以及状态信息
        System.out.println(result.getStatusCodeValue());//获取响应状态码
        System.out.println(result.getHeaders());//获取响应头
        System.out.println(result.getBody());//获取响应体(其实就是响应数据)
        return "服务消费者----"+result.getBody();
    }

    @RequestMapping(value = "/testList")
    public String testList(){
        /**
         *  由于服务提供者返回的数据是，一个符合Json对象数组格式的数据，因此消费者可以使用
         *  String.class或List.class或Set.class来封装响应数据.
         *  注意：
         *    Spring底层其实是将一个Json转换成Java的对象，但是由于我们不能为List集合指定泛型因此
         *    获取的List集合中的元素就不能是User类型，默认的元素类型是LinkedHashMap集合类型
         */
        String url = "http://localhost:8081/getList";
        ResponseEntity<List> result = restTemplate.getForEntity(url, List.class);
        if (result.getBody()!=null){
            for (Object o : result.getBody()) {
                System.out.println(o.getClass());
            }
        }
        return "服务消费者----"+result.getBody();
    }


    @RequestMapping(value = "/testParam")
    public String testParam(){
        /**
         *
         *  请求地址路径中的{0}和{1}为请求地址路径的占位符，需要后期动态赋值
         *  getForEntity 方法的参数3位url动态数据，主要用于为地址路径中的占位符进行赋值
         *  取值类型为Object的可变长度参数，我们可以传递0个或多个Object类型的数据或一个Object类型的数组
         *  其中地址路径中的{0}和{1}中的0和1需要与参数3这个数组中的索引完全对应
         *  注意：
         *    1、参数数组中的数据含义以及类型必须要与地址路径的占位符的含义完全一一对应
         *    2、地址路径占位符可以出现在地址路径的任何一个位置不一定永远是在参数中例如
         *      http://{0}:{1}/{2}?{3}={4}&{5}={6} ,但是千万不要怎么做
         */

        String url = "http://localhost:8081/getParam?name={0}&age={1}";
        Object [] params = {"zhaoliu",20};
        ResponseEntity<User> result = restTemplate.getForEntity(url, User.class,params);
        return "服务消费者----"+result.getBody();
    }

    @RequestMapping(value = "/testMap")
    public String testMap(){
        /**
         *
         *  请求地址路径中的{name}和{age}为请求地址路径的占位符，需要后期动态赋值
         *  getForEntity 方法的参数3位url动态数据，主要用于为地址路径中的占位符进行赋值
         *  取值类型为Map集合，
         *  其中地址路径中的{name}和{age}中的name和age需要与参数3这个Map集合的key完全对应
         *
         */

        String url = "http://localhost:8081/getParam?name={name}&age={age}";
        Map<String,Object> params = new HashMap<>();
        params.put("name", "zhangsan");
        params.put("age", 15);
        ResponseEntity<User> result = restTemplate.getForEntity(url, User.class,params);
        return "服务消费者----"+result.getBody();
    }

    @RequestMapping(value = "/getForObject")
    public String getForObject(){
        /**
         *  getForObject作用以及使用方法与getForEntity完全相同
         *  区别在于返回值getForObject 只能返回响应的具体数据不能获取响应头以及响应状态码
         *  getForObject的具体返回类型取决于getForObject方法的参数2
         *  注意：
         *    如果在不需要获取响应头或响应状态码时尽可能使用getForObject方法
         */
        String url = "http://localhost:8081/getParam?name={name}&age={age}";
        Map<String,Object> params = new HashMap<>();
        params.put("name", "zhangfei");
        params.put("age", 17);
        User result = restTemplate.getForObject(url, User.class, params);
        return "服务消费者----"+result;
    }

    @RequestMapping(value = "/postTest")
    public String postTest(){
        /**
         *  postForObject 使用Http协议使用 Post方式提交请求访问服务的提供者，对应服务提供者的@PostMapping或@RequestMapping
         *
         *  参数 1 为请求地址路径
         *  参数 2 为本次请求的参数封装体
         *  参数 3 为本次响应的数据封装类型
         *  参数 4 为地址路径的动态参数取值为 Object类型的可变长度参数或Map集合
         *  注意：
         *    参数2取值为Object类型但是不能随意传递参数，只能传递Map集合，但是Map集合的value泛型必须是List类型
         *    LinkedMultiValueMap 类为Map集合的子类它是Spring提供的一个类它的value泛型就是List
         *
         */

        String url = "http://localhost:8081/postTest";
        LinkedMultiValueMap params = new LinkedMultiValueMap();
        /**
         * 设置参数数据，如果name在LinkedMultiValueMap集合中存在，那么会将陈七设置到name所对应的value（List集合）中的尾部
         * 如果name在LinkedMultiValueMap集合中不存在则创建一个List然后将陈七设置到这个List中在将List设置LinkedMultiValueMap
         * 集合中key为name
         */
        params.add("name", "liuei");
        params.add("age", 18);
        User result = restTemplate.postForObject(url, params, User.class);
        return "服务消费者----"+result;
    }

    @RequestMapping(value = "/putTest")
    public String putTest(){
        String url = "http://localhost:8081/putTest";
        LinkedMultiValueMap params = new LinkedMultiValueMap();
        /**
         *  put 相关方法 使用Http协议 以Put方式提交请求访问服务提供者 对应服务提供者的@PutMapping、
         *  参数 1 为请求地址路径
         *  参数 2 为请求参数封装体
         *  参数 3 为地址路径动态数据
         * 注意：
         *   put相关方法主要针对数据的修改，它没有返回值因此我们不知道远程服务是否正确的完成了数据的修改
         *   因此我们不建议使用Put相关方法，除非远程服务明确使用@PutMapping
         *
         */
        params.add("name", "liuei");
        params.add("age", 18);
        restTemplate.put(url, params);
        return "服务消费者----";
    }

    @RequestMapping(value = "/deleteTest")
    public String deleteTest(){
        String url = "http://localhost:8081/deleteTest?id={0}";
        /**
         *  delete 相关方法 使用Http协议 以Delete方式提交请求访问服务提供者 对应服务提供者的@DeleteMapping
         *  参数 1 为请求地址路径
         *  参数 2 为地址路径动态数据
         * 注意：
         *   delete相关方法主要针对数据的删除，它没有返回值因此我们不知道远程服务是否正确的完成了数据的删除
         *   因此我们不建议使用Delete相关方法，除非远程服务明确使用@DeleteMapping
         *
         */
        restTemplate.delete(url, 1);
        return "服务消费者----";
    }
}
