package com.fastdfs.mapper;

import com.fastdfs.model.CreditorInfo;

import java.util.List;

public interface CreditorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditorInfo record);

    int insertSelective(CreditorInfo record);

    CreditorInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditorInfo record);

    int updateByPrimaryKey(CreditorInfo record);

    List<CreditorInfo> queryAllCreditorInfoList();

}