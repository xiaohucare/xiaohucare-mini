package com.xiaohucare.framework.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OSS基础配置类
 * 
 * @author xiaohucare
 */
@ConfigurationProperties
public abstract class BaseOssProperties {
    
    /**
     * 是否启用
     */
    protected boolean enabled = false;
    
    /**
     * 访问域名
     */
    protected String domain;
    
    /**
     * 是否使用https
     */
    protected boolean https = false;
    
    /**
     * 存储路径前缀
     */
    protected String prefix = "";
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public boolean isHttps() {
        return https;
    }
    
    public void setHttps(boolean https) {
        this.https = https;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}