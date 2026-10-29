package com.xiaohucare.app.gps.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备位置缓存表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_device_location_cache")
public class GpsDeviceLocationCacheEntity extends BaseEntity {
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
	 * 家属ID
	 */
	@Schema(description = "家属ID")
	@Excel(name = "家属ID")
	private Long guardianId;
	/**
	 * 最新纬度
	 */
	@Schema(description = "最新纬度")
	@Excel(name = "最新纬度")
	private Double latitude;
	/**
	 * 最新经度
	 */
	@Schema(description = "最新经度")
	@Excel(name = "最新经度")
	private Double longitude;
	/**
	 * 海拔高度
	 */
	@Schema(description = "海拔高度")
	@Excel(name = "海拔高度")
	private Double altitude;
	/**
	 * 当前速度
	 */
	@Schema(description = "当前速度")
	@Excel(name = "当前速度")
	private Double speed;
	/**
	 * 方向角度
	 */
	@Schema(description = "方向角度")
	@Excel(name = "方向角度")
	private Double direction;
	/**
	 * 定位精度
	 */
	@Schema(description = "定位精度")
	@Excel(name = "定位精度")
	private Double accuracy;
	/**
	 * 电池电量
	 */
	@Schema(description = "电池电量")
	@Excel(name = "电池电量")
	private Integer batteryLevel;
	/**
	 * 信号强度
	 */
	@Schema(description = "信号强度")
	@Excel(name = "信号强度")
	private Integer signalStrength;
	/**
	 * 地址信息
	 */
	@Schema(description = "地址信息")
	@Excel(name = "地址信息")
	private String address;
	/**
	 * 定位时间
	 */
	@Schema(description = "定位时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "定位时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date locationTime;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss ")
	private Date updateAt;
	private Date createAt;

	@TableField(exist = false)
	private Integer isDelete;

}