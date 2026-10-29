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
 * 结算明细表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_settlement_detail")
public class SettlementDetailEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 结算ID
	 */
	@Schema(description = "结算ID")
	@Excel(name = "结算ID")
	private Long settlementId;
	/**
	 * 登记宠物ID
	 */
	@Schema(description = "入住登记ID")
	@Excel(name = "入住登记ID")
	private Long checkinId;
	/**
	 * 宠物昵称
	 */
	@Schema(description = "宠物昵称")
	@Excel(name = "宠物昵称")
	private String residentName;
	/**
	 * 费用项ID
	 */
	@Schema(description = "费用项ID")
	@Excel(name = "费用项ID")
	private Long itemId;
	/**
	 * 费用项名称
	 */
	@Schema(description = "费用项名称")
	@Excel(name = "费用项名称")
	private String itemName;
	/**
	 * 费用项类型
	 */
	@Schema(description = "费用项类型")
	@Excel(name = "费用项类型")
	private String itemType;
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
	 * 数量
	 */
	@Schema(description = "数量")
	@Excel(name = "数量")
	private Integer quantity;
	/**
	 * 天数
	 */
	@Schema(description = "天数")
	@Excel(name = "天数")
	private Integer days;
	/**
	 * 小计
	 */
	@Schema(description = "小计")
	@Excel(name = "小计")
	private BigDecimal subtotal;
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

}