package com.xiaohucare.app.gps.entity;

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
 * 围栏报警记录表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_fence_alarm_record")
public class GpsFenceAlarmRecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 围栏ID
	 */
	@Schema(description = "围栏ID")
	@Excel(name = "围栏ID")
	private Long fenceId;
	/**
	 * 围栏名称
	 */
	@Schema(description = "围栏名称")
	@Excel(name = "围栏名称")
	private String fenceName;
	/**
	 * 设备ID
	 */
	@Schema(description = "设备ID")
	@Excel(name = "设备ID")
	private Long deviceId;
	/**
	 * 设备编码
	 */
	@Schema(description = "设备编码")
	@Excel(name = "设备编码")
	private String deviceCode;
	/**
	 * 长者ID
	 */
	@Schema(description = "长者ID")
	@Excel(name = "长者ID")
	private Long residentId;
	/**
	 * 长者姓名
	 */
	@Schema(description = "长者姓名")
	@Excel(name = "长者姓名")
	private String residentName;
	/**
	 * 家属ID
	 */
	@Schema(description = "家属ID")
	@Excel(name = "家属ID")
	private Long guardianId;
	/**
	 * 家属姓名
	 */
	@Schema(description = "家属姓名")
	@Excel(name = "家属姓名")
	private String guardianName;
	/**
	 * 家属电话
	 */
	@Schema(description = "家属电话")
	@Excel(name = "家属电话")
	private String guardianPhone;
	/**
	 * 报警类型：ENTER-进入围栏 LEAVE-离开围栏
	 */
	@Schema(description = "报警类型：ENTER-进入围栏 LEAVE-离开围栏")
	@Excel(name = "报警类型：ENTER-进入围栏 LEAVE-离开围栏")
	private String alarmType;
	/**
	 * 报警等级
	 */
	@Schema(description = "报警等级")
	@Excel(name = "报警等级")
	private String alarmLevel;
	/**
	 * 报警消息
	 */
	@Schema(description = "报警消息")
	@Excel(name = "报警消息")
	private String alarmMessage;
	/**
	 * 触发位置纬度
	 */
	@Schema(description = "触发位置纬度")
	@Excel(name = "触发位置纬度")
	private Double triggerLatitude;
	/**
	 * 触发位置经度
	 */
	@Schema(description = "触发位置经度")
	@Excel(name = "触发位置经度")
	private Double triggerLongitude;
	/**
	 * 触发位置地址
	 */
	@Schema(description = "触发位置地址")
	@Excel(name = "触发位置地址")
	private String triggerAddress;
	/**
	 * 触发时间
	 */
	@Schema(description = "触发时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "触发时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date triggerTime;
	/**
	 * 处理状态：0-未处理 1-已确认 2-已忽略
	 */
	@Schema(description = "处理状态：0-未处理 1-已确认 2-已忽略")
	@Excel(name = "处理状态：0-未处理 1-已确认 2-已忽略")
	private Integer status;
	/**
	 * 确认人
	 */
	@Schema(description = "确认人")
	@Excel(name = "确认人")
	private String acknowledgedBy;
	/**
	 * 确认时间
	 */
	@Schema(description = "确认时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date acknowledgedTime;
	/**
	 * 处理备注
	 */
	@Schema(description = "处理备注")
	@Excel(name = "处理备注")
	private String acknowledgedContent;
	/**
	 * 是否已通知：0-未通知 1-已通知
	 */
	@Schema(description = "是否已通知：0-未通知 1-已通知")
	@Excel(name = "是否已通知：0-未通知 1-已通知")
	private Integer isNotified;
	/**
	 * 通知时间
	 */
	@Schema(description = "通知时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "通知时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date notifyTime;
	/**
	 * 通知方式：SMS-短信 PUSH-推送 PHONE-电话
	 */
	@Schema(description = "通知方式：SMS-短信 PUSH-推送 PHONE-电话")
	@Excel(name = "通知方式：SMS-短信 PUSH-推送 PHONE-电话")
	private String notifyMethod;
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
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;

}