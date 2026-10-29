package com.xiaohucare.framework.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.xiaohucare.framework.oss.constant.OSSConstants;

/**
 * 本地存储配置类
 * 
 * @author xiaohucare
 */
@Component
@ConfigurationProperties(prefix = OSSConstants.LOCAL_STORAGE_PREFIX)
public class LocalOssProperties extends BaseOssProperties {
    /**
     * 本地存储路径
     */
    private String path;
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}