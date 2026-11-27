package com.masterslave.service;

import com.masterslave.datasource.MyThreadLocal;
import com.masterslave.mapper.StuMapper;
import com.masterslave.model.Stu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 许彤
 * 2021/1/4
 */
@Service
public class StuService {
    @Resource
    private StuMapper stuMapper;


    @Transactional
    public void addStu(Stu stu){
        //切换数据源，向threadLocal中设置数据名称，取值任意，但必须要与spring配置文件中的动态数据源bean的targetDataSource属性的map 的key相对应
        MyThreadLocal.setDataSource("master");
        stuMapper.insertSelective(stu);
        /*System.out.println(10/0);
        stuMapper.insertSelective(stu);*/

    }

    public void queryAll(){
        //切换数据源，向threadLocal中设置数据名称，取值任意，但必须要与spring配置文件中的动态数据源bean的targetDataSource属性的map 的key相对应
        MyThreadLocal.setDataSource("slave");
        List<Stu> stuList = stuMapper.queryAllStu();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }
}
