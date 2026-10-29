
package com.xiaohucare.app.care.vo;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

 /**
 * 商户 视图实体类
 * @author xiaohucare
 * @date 2026-04-15 19:01:57
 */
 
@Data
public class StoreSericeConfigVO {
	private Long id;
	@Schema(description = "审核状态：0 默认 1 待审核 2已通过 3 已拒绝")
	@Excel(name = "审核状态：0 默认 1 待审核 2已通过 3 已拒绝")
	private Integer useState;
	/**
	 * 过期时间方式
	 */
	@Schema(description = "过期时间方式：1 天，2 月有效 3 年有效")
	@Excel(name = "过期时间方式")
	private Integer validTimeType;
	/**
	 * 过期时间
	 */
	@Schema(description = "过期时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
	private Date validTime;
	
	/**
	 * 签约文件
	 */
	private String signPdf;
	/**
	 * 签约日期
	 */
	private Date signDate;
	/**
	 * 签约状态
	 */
	private Integer signState;
	
	
	/**
	 * 是否开通 康复
	 */
	@Schema(description = "是否开通 康复")
	@Excel(name = "是否开通 康复")
	private Integer storeOpenRehab;
	/**
	 * 是否开通商城
	 */
	@Schema(description = "是否开通商城")
	@Excel(name = "是否开通商城")
	private Integer storeOpenMall;
	/**
	 * 是否开通康养
	 */
	@Schema(description = "是否开通康养")
	@Excel(name = "是否开通康养")
	private Integer storeOpenCare;
	@Schema(description = "库存")
	@Excel(name = "库存")
	private Integer storeOpenWms;
	@Schema(description = "日间照料")
	@Excel(name = "日间照料")
	private Integer storeOpenDay;
	/**
	 * 医疗服务
	 */
	@Schema(description = "医疗服务")
	@Excel(name = "医疗服务")
	private Integer storeOpenMed;
	
	/**
	 * 就医
	 */
	@Schema(description = "就医")
	@Excel(name = "就医")
	private Integer storeOpenHosp;
	@Schema(description = "批发")
	@Excel(name = "批发")
	private Integer storeOpenPf;
	private String remark;
	
	
	
	/**
	 * OSS类型:0 系统 1自定义
	 */
	@Schema(description = "OSS类型:0 系统 1自定义")
	@Excel(name = "OSS类型:0 系统 1自定义")
	private Integer ossType;
	/**
	 * OSS Endpoint
	 */
	@Schema(description = "OSS Endpoint")
	@Excel(name = "OSS Endpoint")
	private String ossEndpoint;
	/**
	 * OSS Access Key ID
	 */
	@Schema(description = "OSS Access Key ID")
	@Excel(name = "OSS Access Key ID")
	private String ossAccessKeyId;
	/**
	 * OSS Access Key Secret
	 */
	@Schema(description = "OSS Access Key Secret")
	@Excel(name = "OSS Access Key Secret")
	private String ossAccessKeySecret;
	/**
	 * OSS Bucket
	 */
	@Schema(description = "OSS Bucket")
	@Excel(name = "OSS Bucket")
	private String ossBucket;
	/**
	 * OSS域名
	 */
	@Schema(description = "OSS域名")
	@Excel(name = "OSS域名")
	private String ossDomain;
	/**
	 * OSS前缀
	 */
	@Schema(description = "OSS前缀")
	@Excel(name = "OSS前缀")
	private String ossPrefix;
	/**
	 * OSS HTTPS:0 HTTP 1HTTPS
	 */
	@Schema(description = "OSS HTTPS:0 HTTP 1HTTPS")
	@Excel(name = "OSS HTTPS:0 HTTP 1HTTPS")
	private Integer ossHttps;
}
