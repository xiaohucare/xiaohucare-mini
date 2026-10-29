package com.xiaohucare.app.care.entity;

import java.math.BigDecimal;
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
 * 预约表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_appointment")
public class AppointmentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	@Excel(name = "用户ID")
	private Long memberId;
	/**
	 * 预约编号
	 */
	@Schema(description = "预约编号")
	@Excel(name = "预约编号")
	private String appointmentNo;
	/**
	 * 预约人ID（通常为家属）
	 */
	@Schema(description = "预约人ID")
	@Excel(name = "预约人ID")
	private Long appointerId;
	/**
	 * 预约人姓名
	 */
	@Schema(description = "预约人姓名")
	@Excel(name = "预约人姓名")
	private String appointerName;
	/**
	 * 预约人联系电话
	 */
	@Schema(description = "预约人联系电话")
	@Excel(name = "预约人联系电话")
	private String appointerPhone;
	/**
	 * 预约人与长者关系（子女/配偶/本人等）
	 */
	@Schema(description = "预约人与长者关系")
	@Excel(name = "预约人与长者关系")
	private String appointerRelation;
	/**
	 * 意向入住长者ID（关联 e_resident.id，新客可空）
	 */
	@Schema(description = "意向入住长者ID")
	@Excel(name = "意向入住长者ID")
	private Long residentId;
	/**
	 * 意向入住长者姓名
	 */
	@Schema(description = "意向入住长者姓名")
	@Excel(name = "意向入住长者姓名")
	private String residentName;
	/**
	 * 房型性别偏好：0-不限 1-男 2-女
	 */
	@Schema(description = "房型性别偏好：0-不限 1-男 2-女")
	@Excel(name = "房型性别偏好")
	private Integer genderRequired;
	/**
	 * 意向床位数
	 */
	@Schema(description = "意向床位数")
	@Excel(name = "意向床位数")
	private Integer bedCount;
	/**
	 * 照护等级需求（自理/半自理/失能）
	 */
	@Schema(description = "照护等级需求（自理/半自理/失能）")
	@Excel(name = "照护等级需求")
	private String careLevelDemand;
	/**
	 * 意向楼栋/康养区
	 */
	@Schema(description = "意向楼栋/康养区")
	@Excel(name = "意向楼栋/康养区")
	private String buildingPref;
	/**
	 * 预约日期
	 */
	@Schema(description = "预约日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "预约日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date appointmentDate;
	/**
	 * 预计天数
	 */
	@Schema(description = "预计天数")
	@Excel(name = "预计天数")
	private Integer estimatedDays;
	/**
	 * 选择的套餐ID
	 */
	@Schema(description = "选择的套餐ID")
	@Excel(name = "选择的套餐ID")
	private Long comboId;
	
	private String comboName;
	private String coverImage;
	/**
	 * 日单价
	 */
	@Schema(description = "日单价")
	@Excel(name = "日单价")
	private BigDecimal dailyPrice;
	private BigDecimal depositAmount;
	/**
	 * 预计费用
	 */
	@Schema(description = "预计费用")
	@Excel(name = "预计费用")
	private BigDecimal estimatedFee;
	/**
	 * 状态（0-待确认 1-已确认 2-已取消 3-已登记）
	 */
	@Schema(description = "状态（0-待确认 1-已确认 2-已取消 3-已登记）")
	@Excel(name = "状态（0-待确认 1-已确认 2-已取消 3-已登记）")
	private Integer status;
	/**
	 * 特殊需求
	 */
	@Schema(description = "特殊需求")
	@Excel(name = "特殊需求")
	private String specialNeeds;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remarks;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
	/**
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
	/**
	 * 更新者
	 */
	@Schema(description = "更新者")
	@Excel(name = "更新者")
	private String updateBy;
	
	/**
	 * VIP标记：0-否, 1-是
	 */
	@Schema(description = "VIP标记：0-否, 1-是")
	@Excel(name = "VIP标记：0-否, 1-是")
	private Integer isVip;
	/**
	 * 急单标记：0-否, 1-是
	 */
	@Schema(description = "急单标记：0-否, 1-是")
	@Excel(name = "急单标记：0-否, 1-是")
	private Integer isUrgent;

}