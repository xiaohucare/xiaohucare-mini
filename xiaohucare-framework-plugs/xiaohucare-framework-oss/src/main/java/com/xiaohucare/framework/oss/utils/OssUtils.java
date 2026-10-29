package com.xiaohucare.framework.oss.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.xiaohucare.framework.oss.service.IFileStorageService;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * OSS工具类
 * 提供静态方法方便调用文件存储服务
 *
 * @author xiaohucare
 */
@Component
public class OssUtils {

    private static IFileStorageService fileStorageService;

    @Autowired
    @Qualifier("fileStorageService")
    private IFileStorageService service;

    @PostConstruct
    public void init() {
        fileStorageService = service;
    }

    // ==================== 上传 ====================

    /**
     * 上传 MultipartFile
     *
     * @param file 上传的文件
     * @return 文件访问路径
     */
    public static String uploadFile(MultipartFile file) {
        checkService();
        return fileStorageService.uploadFile(file);
    }

    /**
     * 上传 MultipartFile（自定义文件名）
     *
     * @param file     上传的文件
     * @param fileName 自定义文件名
     * @return 文件访问路径
     */
    public static String uploadFile(MultipartFile file, String fileName) {
        checkService();
        return fileStorageService.uploadFile(file, fileName);
    }
    public static String uploadFile(MultipartFile file, String prefix,String fileName) {
    	checkService();
    	return fileStorageService.uploadFile(file,prefix,fileName);
    }

    /**
     * 上传 InputStream
     *
     * @param inputStream 文件输入流
     * @param fileName    文件名
     * @param contentType 文件类型（如 image/jpeg）
     * @return 文件访问路径
     */
    public static String uploadFile(InputStream inputStream, String fileName, String contentType) {
        checkService();
        return fileStorageService.uploadFile(inputStream, fileName, contentType);
    }

    /**
     * 上传 File（自动生成文件名）
     *
     * @param file 上传的文件
     * @return 文件访问路径
     */
    public static String uploadFile(File file) {
        checkService();
        try (InputStream inputStream = new FileInputStream(file)) {
            return fileStorageService.uploadFile(inputStream, file.getName(), null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 File（自定义文件名）
     *
     * @param file     上传的文件
     * @param fileName 自定义文件名
     * @return 文件访问路径
     */
    public static String uploadFile(File file, String fileName) {
        checkService();
        try (InputStream inputStream = new FileInputStream(file)) {
            return fileStorageService.uploadFile(inputStream, fileName, null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 byte 数组
     *
     * @param data     文件数据
     * @param fileName 文件名
     * @return 文件访问路径
     */
    public static String uploadFile(byte[] data, String fileName) {
        checkService();
        return fileStorageService.uploadFile(new java.io.ByteArrayInputStream(data), fileName, null);
    }

    // ==================== 删除 ====================

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        checkService();
        return fileStorageService.deleteFile(filePath);
    }

    // ==================== 查询 ====================

    /**
     * 获取文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    public static Map<String, Object> getFileInfo(String filePath) {
        checkService();
        return fileStorageService.getFileInfo(filePath);
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件输入流
     */
    public static InputStream downloadFile(String filePath) {
        checkService();
        return fileStorageService.downloadFile(filePath);
    }

    // ==================== URL ====================

    /**
     * 获取文件URL
     *
     * @param filePath 文件路径
     * @return 文件URL
     */
    public static String getFileUrl(String filePath) {
        checkService();
        return fileStorageService.getFileUrl(filePath);
    }

    /**
     * 获取文件URL（带过期时间）
     *
     * @param filePath      文件路径
     * @param expireSeconds 过期时间（秒）
     * @return 文件URL
     */
    public static String getFileUrl(String filePath, int expireSeconds) {
        checkService();
        return fileStorageService.getFileUrl(filePath, expireSeconds);
    }

    // ==================== 配置 ====================

    /**
     * 获取当前存储类型
     *
     * @return 存储类型
     */
    public static String getStorageType() {
        checkService();
        return fileStorageService.getStorageType();
    }

    /**
     * 获取存储路径前缀
     *
     * @return 路径前缀
     */
    public static String getPrefix() {
        checkService();
        return fileStorageService.getPrefix();
    }

    /**
     * 检查服务是否初始化
     */
    private static void checkService() {
        if (fileStorageService == null) {
            throw new RuntimeException("OSS服务尚未初始化，请检查配置");
        }
    }
}