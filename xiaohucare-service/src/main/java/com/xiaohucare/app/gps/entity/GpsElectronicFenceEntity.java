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
 * 电子围栏表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_electronic_fence")
public class GpsElectronicFenceEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 围栏ID
	 */
	@TableId
	@Schema(description = "围栏ID")
	@Excel(name = "围栏ID")
	private Long id;
	/**
	 * 围栏名称
	 */
	@Schema(description = "围栏名称")
	@Excel(name = "围栏名称")
	private String fenceName;
	/**
	 * 围栏编码
	 */
	@Schema(description = "围栏编码")
	@Excel(name = "围栏编码")
	private String fenceCode;
	/**
	 * 围栏类型：1-圆形 2-多边形 3-路线
	 */
	@Schema(description = "围栏类型：1-圆形 2-多边形 3-路线")
	@Excel(name = "围栏类型：1-圆形 2-多边形 3-路线")
	private Integer fenceType;
	/**
	 * 中心点纬度
	 */
	@Schema(description = "中心点纬度")
	@Excel(name = "中心点纬度")
	private Double centerLatitude;
	/**
	 * 中心点经度
	 */
	@Schema(description = "中心点经度")
	@Excel(name = "中心点经度")
	private Double centerLongitude;
	private String centerAddress;
	/**
	 * 半径（米）
	 */
	@Schema(description = "半径（米）")
	@Excel(name = "半径（米）")
	private Double radius;
	/**
	 * 多边形顶点坐标JSON数组
	 * eg:[{"lng":116.407400,"lat":39.904200},{"lng":116.408500,"lat":39.904500},{"lng":116.409000,"lat":39.903800},{"lng":116.407800,"lat":39.903500}]
	 */
	@Schema(description = "多边形顶点坐标JSON数组")
	@Excel(name = "多边形顶点坐标JSON数组")
	private String polygonPoints;
	/**
	 * 路线坐标点JSON数组
	 */
	@Schema(description = "路线坐标点JSON数组")
	@Excel(name = "路线坐标点JSON数组")
	private String routePoints;
	/**
	 * 路线偏离容忍距离（米）
	 */
	@Schema(description = "路线偏离容忍距离（米）")
	@Excel(name = "路线偏离容忍距离（米）")
	private Double routeTolerance;
	/**
	 * 是否启用报警：0-禁用 1-启用
	 */
	@Schema(description = "是否启用报警：0-禁用 1-启用")
	@Excel(name = "是否启用报警：0-禁用 1-启用")
	private Integer alarmEnabled;
	/**
	 * 报警类型：ENTER-进入 LEAVE-离开 BOTH-进出都报警
	 */
	@Schema(description = "报警类型：ENTER-进入 LEAVE-离开 BOTH-进出都报警")
	@Excel(name = "报警类型：ENTER-进入 LEAVE-离开 BOTH-进出都报警")
	private String alarmType;
	/**
	 * 报警等级
	 */
	@Schema(description = "报警等级")
	@Excel(name = "报警等级")
	private String alarmLevel;
	/**
	 * 报警消息模板
	 */
	@Schema(description = "报警消息模板")
	@Excel(name = "报警消息模板")
	private String alarmMessage;
	/**
	 * 是否启用时间限制：0-全天 1-定时
	 */
	@Schema(description = "是否启用时间限制：0-全天 1-定时")
	@Excel(name = "是否启用时间限制：0-全天 1-定时")
	private Integer timeEnabled;
	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	@Excel(name = "开始时间")
	private String startTime;
	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	@Excel(name = "结束时间")
	private String endTime;
	/**
	 * 是否启用：0-禁用 1-启用
	 */
	@Schema(description = "是否启用：0-禁用 1-启用")
	@Excel(name = "是否启用：0-禁用 1-启用")
	private Integer isActive;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;

	
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
}