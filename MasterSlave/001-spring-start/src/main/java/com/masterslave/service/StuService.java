package com.masterslave.service;

import com.masterslave.master.mapper.MasterStuMapper;
import com.masterslave.model.Stu;
import com.masterslave.slave.mapper.SlaveStuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 许彤
 * 2021/1/4
 */
/**
 * 多数据源的问题
 *  1、重复代码和配置太多了，例如重复的Mapper包以及重复数据源连接配置
 *  2、不能自动分离，需要手动选择操作的Mapper或通过数据源名字动态切换，这样就非常容易调用错误的数据源
 *  3、不能故障转移，如果某个从库或主库宕机后不能将请求转发给其它的数据源
 *  4、不能负载均衡读数据
 *  解决方案：
 *    1、 我们可以自己写一套数据源的管理类，根据不同的SQL语句类型返回不用的数据源，并配合心跳机制判断服务器
 *    是否宕机，同时还需要记录调用次数提供负载均衡
 */
@Service
public class StuService {
    @Resource
    private MasterStuMapper masterStuMapper;
    @Resource
    private SlaveStuMapper slaveStuMapper;

    @Transactional(transactionManager = "masterTransactionManager")
    public void addStu(Stu stu){
        masterStuMapper.insertSelective(stu);
    }

    public void queryAll(){
        List<Stu> stuList = slaveStuMapper.queryAllStu();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }

}
