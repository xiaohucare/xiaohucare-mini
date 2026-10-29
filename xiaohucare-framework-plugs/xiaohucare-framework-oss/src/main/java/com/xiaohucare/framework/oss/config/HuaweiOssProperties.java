package com.xiaohucare.framework.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.xiaohucare.framework.oss.constant.OSSConstants;

/**
 * 华为云OSS配置类
 * 
 * @author xiaohucare
 */
@Component
@ConfigurationProperties(prefix = OSSConstants.HUAWEI_OSS_PREFIX)
public class HuaweiOssProperties extends BaseOssProperties {
    
    /**
     * 地域节点
     */
    private String endpoint;
    
    /**
     * AccessKey ID
     */
    private String accessKeyId;
    
    /**
     * AccessKey Secret
     */
    private String accessKeySecret;
    
    /**
     * 桶名称
     */
    private String bucketName;
    
    /**
     * 区域
     */
    private String region;
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
    
    public String getBucketName() {
        return bucketName;
    }
    
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
}