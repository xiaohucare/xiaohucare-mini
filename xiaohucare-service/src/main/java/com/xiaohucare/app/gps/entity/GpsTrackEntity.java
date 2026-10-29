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
 * 长者GPS轨迹表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_track")
public class GpsTrackEntity extends BaseEntity {
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
	 * 纬度
	 */
	@Schema(description = "纬度")
	@Excel(name = "纬度")
	private Double latitude;
	/**
	 * 经度
	 */
	@Schema(description = "经度")
	@Excel(name = "经度")
	private Double longitude;
	/**
	 * 海拔高度（米）
	 */
	@Schema(description = "海拔高度（米）")
	@Excel(name = "海拔高度（米）")
	private Double altitude;
	/**
	 * 速度（km/h）
	 */
	@Schema(description = "速度（km/h）")
	@Excel(name = "速度（km/h）")
	private Double speed;
	/**
	 * 方向角度（0-360）
	 */
	@Schema(description = "方向角度（0-360）")
	@Excel(name = "方向角度（0-360）")
	private Double direction;
	/**
	 * 定位精度（米）
	 */
	@Schema(description = "定位精度（米）")
	@Excel(name = "定位精度（米）")
	private Double accuracy;
	/**
	 * 定位类型：GPS-卫星定位 BASE-基站定位 WIFI-WIFI定位
	 */
	@Schema(description = "定位类型：GPS-卫星定位 BASE-基站定位 WIFI-WIFI定位")
	@Excel(name = "定位类型：GPS-卫星定位 BASE-基站定位 WIFI-WIFI定位")
	private String locationType;
	/**
	 * 地址信息
	 */
	@Schema(description = "地址信息")
	@Excel(name = "地址信息")
	private String address;
	/**
	 * 省份
	 */
	@Schema(description = "省份")
	@Excel(name = "省份")
	private String province;
	/**
	 * 城市
	 */
	@Schema(description = "城市")
	@Excel(name = "城市")
	private String city;
	/**
	 * 区域
	 */
	@Schema(description = "区域")
	@Excel(name = "区域")
	private String district;
	/**
	 * 轨迹时间
	 */
	@Schema(description = "轨迹时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "轨迹时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date trackTime;
	/**
	 * 是否有效：0-无效 1-有效
	 */
	@Schema(description = "是否有效：0-无效 1-有效")
	@Excel(name = "是否有效：0-无效 1-有效")
	private Integer isValid;
	/**
	 * 是否触发围栏报警：0-否 1-是
	 */
	@Schema(description = "是否触发围栏报警：0-否 1-是")
	@Excel(name = "是否触发围栏报警：0-否 1-是")
	private Integer isFenceAlarm;
	/**
	 * 关联最近摄像头设备ID
	 */
	@Schema(description = "关联最近摄像头设备ID")
	@Excel(name = "关联最近摄像头设备ID")
	private Long cameraDeviceId;
	/**
	 * 关联截图ID
	 */
	@Schema(description = "关联截图ID")
	@Excel(name = "关联截图ID")
	private Long cameraSnapshotId;
	/**
	 * 关联录像ID
	 */
	@Schema(description = "关联录像ID")
	@Excel(name = "关联录像ID")
	private Long cameraRecordId;
	/**
	 * 设备电量（%）
	 */
	@Schema(description = "设备电量（%）")
	@Excel(name = "设备电量（%）")
	private Integer batteryLevel;
	/**
	 * 信号强度
	 */
	@Schema(description = "信号强度")
	@Excel(name = "信号强度")
	private Integer signalStrength;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;

	@TableField(exist = false)
	private Integer isDelete;
}