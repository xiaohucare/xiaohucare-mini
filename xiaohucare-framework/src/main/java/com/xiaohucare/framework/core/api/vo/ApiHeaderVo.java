package com.xiaohucare.framework.core.api.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiHeaderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String appId;//应用AppId
	private String token;//应用Token
	
	private Long groupId;
	private String groupName;//用户系统
	private Integer tenantId;
	//登录后自动填入
	private Long userId;
	private String userName;
	private String userPhoto;
	private String certiName;
	private String certiNumber;
	private String moblie;
	private String certiNo;
	private String shareCode;
	private Integer siteId;
	private Integer isBlack;//是否是黑户
	
	private String mchNo;
	private Long sysUserId;
	private String sysUserName;
	
	private String appCode;
	
	private Long agentId;
	private String agentName;

	private String agentAppCode;
	private Integer openType;
	private Integer auditState;
	private Integer grabType;
	private String storeName;
	private Integer roomId;
	private Integer villageId;
	private String devUUID;
	private String tradeAccount;
	/**
     * 签约类型
     */
    private Integer signType;
    /**
     * 签约对象ID
     */
    private Long signTypeId;
    /**
     * 
     */
    private String signTypeName;
    private Integer agentType;
    
    private Integer shopType;
    private Long walletMemberId;
    
    
    private Long shopId;
    private String shopName;
    
    /**
	 * OSS类型:0 系统 1自定义
	 */
	@Schema(description = "OSS类型:0 系统 1自定义")
	private Integer ossType;
	/**
	 * OSS Endpoint
	 */
	@Schema(description = "OSS Endpoint")
	private String ossEndpoint;
	/**
	 * OSS Access Key ID
	 */
	@Schema(description = "OSS Access Key ID")
	private String ossAccessKeyId;
	/**
	 * OSS Access Key Secret
	 */
	@Schema(description = "OSS Access Key Secret")
	private String ossAccessKeySecret;
	/**
	 * OSS Bucket
	 */
	@Schema(description = "OSS Bucket")
	private String ossBucket;
	/**
	 * OSS域名
	 */
	@Schema(description = "OSS域名")
	private String ossDomain;
	/**
	 * OSS前缀
	 */
	@Schema(description = "OSS前缀")
	private String ossPrefix;
	/**
	 * OSS HTTPS:0 HTTP 1HTTPS
	 */
	@Schema(description = "OSS HTTPS:0 HTTP 1HTTPS")
	private Integer ossHttps;
	
}
