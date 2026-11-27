package fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import javax.print.attribute.standard.RequestingUserName;
import java.io.IOException;

/**
 * 许彤
 * 2020/12/22
 */
public class RunMain {
    public static void main(String[] args) {
        delete();
    }

    /**
     * 上传文件
     */
    public static void upload(){
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
            String[] strs = sc.upload_appender_file("f:/test.jpg", "jpg", null);
            for (String str : strs) {
                System.out.println(str);
            }
            sc.close();

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
    }

    /**
     * 下载文件
     */
    public static void download(){
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
             * 第三个是本地磁盘路径，用于决定保存在磁盘上的哪个文件中
             * 返回值是int，0为下载成功，其他的为文件下载失败，失败的原因为
             * 文件不存在
             */
            String groupName = "group1";
            String remoteFilename = "M00/00/00/wKiYgF_hqz2EJ6MpAAAAAMkSByo394.jpg";
            String localFilename = "F:/download.jpg";
            int flag = sc.download_file(groupName, remoteFilename, localFilename);
            if (flag==0){
                System.out.println("下载成功");
            }
            sc.close();

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
    }

    /**
     * 删除文件
     */
    public static void delete(){
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
            String groupName = "group1";
            String remoteFilename = "M00/00/00/wKiYgF_hqz2EJ6MpAAAAAMkSByo394.jpg";

            int flag = sc.delete_file(groupName, remoteFilename);
            if (flag==0){
                System.out.println("success");
            }
            sc.close();

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
    }
}
