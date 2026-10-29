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
 * 收费类目表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_fee_item")
public class FeeItemEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	@Schema(description = "收费类型：0 酒店收费 1 洗护收费")
	@Excel(name = "收费类型：0 酒店收费 1 洗护收费")
	private Integer chargeKind;
	/**
	 * 项目名称
	 */
	@Schema(description = "项目名称")
	@Excel(name = "项目名称")
	private String itemName;
	/**
	 * 项目类型：0-基础 1-增值 2-餐饮 3-医疗 4-美容 5-赔偿 6-其他
	 */
	@Schema(description = "项目类型：0-基础 1-增值 2-餐饮 3-医疗 4-美容 5-赔偿 6-其他")
	@Excel(name = "项目类型：0-基础 1-增值 2-餐饮 3-医疗 4-美容 5-赔偿 6-其他")
	private Integer itemType;
	/**
	 * 计费方式（0-按天 1-按次 2-固定 3-按重）
	 */
	@Schema(description = "计费方式（0-按天 1-按次 2-固定 3-按重）")
	@Excel(name = "计费方式（0-按天 1-按次 2-固定 3-按重）")
	private Integer chargeMode;
	/**
	 * 单价
	 */
	@Schema(description = "单价")
	@Excel(name = "单价")
	private BigDecimal unitPrice;
	/**
	 * 是否启用（0-禁用 1-启用）
	 */
	@Schema(description = "是否启用（0-禁用 1-启用）")
	@Excel(name = "是否启用（0-禁用 1-启用）")
	private Integer status;
	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@Excel(name = "排序")
	private Integer sortOrder;
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

	/**
	 * 预计时长（分钟）
	 */
	@Schema(description = "预计时长（分钟）")
	@Excel(name = "预计时长（分钟）")
	private Integer duration;
	/**
	 * 服务描述
	 */
	@Schema(description = "服务描述")
	@Excel(name = "服务描述")
	private String description;

}