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
 * 美容师排班表
 * 
 * @author xiaohucare
 * @date 2026-04-08 18:12:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_employee_schedule")
public class EmployeeScheduleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 排班ID
	 */
	@TableId
	@Schema(description = "排班ID")
	@Excel(name = "排班ID")
	private Long id;
	/**
	 * 排班类型：0 小时排班 1 值班
	 */
	@Schema(description = "排班类型")
	private Integer scheduleType;
	/**
	 * 美容师ID
	 */
	@Schema(description = "员工ID")
	@Excel(name = "员工ID")
	private Long employeeId;
	/**
	 * 排班日期
	 */
	@Schema(description = "排班日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "排班日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date scheduleDate;
	/**
	 * 时间段：09:00, 10:00等
	 */
	@Schema(description = "时间段：09:00, 10:00等")
	@Excel(name = "时间段：09:00, 10:00等")
	private String timeSlot;
	/**
	 * 时段状态：0-可约, 1-已约, 2-休息
	 */
	@Schema(description = "时段状态：0-可约, 1-已约, 2-休息")
	@Excel(name = "时段状态：0-可约, 1-已约, 2-休息")
	private Integer slotStatus;
	/**
	 * 关联预约ID（已约时）
	 */
	@Schema(description = "关联预约ID（已约时）")
	@Excel(name = "关联预约ID（已约时）")
	private Long appointmentId;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
	private Integer isDelete;
}