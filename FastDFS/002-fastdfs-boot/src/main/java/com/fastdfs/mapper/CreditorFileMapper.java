package com.fastdfs.mapper;

import com.fastdfs.model.CreditorFile;

public interface CreditorFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditorFile record);

    int insertSelective(CreditorFile record);

    CreditorFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditorFile record);

    int updateByPrimaryKey(CreditorFile record);

}