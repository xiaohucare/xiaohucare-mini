package com.xiaohucare.framework.oss.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态配置OSS上传工具类
 * <p>
 * 支持运行时传入OSS配置（endpoint、accessKey、bucket等），
 * 不依赖全局单例配置，适用于多租户/多Bucket场景。
 *
 * @author xiaohucare
 */
@Slf4j
public class DynamicOss {

    private static final DateTimeFormatter DATE_DIR_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FILE_FMT = DateTimeFormatter.ofPattern("HHmmssS");

    private final Config config;

    public DynamicOss(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("OSS配置不能为空");
        }
        if (StrUtil.isEmpty(config.getEndpoint()) || StrUtil.isEmpty(config.getAccessKeyId())
                || StrUtil.isEmpty(config.getAccessKeySecret()) || StrUtil.isEmpty(config.getBucketName())) {
            throw new IllegalArgumentException("OSS配置不完整: endpoint/accessKeyId/accessKeySecret/bucketName 均为必填");
        }
        this.config = config;
    }

    // ==================== OSS Client 模板 ====================

    /**
     * OSS操作执行模板：自动管理客户端创建与关闭
     */
    @FunctionalInterface
    private interface OssAction<T> {
        T execute(OSS ossClient);
    }

    private <T> T execute(OssAction<T> action) {
        OSS ossClient = new OSSClientBuilder().build(
                config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
        try {
            return action.execute(ossClient);
        } finally {
            try { ossClient.shutdown(); } catch (Exception ignored) {}
        }
    }

    // ==================== 上传 ====================

    /**
     * 上传 MultipartFile（自动生成路径）
     */
    public String upload(MultipartFile file) {
        String objectKey = generateObjectKey(file.getOriginalFilename());
        try {
            return upload(file.getInputStream(), objectKey, file.getContentType());
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 MultipartFile（自定义 objectKey）
     */
    public String upload(MultipartFile file, String objectKey) {
        try {
            return upload(file.getInputStream(), objectKey, file.getContentType());
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 File（自动生成路径）
     */
    public String upload(File file) {
        String objectKey = generateObjectKey(file.getName());
        try (InputStream inputStream = new FileInputStream(file)) {
            return upload(inputStream, objectKey, null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 File（自定义 objectKey）
     */
    public String upload(File file, String objectKey) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return upload(inputStream, objectKey, null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 上传 byte 数组
     *
     * @param data      文件数据
     * @param objectKey 文件路径
     */
    public String upload(byte[] data, String objectKey) {
        return upload(new ByteArrayInputStream(data), objectKey, null);
    }

    /**
     * 上传 InputStream（核心方法）
     *
     * @param inputStream 文件输入流
     * @param objectKey   OSS对象路径
     * @param contentType 文件类型（如 image/jpeg），可为 null
     * @return 文件访问URL
     */
    public String upload(InputStream inputStream, String objectKey, String contentType) {
        try {
            return execute(ossClient -> {
                PutObjectRequest putReq = new PutObjectRequest(config.getBucketName(), objectKey, inputStream);
                if (StrUtil.isNotEmpty(contentType)) {
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentType(contentType);
                    putReq.setMetadata(metadata);
                }
                ossClient.putObject(putReq);
                return getFileUrl(objectKey);
            });
        } catch (Exception e) {
            log.error("OSS上传失败: bucket={}, key={}", config.getBucketName(), objectKey, e);
            throw new RuntimeException("OSS上传失败: " + e.getMessage(), e);
        }
    }

    // ==================== 删除 ====================

    /**
     * 删除文件
     *
     * @param fileUrl 文件完整URL或对象路径
     * @return 是否成功
     */
    public boolean deleteFile(String fileUrl) {
        if (StrUtil.isEmpty(fileUrl)) {
            return false;
        }
        try {
            String objectKey = removeUrlPrefix(fileUrl);
            execute(ossClient -> {
                ossClient.deleteObject(config.getBucketName(), objectKey);
                return null;
            });
            return true;
        } catch (Exception e) {
            log.error("OSS删除失败: bucket={}, file={}", config.getBucketName(), fileUrl, e);
            return false;
        }
    }

    // ==================== URL / 路径工具 ====================

    /**
     * 根据对象路径获取访问URL
     */
    public String getFileUrl(String objectKey) {
        if (StrUtil.isEmpty(objectKey)) {
            return objectKey;
        }
        String protocol = config.isHttps() ? "https://" : "http://";
        String host = StrUtil.isNotEmpty(config.getDomain())
                ? config.getDomain()
                : config.getBucketName() + "." + config.getEndpoint();
        return protocol + host + "/" + objectKey;
    }

    /**
     * 生成对象路径（带日期目录 + 唯一文件名）
     * <p>格式: [prefix/]yyyyMMdd/HHmmssS+随机串.扩展名
     */
    public String generateObjectKey(String originalFilename) {
        LocalDateTime now = LocalDateTime.now();
        String dateDir = now.format(DATE_DIR_FMT);
        String timeFile = now.format(TIME_FILE_FMT)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        String extension = extractExtension(originalFilename);
        String relativePath = dateDir + "/" + timeFile + extension;

        String prefix = config.getPrefix();
        if (StrUtil.isNotEmpty(prefix)) {
            return prefix.endsWith("/") ? prefix + relativePath : prefix + "/" + relativePath;
        }
        return relativePath;
    }

    /**
     * 生成带自定义文件名的对象路径
     * <p>格式: [prefix/]yyyyMMdd/[preFileName]_HHmmssS+随机串
     */
    public String generateObjectKey(String originalFilename, String preFileName) {
        LocalDateTime now = LocalDateTime.now();
        String dateDir = now.format(DATE_DIR_FMT);
        String timeFile = now.format(TIME_FILE_FMT)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        if (StrUtil.isNotBlank(preFileName)) {
            timeFile = preFileName + "_" + timeFile;
        }
        String relativePath = dateDir + "/" + timeFile;

        String prefix = config.getPrefix();
        if (StrUtil.isNotEmpty(prefix)) {
            return prefix.endsWith("/") ? prefix + relativePath : prefix + "/" + relativePath;
        }
        return relativePath;
    }

    // ==================== 内部方法 ====================

    private String extractExtension(String filename) {
        if (StrUtil.isEmpty(filename)) {
            return "";
        }
        int lastDot = filename.lastIndexOf(".");
        if (lastDot > 0) {
            return filename.substring(lastDot).toLowerCase();
        }
        return "";
    }

    private String removeUrlPrefix(String fileUrl) {
        if (StrUtil.isEmpty(fileUrl)) {
            return fileUrl;
        }
        if (fileUrl.contains("://")) {
            int startIdx = fileUrl.indexOf("://") + 3;
            int endIdx = fileUrl.indexOf('/', startIdx);
            if (endIdx > 0) {
                return fileUrl.substring(endIdx + 1);
            }
        }
        return fileUrl;
    }

    // ==================== 配置类 ====================

    /**
     * 动态OSS配置
     */
    @Data
    @Builder
    public static class Config {
        /** OSS地域节点（如 oss-cn-hangzhou.aliyuncs.com） */
        private String endpoint;
        /** AccessKey ID */
        private String accessKeyId;
        /** AccessKey Secret */
        private String accessKeySecret;
        /** Bucket名称 */
        private String bucketName;
        /** 自定义域名（可选，用于生成访问URL） */
        private String domain;
        /** 存储路径前缀（可选，如 "uploads/2026"） */
        private String prefix;
        /** 是否使用https（默认true） */
        @Builder.Default
        private boolean https = true;
    }
}
