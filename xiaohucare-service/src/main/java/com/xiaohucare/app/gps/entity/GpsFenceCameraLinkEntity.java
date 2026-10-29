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
 * 围栏-摄像头联动配置表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_gps_fence_camera_link")
public class GpsFenceCameraLinkEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 围栏ID（e_gps_electronic_fence.id）
	 */
	@Schema(description = "围栏ID（e_gps_electronic_fence.id）")
	@Excel(name = "围栏ID（e_gps_electronic_fence.id）")
	private Long fenceId;
	/**
	 * 围栏名称
	 */
	@Schema(description = "围栏名称")
	@Excel(name = "围栏名称")
	private String fenceName;
	/**
	 * 摄像头设备ID（e_camera_device.id）
	 */
	@Schema(description = "摄像头设备ID（e_camera_device.id）")
	@Excel(name = "摄像头设备ID（e_camera_device.id）")
	private Long cameraDeviceId;
	/**
	 * 摄像头名称
	 */
	@Schema(description = "摄像头名称")
	@Excel(name = "摄像头名称")
	private String cameraDeviceName;
	/**
	 * 触发动作：SNAPSHOT-截图 RECORD-录像 BOTH-截图+录像
	 */
	@Schema(description = "触发动作：SNAPSHOT-截图 RECORD-录像 BOTH-截图+录像")
	@Excel(name = "触发动作：SNAPSHOT-截图 RECORD-录像 BOTH-截图+录像")
	private String triggerAction;
	/**
	 * 录像时长（秒）
	 */
	@Schema(description = "录像时长（秒）")
	@Excel(name = "录像时长（秒）")
	private Integer recordDuration;
	/**
	 * 截图数量
	 */
	@Schema(description = "截图数量")
	@Excel(name = "截图数量")
	private Integer snapshotCount;
	/**
	 * 覆盖报警等级（为空则使用围栏原等级）
	 */
	@Schema(description = "覆盖报警等级（为空则使用围栏原等级）")
	@Excel(name = "覆盖报警等级（为空则使用围栏原等级）")
	private String alarmLevelOverride;
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

}