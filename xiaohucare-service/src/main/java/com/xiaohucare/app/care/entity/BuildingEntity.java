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
 * 康养楼栋表（空间层级：机构→楼栋→楼层→房间→床位）
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_building")
public class BuildingEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 楼栋编号
	 */
	@Schema(description = "楼栋编号")
	@Excel(name = "楼栋编号")
	private String buildingNo;
	/**
	 * 楼栋名称（如：银杏楼/养护区A栋）
	 */
	@Schema(description = "楼栋名称")
	@Excel(name = "楼栋名称")
	private String buildingName;
	/**
	 * 楼栋类型：1-独立楼栋 2-楼层分区
	 */
	@Schema(description = "楼栋类型：1-独立楼栋 2-楼层分区")
	@Excel(name = "楼栋类型")
	private Integer buildingType;
	/**
	 * 总楼层数
	 */
	@Schema(description = "总楼层数")
	@Excel(name = "总楼层数")
	private Integer totalFloors;
	/**
	 * 用途分区（养护区/自理区/康复区/认知照护区）
	 */
	@Schema(description = "用途分区")
	@Excel(name = "用途分区")
	private String usageType;
	/**
	 * 楼栋负责人ID（员工）
	 */
	@Schema(description = "楼栋负责人ID")
	@Excel(name = "楼栋负责人ID")
	private Long managerId;
	/**
	 * 楼栋负责人姓名
	 */
	@Schema(description = "楼栋负责人姓名")
	@Excel(name = "楼栋负责人姓名")
	private String managerName;
	/**
	 * 状态（0-禁用 1-启用）
	 */
	@Schema(description = "状态（0-禁用 1-启用）")
	@Excel(name = "状态")
	private Integer status;
	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@Excel(name = "排序")
	private Integer sortOrder;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
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

}
