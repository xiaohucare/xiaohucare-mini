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
 * 长者GPS定位设备表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_device")
public class GpsDeviceEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 设备ID
	 */
	@TableId
	@Schema(description = "设备ID")
	@Excel(name = "设备ID")
	private Long id;
	/**
	 * 设备编码
	 */
	@Schema(description = "设备编码")
	@Excel(name = "设备编码")
	private String deviceCode;
	/**
	 * 设备名称
	 */
	@Schema(description = "设备名称")
	@Excel(name = "设备名称")
	private String deviceName;
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
	 * 家属ID（客户ID）
	 */
	@Schema(description = "家属ID（客户ID）")
	@Excel(name = "家属ID（客户ID）")
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
	 * 康养订单ID
	 */
	@Schema(description = "康养订单ID")
	@Excel(name = "康养订单ID")
	private Long boardingId;
	/**
	 * 房间/床位ID
	 */
	@Schema(description = "房间/床位ID")
	@Excel(name = "房间/床位ID")
	private Long roomId;
	/**
	 * 房间名称
	 */
	@Schema(description = "房间名称")
	@Excel(name = "房间名称")
	private String roomName;
	/**
	 * 设备类型：GPS_TRACKER-GPS跟踪器
	 */
	@Schema(description = "设备类型：GPS_TRACKER-GPS跟踪器")
	@Excel(name = "设备类型：GPS_TRACKER-GPS跟踪器")
	private String deviceType;
	/**
	 * 协议类型
	 */
	@Schema(description = "协议类型")
	@Excel(name = "协议类型")
	private String protocol;
	/**
	 * SIM卡号
	 */
	@Schema(description = "SIM卡号")
	@Excel(name = "SIM卡号")
	private String simCardNo;
	/**
	 * IMEI码
	 */
	@Schema(description = "IMEI码")
	@Excel(name = "IMEI码")
	private String imei;
	/**
	 * ICCID码
	 */
	@Schema(description = "ICCID码")
	@Excel(name = "ICCID码")
	private String iccid;
	/**
	 * 设备状态：1-未激活 2-禁用 3-在线 4-离线
	 */
	@Schema(description = "设备状态：1-未激活 2-禁用 3-在线 4-离线")
	@Excel(name = "设备状态：1-未激活 2-禁用 3-在线 4-离线")
	private Integer onlineState;
	/**
	 * 上线时间
	 */
	@Schema(description = "上线时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date onlineTime;
	/**
	 * 下线时间
	 */
	@Schema(description = "下线时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "下线时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date offlineTime;
	/**
	 * 电池电量（%）
	 */
	@Schema(description = "电池电量（%）")
	@Excel(name = "电池电量（%）")
	private Integer batteryLevel;
	/**
	 * 信号强度
	 */
	@Schema(description = "信号强度")
	@Excel(name = "信号强度")
	private Integer signalStrength;
	/**
	 * GPS精度（米）
	 */
	@Schema(description = "GPS精度（米）")
	@Excel(name = "GPS精度（米）")
	private Double gpsAccuracy;
	/**
	 * 定位更新间隔（秒）
	 */
	@Schema(description = "定位更新间隔（秒）")
	@Excel(name = "定位更新间隔（秒）")
	private Integer updateInterval;
	/**
	 * 是否启用：0-禁用 1-启用
	 */
	@Schema(description = "是否启用：0-禁用 1-启用")
	@Excel(name = "是否启用：0-禁用 1-启用")
	private Integer isActive;
	/**
	 * 绑定时间
	 */
	@Schema(description = "绑定时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date bindTime;
	/**
	 * 解绑时间
	 */
	@Schema(description = "解绑时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "解绑时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date unbindTime;
	/**
	 * 绑定原因
	 */
	@Schema(description = "绑定原因")
	@Excel(name = "绑定原因")
	private String bindReason;
	/**
	 * 解绑原因
	 */
	@Schema(description = "解绑原因")
	@Excel(name = "解绑原因")
	private String unbindReason;
	/**
	 * 设备图片
	 */
	@Schema(description = "设备图片")
	@Excel(name = "设备图片")
	private String imgUrl;
	/**
	 * 租户ID
	 */
	@Schema(description = "租户ID")
	@Excel(name = "租户ID")
	private Long tenantId;
	/**
	 * 租户名称
	 */
	@Schema(description = "租户名称")
	@Excel(name = "租户名称")
	private String tenantName;
	/**
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新者
	 */
	@Schema(description = "更新者")
	@Excel(name = "更新者")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;

}