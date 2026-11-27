package com.fastdfs.service.impl;

import com.fastdfs.mapper.CreditorFileMapper;
import com.fastdfs.mapper.CreditorInfoMapper;
import com.fastdfs.model.CreditorFile;
import com.fastdfs.model.CreditorInfo;
import com.fastdfs.service.CreditorService;
import com.fastdfs.utils.RunMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 许彤
 * 2020/12/22
 */
@Service
public class CreditorServiceImpl implements CreditorService {
    @Resource
    CreditorInfoMapper creditorInfoMapper;
    @Resource
    CreditorFileMapper creditorFileMapper;

    @Override
    public List<CreditorInfo> queryAllCreditorInfoList() {
        return creditorInfoMapper.queryAllCreditorInfoList();
    }

    @Override
    public int saveCreditorFile(CreditorFile creditorFile) {
        return creditorFileMapper.insertSelective(creditorFile);
    }

    @Override
    public CreditorFile queryCreditorFileById(Integer creditorFileId) {
        return creditorFileMapper.selectByPrimaryKey(creditorFileId);
    }

    @Override
    public void deleteCreditorFileById(Integer creditorFileId) throws Exception {
        CreditorFile creditorFile = creditorFileMapper.selectByPrimaryKey(creditorFileId);
        int delete = RunMain.delete(creditorFile.getGroupName(), creditorFile.getRemoteFilename());
        if (delete==0){
            int deleteFlag = creditorFileMapper.deleteByPrimaryKey(creditorFileId);
            if (deleteFlag<=0){
                throw new Exception();
            }
        }else {
          throw new Exception();
        }
    }
}
