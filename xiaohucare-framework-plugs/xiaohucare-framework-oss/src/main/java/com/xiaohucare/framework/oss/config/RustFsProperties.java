package com.xiaohucare.framework.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.xiaohucare.framework.oss.constant.OSSConstants;

/**
 * RustFS配置类
 * 
 * @author xiaohucare
 */
@Component
@ConfigurationProperties(prefix = OSSConstants.RUSTFS_PREFIX)
public class RustFsProperties extends BaseOssProperties {
    
    /**
     * API地址
     */
    private String apiUrl;
    
    /**
     * 应用ID
     */
    private String appId;
    
    /**
     * 应用密钥
     */
    private String appSecret;
    
    /**
     * 超时时间（毫秒）
     */
    private int timeout = 30000;
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}