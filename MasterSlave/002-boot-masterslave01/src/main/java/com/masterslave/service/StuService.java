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
@Service
public class StuService {
    @Resource
    private MasterStuMapper masterStuMapper;
    @Resource
    private SlaveStuMapper slaveStuMapper;

    @Transactional(transactionManager = "masterTransactionManager")
    public void addStu(Stu stu){
        masterStuMapper.insertSelective(stu);
        //System.out.println(10/0);
        //masterStuMapper.insertSelective(stu);
    }

    public void queryAll(){
        List<Stu> stuList = slaveStuMapper.queryAllStu();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }

}
