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
 * 活动类型表
 * 
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_activity_type")
public class ActivityTypeEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	@Excel(name = "")
	private Integer id;
	/**
	 * 活动类型名称
	 */
	@Schema(description ="活动类型名称")
	@Excel(name = "活动类型名称")
	private String typeName;
	/**
	 * 描述
	 */
	@Schema(description ="描述")
	@Excel(name = "描述")
	private String description;
	/**
	 * 状态：1正常，0停用
	 */
	@Schema(description ="状态：1正常，0停用")
	@Excel(name = "状态：1正常，0停用")
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