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
 * 套餐费用项关联表
 * 
 * @author xiaohucare
 * @date 2026-03-21 13:47:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_combo_fee_item")
public class ComboFeeItemEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 套餐ID
	 */
	@Schema(description = "套餐ID")
	@Excel(name = "套餐ID")
	private Long comboId;
	/**
	 * 费用项ID
	 */
	@Schema(description = "费用项ID")
	@Excel(name = "费用项ID")
	private Long itemId;
	/**
	 * 数量
	 */
	@Schema(description = "数量")
	@Excel(name = "数量")
	private Integer quantity;
	/**
	 * 是否必选（0-可选 1-必选）
	 */
	@Schema(description = "是否必选（0-可选 1-必选）")
	@Excel(name = "是否必选（0-可选 1-必选）")
	private Integer mandatory;
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
	private Integer isDelete;
}