package com.xiaohucare.framework.oss.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Map;

/**
 * 文件存储服务接口
 * 
 * @author xiaohucare
 */
public interface IFileStorageService {
	 /**
     * 生成STS临时凭证，返回给前端直接上传到OSS
     */
    Object getStsToken();
    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @return 文件访问路径
     */
    String uploadFile(MultipartFile file);
    
    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @param fileName 自定义文件名
     * @return 文件访问路径
     */
    String uploadFile(MultipartFile file, String fileName);
    String uploadFile(MultipartFile file,String prefix, String fileName);
    
    /**
     * 上传文件
     * 
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param contentType 文件类型
     * @return 文件访问路径
     */
    String uploadFile(InputStream inputStream, String fileName, String contentType);
    
    /**
     * 删除文件
     * 
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    boolean deleteFile(String filePath);
    
    /**
     * 获取文件信息
     * 
     * @param filePath 文件路径
     * @return 文件信息
     */
    Map<String, Object> getFileInfo(String filePath);
    
    /**
     * 下载文件
     * 
     * @param filePath 文件路径
     * @return 文件输入流
     */
    InputStream downloadFile(String filePath);
    
    /**
     * 获取文件URL
     * 
     * @param filePath 文件路径
     * @return 文件URL
     */
    String getFileUrl(String filePath);
    
    /**
     * 获取文件URL（带过期时间）
     * 
     * @param filePath 文件路径
     * @param expireSeconds 过期时间（秒）
     * @return 文件URL
     */
    String getFileUrl(String filePath, int expireSeconds);
    
    /**
     * 获取存储类型
     * 
     * @return 存储类型
     */
    String getStorageType();
    
    String getPrefix();
}