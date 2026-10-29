package com.xiaohucare.framework.oss.constant;

/**
 * OSS常量类
 * 
 * @author xiaohucare
 */
public class OSSConstants {
    
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024L;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;
    
    /**
     * 本地存储配置前缀
     */
    public static final String LOCAL_STORAGE_PREFIX = "oss.local";
    
    /**
     * 阿里云OSS配置前缀
     */
    public static final String ALIYUN_OSS_PREFIX = "oss.aliyun";
    
    /**
     * 华为云OSS配置前缀
     */
    public static final String HUAWEI_OSS_PREFIX = "oss.huawei";
    
    /**
     * RustFS配置前缀
     */
    public static final String RUSTFS_PREFIX = "oss.rustfs";
    
    /**
     * 当前使用的存储类型配置项
     */
    public static final String CURRENT_STORAGE_TYPE = "oss.type";
    
    /**
     * 成功标识
     */
    public static final String SUCCESS = "0";
    
    /**
     * 失败标识
     */
    public static final String FAIL = "1";
}