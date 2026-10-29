package com.xiaohucare.framework.oss.service;

import org.springframework.web.multipart.MultipartFile;

import com.xiaohucare.framework.oss.constant.OSSConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;

/**
 * 文件存储服务抽象实现
 * 
 * @author xiaohucare
 */
public abstract class AbstractFileStorageService implements IFileStorageService {
    
    /**
     * 生成文件名称
     * 
     * @param originalFilename 原始文件名
     * @return 生成的文件名
     */
    protected String generateFileName(String originalFilename) {
        if (StrUtil.isEmpty(originalFilename)) {
            throw new RuntimeException("文件名不能为空");
        }
        
        // 获取文件扩展名
        String extension = getFileExtension(originalFilename);
        
        // 生成随机文件名
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        
        // 返回格式：时间戳_随机UUID.扩展名
        return System.currentTimeMillis() + "_" + uuid + (StrUtil.isNotEmpty(extension) ? "." + extension : "");
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 文件扩展名
     */
    protected String getFileExtension(String filename) {
        if (StrUtil.isEmpty(filename)) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * 构建完整的文件路径
     * 
     * @param prefix 前缀
     * @param filename 文件名
     * @return 完整路径
     */
    protected String buildFilePath(String prefix, String filename) {
        if (StrUtil.isEmpty(prefix)) {
            return filename;
        }
        return prefix.endsWith("/") ? prefix + filename : prefix + "/" + filename;
    }
    
    /**
     * 验证文件大小
     * 
     * @param file 文件
     */
    protected void validateFileSize(MultipartFile file) {
        if (file.getSize() > OSSConstants.DEFAULT_MAX_SIZE) {
            throw new RuntimeException("文件大小不能超过50MB");
        }
    }
    
    /**
     * 验证文件大小
     * 
     * @param inputStream 输入流
     */
    protected void validateFileSize(InputStream inputStream) throws IOException {
        // 注意：此方法会消费输入流，实际使用时可能需要考虑重置流
        long size = inputStream.available();
        if (size > OSSConstants.DEFAULT_MAX_SIZE) {
            throw new RuntimeException("文件大小不能超过50MB");
        }
    }
    
    /**
     * 标准化文件路径
     * 
     * @param filePath 文件路径
     * @return 标准化后的路径
     */
    protected String normalizeFilePath(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return "";
        }
        // 替换反斜杠为正斜杠
        return filePath.replace("\\", "/");
    }
    
    /**
     * 创建默认的文件信息
     * 
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param size 文件大小
     * @param contentType 内容类型
     * @return 文件信息
     */
    protected Map<String, Object> createFileInfo(String filePath, String fileName, long size, String contentType) {
        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("path", filePath);
        fileInfo.put("name", fileName);
        fileInfo.put("size", size);
        fileInfo.put("type", contentType);
        fileInfo.put("ext", getFileExtension(fileName));
        fileInfo.put("storageType", getStorageType());
        fileInfo.put("createTime", System.currentTimeMillis());
        return fileInfo;
    }
    
    /**
     * 获取文件URL
     * 
     * @param filePath 文件路径
     * @return 文件URL
     */
    @Override
    public String getFileUrl(String filePath) {
        // 默认实现：调用带过期时间的方法，默认10年
        return getFileUrl(filePath, 315360000); // 10年 = 365 * 10 * 24 * 60 * 60
    }
    
    /**
     * 获取文件URL（带过期时间）
     * 
     * @param filePath 文件路径
     * @param expireSeconds 过期时间（秒）
     * @return 文件URL
     */
    @Override
    public String getFileUrl(String filePath, int expireSeconds) {
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }
        
        // 默认实现：直接返回路径，子类可覆盖实现签名URL
        return filePath;
    }
}