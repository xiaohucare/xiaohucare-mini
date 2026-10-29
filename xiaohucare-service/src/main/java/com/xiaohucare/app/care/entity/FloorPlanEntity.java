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
 * 康养楼层表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_floor_plan")
public class FloorPlanEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 楼栋ID（e_care_building.id）
	 */
	@Schema(description = "楼栋ID")
	@Excel(name = "楼栋ID")
	private Long buildingId;
	/**
	 * 楼层号（地下层用负数）
	 */
	@Schema(description = "楼层号")
	@Excel(name = "楼层号")
	private Integer floorNo;
	/**
	 * 楼层名称（如：1F/护理一层）
	 */
	@Schema(description = "楼层名称")
	@Excel(name = "楼层名称")
	private String floorName;
	/**
	 * 楼层平面图（JSON数组）
	 */
	@Schema(description = "楼层平面图（JSON数组）")
	@Excel(name = "楼层平面图")
	private String planImages;
	/**
	 * 主要朝向（南/北/东南等）
	 */
	@Schema(description = "主要朝向")
	@Excel(name = "主要朝向")
	private String orientation;
	/**
	 * 是否有电梯：0-无 1-有
	 */
	@Schema(description = "是否有电梯：0-无 1-有")
	@Excel(name = "是否有电梯")
	private Integer hasElevator;
	/**
	 * 楼层面积（㎡）
	 */
	@Schema(description = "楼层面积（㎡）")
	@Excel(name = "楼层面积")
	private java.math.BigDecimal area;
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
