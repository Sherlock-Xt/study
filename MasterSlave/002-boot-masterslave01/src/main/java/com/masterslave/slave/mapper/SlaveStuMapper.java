package com.masterslave.slave.mapper;

import com.masterslave.model.Stu;

import java.util.List;

public interface SlaveStuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Stu record);

    int insertSelective(Stu record);

    Stu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stu record);

    int updateByPrimaryKey(Stu record);

    List<Stu> queryAllStu();

}