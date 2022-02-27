package com.example.server.utils;

import com.example.server.pojo.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtils {
    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    //ClientGlobal.init方法会读取配置文件，并初始化对应的属性
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            logger.error("初始化DFS失败", e.getMessage());
        }
        logger.info("文件初始化成功！");
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file) {
        String name = file.getOriginalFilename();

        logger.info("文件名:", name);
        StorageClient storageClient = null;
        String[] uploadResult = null;

        try {
            //获取storage客户端
            storageClient = getStorageClient();
            //上传
            uploadResult = storageClient.upload_file
                    (file.getBytes(), name.substring(name.lastIndexOf(".") + 1), null);
        } catch (Exception e) {
            logger.error("上传文件失败", e.getMessage());
        }
        if (null == uploadResult) {
            logger.error("上传失败",storageClient.getErrorCode());
        }
        return uploadResult;
    }

    /**
     * 查看文件信息
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {

        StorageClient storageClient = null;

        try {
            storageClient = getStorageClient();
            FileInfo file_info = storageClient.get_file_info(groupName, remoteFileName);
            return file_info;
        } catch (Exception e) {
            logger.error("查看文件信息失败", e.getMessage());
        }
        return null;
    }


    /**
     * 下载文件
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) {
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            logger.error("下载失败：", e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            int i = storageClient.delete_file(groupName, remoteFileName);
            logger.info("删除成功" + i);
        } catch (Exception e) {
            logger.error("删除失败：", e);
        }
    }



    /**
     * 获取文件路径
     *展示文件需要完整的url路径
     * @return
     * @throws IOException
     */
    public static String getTrackerUrl() {
        TrackerClient trackerClient = new TrackerClient();

        StorageServer storeStorage = null;
        TrackerServer trackerServer = null;

        try {
            trackerServer = trackerClient.getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("获取文件路径失败：",e.getMessage());
        }

        return "http://" + storeStorage.getInetSocketAddress().getHostString() + ":8888/";
    }



    /**
     * 生成storage客户端
     *
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        return new StorageClient(trackerServer, null);
    }

    /**
     * 生成tracker服务器端
     *
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getTrackerServer();
    }
}
