package com.masterslave.service;


import com.masterslave.master.mapper.StuMapper;
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
        stuMapper.insertSelective(stu);


    }

    public void queryAll(){
        List<Stu> stuList = stuMapper.queryAllStu();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }
}
