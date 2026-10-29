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
 * 长者活动统计表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_activity_statistics")
public class GpsActivityStatisticsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 设备ID
	 */
	@Schema(description = "设备ID")
	@Excel(name = "设备ID")
	private Long deviceId;
	/**
	 * 长者ID
	 */
	@Schema(description = "长者ID")
	@Excel(name = "长者ID")
	private Long residentId;
	/**
	 * 家属ID
	 */
	@Schema(description = "家属ID")
	@Excel(name = "家属ID")
	private Long guardianId;
	/**
	 * 统计日期
	 */
	@Schema(description = "统计日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date statDate;
	/**
	 * 总移动距离（米）
	 */
	@Schema(description = "总移动距离（米）")
	@Excel(name = "总移动距离（米）")
	private Double totalDistance;
	/**
	 * 活动时长（分钟）
	 */
	@Schema(description = "活动时长（分钟）")
	@Excel(name = "活动时长（分钟）")
	private Integer activeDuration;
	/**
	 * 休息时长（分钟）
	 */
	@Schema(description = "休息时长（分钟）")
	@Excel(name = "休息时长（分钟）")
	private Integer restDuration;
	/**
	 * 最高速度（km/h）
	 */
	@Schema(description = "最高速度（km/h）")
	@Excel(name = "最高速度（km/h）")
	private Double maxSpeed;
	/**
	 * 平均速度（km/h）
	 */
	@Schema(description = "平均速度（km/h）")
	@Excel(name = "平均速度（km/h）")
	private Double avgSpeed;
	/**
	 * 步数（估算）
	 */
	@Schema(description = "步数（估算）")
	@Excel(name = "步数（估算）")
	private Integer stepCount;
	/**
	 * 消耗卡路里（估算）
	 */
	@Schema(description = "消耗卡路里（估算）")
	@Excel(name = "消耗卡路里（估算）")
	private Double calorieBurn;
	/**
	 * 轨迹点数量
	 */
	@Schema(description = "轨迹点数量")
	@Excel(name = "轨迹点数量")
	private Integer trackPointCount;
	/**
	 * 围栏报警次数
	 */
	@Schema(description = "围栏报警次数")
	@Excel(name = "围栏报警次数")
	private Integer fenceAlarmCount;
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