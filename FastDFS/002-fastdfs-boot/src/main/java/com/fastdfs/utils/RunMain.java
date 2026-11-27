package com.fastdfs.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * 许彤
 * 2020/12/22
 */
public class RunMain {


    /**
     * 上传文件
     */
    public static String [] upload(byte [] fileBuff,String fileExtName){
        //加载配置文件用于获取所有TrackerServer地址
        StorageClient sc = null;
        try {
            ClientGlobal.init("fastdfs.conf");
            TrackerClient tc = new TrackerClient();
            TrackerServer ts = tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            //创建客户端对象，利用这个对象完成对FastDFS的操作，例如文件的上传和下载和删除
            sc = new StorageClient(ts,ss);
            /**
             * 上传文件返回一个String数组，数组第一个元素为组名，第二个为远程文件名
             *
             */
            String[] strs = sc.upload_appender_file(fileBuff, fileExtName, null);
            return strs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (sc!=null){
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 下载文件
     */
    public static byte [] download(String groupName,String remoteFilename){
        //加载配置文件用于获取所有TrackerServer地址
        StorageClient sc = null;
        try {
            ClientGlobal.init("fastdfs.conf");
            TrackerClient tc = new TrackerClient();
            TrackerServer ts = tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            //创建客户端对象，利用这个对象完成对FastDFS的操作，例如文件的上传和下载和删除
            sc = new StorageClient(ts,ss);
            /**
             * 下载文件，
             * 第一个参数是需要下载文件的组名，
             * 第二个是需要下载文件的文件名
             * 返回值是字节数组类型
             */
            /*String groupName = "group1";
            String remoteFilename = "M00/00/00/wKiYgF_hqz2EJ6MpAAAAAMkSByo394.jpg";*/
            /*String localFilename = "F:/download.jpg";
            int flag = sc.download_file(groupName, remoteFilename, localFilename);*/

            byte[] bytes = sc.download_file(groupName, remoteFilename);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (sc!=null){
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 删除文件
     */
    public static int delete(String groupName,String remoteFilename){
        //加载配置文件用于获取所有TrackerServer地址
        StorageClient sc = null;
        try {
            ClientGlobal.init("fastdfs.conf");
            TrackerClient tc = new TrackerClient();
            TrackerServer ts = tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            //创建客户端对象，利用这个对象完成对FastDFS的操作，例如文件的上传和下载和删除
            sc = new StorageClient(ts,ss);
            /**
             * 删除文件，
             * 第一个参数是需要删除文件的组名，
             * 第二个是需要删除文件的文件名
             * 返回值是int，0为删除成功，其他的为文件删除失败，失败的原因为
             * 文件不存在
             */
            /*String groupName = "group1";
            String remoteFilename = "M00/00/00/wKiYgF_hqz2EJ6MpAAAAAMkSByo394.jpg";
*/
            int flag = sc.delete_file(groupName, remoteFilename);
            return flag;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (sc!=null){
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}
