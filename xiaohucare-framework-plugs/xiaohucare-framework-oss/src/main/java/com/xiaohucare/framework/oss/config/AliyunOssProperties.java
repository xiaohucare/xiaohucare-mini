package com.xiaohucare.framework.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.xiaohucare.framework.oss.constant.OSSConstants;

/**
 * 阿里云OSS配置类
 * 
 * @author xiaohucare
 */
@Component
@ConfigurationProperties(prefix = OSSConstants.ALIYUN_OSS_PREFIX)
public class AliyunOssProperties extends BaseOssProperties {
    
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
     * Bucket名称
     */
    private String bucketName;
    
    /**
     * RAM角色ARN（用于STS临时凭证）
     * 格式: acs:ram::<AccountID>:role/<RoleName>
     * 示例: acs:ram::1234567890123456:role/OssUploadRole
     */
    private String roleArn = "acs:ram::your-account-id:role/your-role-name";
    
    public String getRoleArn() {
		return roleArn;
	}

	public void setRoleArn(String roleArn) {
		this.roleArn = roleArn;
	}

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
}