package com.fastdfs.service;

import com.fastdfs.model.CreditorFile;
import com.fastdfs.model.CreditorInfo;

import java.util.List;

/**
 * 许彤
 * 2020/12/22
 */
public interface CreditorService {
    List<CreditorInfo> queryAllCreditorInfoList();

    int saveCreditorFile(CreditorFile creditorFile);

    CreditorFile queryCreditorFileById(Integer creditorFileId);

    void deleteCreditorFileById(Integer creditorFileId) throws Exception;
}
