package com.fastdfs.controller;

import com.fastdfs.model.CreditorFile;
import com.fastdfs.model.CreditorInfo;
import com.fastdfs.service.CreditorService;
import com.fastdfs.utils.RunMain;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 许彤
 * 2020/12/22
 */
@Controller
public class CreditorController {
    @Resource
    CreditorService creditorService;

    @RequestMapping(value = "/")
    public String creditorList(Model model) {
        List<CreditorInfo> creditorInfoList = creditorService.queryAllCreditorInfoList();
        model.addAttribute("creditorInfoList", creditorInfoList);
        return "creditorList";
    }

    @RequestMapping(value = "/toUpload")
    public String toUpload(Model model, Integer creditorId) {
        model.addAttribute("creditorId", creditorId);
        return "upload";
    }

    /**
     * @param creditorId 要上传的ciId
     * @param uploadFile spring提供的专门用于封装某个文件数据的对象
     */
    @RequestMapping(value = "/upload")
    public String upload(Integer creditorId, MultipartFile uploadFile, Model model) throws IOException {
        /*System.out.println(uploadFile.getBytes());//获取文件所对应的字节数组
        System.out.println(uploadFile.getContentType());//获取文件类型
        System.out.println(uploadFile.getInputStream());//获取文件的输入流
        System.out.println(uploadFile.getName());//获取这个文件域这个表单元素的name属性值
        System.out.println(uploadFile.getOriginalFilename());//获取上传的文件的文件名
        System.out.println(uploadFile.getSize());//获取文件的大小
        System.out.println(uploadFile.isEmpty());//判断文件是否为空，如果没有上传文件或者大小为0则返回true表示空文件*/
        byte[] bytes = uploadFile.getBytes();
        String fileName = uploadFile.getOriginalFilename();
        int index = -1;
        if (fileName != null) {
            index = fileName.lastIndexOf(".");
        }

        String fileExtName = "";
        if (index >= 0) {
            fileExtName = fileName.substring(index + 1);
        }

        try {
            String[] upload = RunMain.upload(bytes, fileExtName);
            CreditorFile creditorFile = new CreditorFile();
            if (upload != null) {
                creditorFile.setGroupName(upload[0]);
                creditorFile.setRemoteFilename(upload[1]);
            }
            creditorFile.setCiId(creditorId);
            creditorFile.setFileSize(uploadFile.getSize());
            creditorFile.setFileType(uploadFile.getContentType());
            creditorFile.setOldFilename(fileName);
            int flag = creditorService.saveCreditorFile(creditorFile);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "上传失败");
            return "result";
        }
    }

    /**
     * 下载文件
     *
     * @param creditorFileId creditorFile的主键Id
     * @return 返回值对象为ResponseEntity，这个对象表示响应体，是springWeb的响应封装对象
     * 注意：
     * 这个返回值对象中的泛型将决定本次响应时的具体数据类型,byte[]表示本次返回的数据类型是文件流类型
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download(Integer creditorFileId) {
        CreditorFile creditorFile = creditorService.queryCreditorFileById(creditorFileId);
        byte[] download = RunMain.download(creditorFile.getGroupName(), creditorFile.getRemoteFilename());
        //创建一个http头对象
        HttpHeaders httpHeaders = new HttpHeaders();
        //设置http头的contentType为流类型，这样浏览器就可以直接把流转换为文件让用户下载
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //设置http头的文件大小，用于浏览器显示下载的进度条
        httpHeaders.setContentLength(creditorFile.getFileSize());
        httpHeaders.setContentDispositionFormData("attachment", creditorFile.getOldFilename());
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(download, httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * @param creditorFileId 要删除的fileId
     */
    @RequestMapping(value = "/deleteFile")
    public String delete(Integer creditorFileId, Model model) throws IOException {
        CreditorFile creditorFile = creditorService.queryCreditorFileById(creditorFileId);
        try {
            creditorService.deleteCreditorFileById(creditorFileId);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "上传失败");
            return "result";
        }
    }

}
