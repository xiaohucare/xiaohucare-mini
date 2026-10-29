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
 * 活动参与记录表
 * 
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("e_activity_participant")
public class ActivityParticipantEntity extends BaseEntity{
private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
				    @Excel(name = "")
	private Integer id;
	/**
	 * 活动ID
	 */
		@Schema(description ="活动ID")
			    @Excel(name = "活动ID")
	private Integer activityId;
	/**
	 * 老人ID
	 */
		@Schema(description ="老人ID")
			    @Excel(name = "老人ID")
	private Integer residentId;
	/**
	 * 参与状态
	 */
		@Schema(description ="参与状态：1已参与，0未参与，2请假")
			    @Excel(name = "参与状态", readConverterExp = "1=已参与,0=未参与,2=请假")
	private Integer participationStatus;
	/**
	 * 反馈意见
	 */
		@Schema(description ="反馈意见")
			    @Excel(name = "反馈意见")
	private String feedback;
	/**
	 * 评分(1-5分)
	 */
		@Schema(description ="评分(1-5分)")
			    @Excel(name = "评分(1-5分)")
	private Integer rating;
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