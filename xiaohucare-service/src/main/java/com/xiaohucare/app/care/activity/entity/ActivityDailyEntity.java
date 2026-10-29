package com.xiaohucare.app.care.activity.entity;

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
 * 日常活动记录表
 * 
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("e_activity_daily")
public class ActivityDailyEntity extends BaseEntity{
private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
				    @Excel(name = "")
	private Integer id;
	/**
	 * 活动类型ID
	 */
		@Schema(description ="活动类型ID")
			    @Excel(name = "活动类型ID")
	private Integer activityTypeId;
	/**
	 * 活动名称
	 */
		@Schema(description ="活动名称")
			    @Excel(name = "活动名称")
	private String activityName;
	/**
	 * 活动日期
	 */
		@Schema(description ="活动日期")
			    @JsonFormat(pattern = "yyyy-MM-dd")
	    @Excel(name = "活动日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date activityDate;
	/**
	 * 开始时间
	 */
		@Schema(description ="开始时间")
			    @Excel(name = "开始时间")
	private String startTime;
	/**
	 * 结束时间
	 */
		@Schema(description ="结束时间")
			    @Excel(name = "结束时间")
	private String endTime;
	/**
	 * 活动地点
	 */
		@Schema(description ="活动地点")
			    @Excel(name = "活动地点")
	private String location;
	/**
	 * 组织者ID
	 */
		@Schema(description ="组织者ID")
			    @Excel(name = "组织者ID")
	private Integer organizerId;
	/**
	 * 参与人数
	 */
		@Schema(description ="参与人数")
			    @Excel(name = "参与人数")
	private Integer participantCount;
	/**
	 * 活动描述
	 */
		@Schema(description ="活动描述")
			    @Excel(name = "活动描述")
	private String description;
	/**
	 * 备注
	 */
		@Schema(description ="备注")
			    @Excel(name = "备注")
	private String remarks;
	/**
	 * 状态：1正常，0取消
	 */
		@Schema(description ="状态：1正常，0取消")
			    @Excel(name = "状态：1正常，0取消")
	private Integer status;
	/**
	 * 创建时间
	 */
		@Schema(description ="创建时间")
			    @JsonFormat(pattern = "yyyy-MM-dd")
	    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新时间
	 */
		@Schema(description ="更新时间")
			    @JsonFormat(pattern = "yyyy-MM-dd")
	    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;

}