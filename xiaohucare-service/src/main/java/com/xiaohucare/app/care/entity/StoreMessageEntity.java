package com.xiaohucare.app.care.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺通知表(门店消息)
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_notice")
@Schema(description = "门店消息")
public class StoreMessageEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@TableId
	@Schema(description = "主键")
	@Excel(name = "主键")
	private Long id;
	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	@Excel(name = "店铺名称")
	private String storeName;
	/**
	 * 标题
	 */
	@Schema(description = "标题")
	@Excel(name = "标题")
	private String title;
	/**
	 * 内容
	 */
	@Schema(description = "内容")
	private String content;
	/**
	 * 应用类型
	 */
	@Schema(description = "应用类型")
	private Integer appType;
	/**
	 * 应用ID
	 */
	@Schema(description = "应用ID")
	private Long appId;
	/**
	 * 分类: 0=系统 1=活动 2=交易
	 */
	@Schema(description = "分类: 0=系统 1=活动 2=交易")
	@Excel(name = "分类", dictType = "store_notice_tips_type")
	private Integer tipsType;
	/**
	 * 价值
	 */
	@Schema(description = "价值")
	private Integer worth;
	/**
	 * 浏览次数
	 */
	@Schema(description = "浏览次数")
	@Excel(name = "浏览次数")
	private Integer viewCount;
	/**
	 * 回复次数
	 */
	@Schema(description = "回复次数")
	@Excel(name = "回复次数")
	private Integer replyCount;
	/**
	 * 最后回复人
	 */
	@Schema(description = "最后回复人")
	private String replyMemberName;
	/**
	 * 最后回复时间
	 */
	@Schema(description = "最后回复时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date replyLastTime;
	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sequence;
	/**
	 * 有效时间起
	 */
	@Schema(description = "有效时间起")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "有效时间起", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date validStartDate;
	/**
	 * 有效时间止
	 */
	@Schema(description = "有效时间止")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "有效时间止", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date validEndDate;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;
	/**
	 * 状态: 0=待处理 1=已处理 2=已关闭
	 */
	@Schema(description = "状态: 0=待处理 1=已处理 2=已关闭")
	@Excel(name = "状态", dictType = "store_notice_state")
	private Integer state;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;

}
