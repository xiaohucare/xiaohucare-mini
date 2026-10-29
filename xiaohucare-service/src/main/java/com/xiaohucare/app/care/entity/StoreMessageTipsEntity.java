package com.xiaohucare.app.care.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺通知提示表(门店消息未读提示)
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_notice_tips")
@Schema(description = "门店消息未读提示")
public class StoreMessageTipsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@TableId
	@Schema(description = "主键")
	private Long id;
	/**
	 * 分类: 0=系统 1=活动 2=交易
	 */
	@Schema(description = "分类: 0=系统 1=活动 2=交易")
	private Integer tipsType;
	/**
	 * 未读数量
	 */
	@Schema(description = "未读数量")
	private Integer count;
	/**
	 * 最后通知
	 */
	@Schema(description = "最后通知")
	private String lastNotice;
	/**
	 * 最后通知标题
	 */
	@Schema(description = "最后通知标题")
	private String lastNoticeTitle;
	/**
	 * 最后通知时间
	 */
	@Schema(description = "最后通知时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastDate;
	/**
	 * 最后通知ID
	 */
	@Schema(description = "最后通知ID")
	private Long lastId;
	/**
	 * 通知ID集合
	 */
	@Schema(description = "通知ID集合")
	private String noticeIds;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;

}
