package com.xiaohucare.app.care.wms.entity;

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
 * 出货明细表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_sales_item")
public class SalesItemEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 出货单ID
	 */
	@Schema(description = "出货单ID")
	@Excel(name = "出货单ID")
	private Long orderId;
	/**
	 * SKU ID
	 */
	@Schema(description = "SKU ID")
	@Excel(name = "SKU ID")
	private Long skuId;
	/**
	 * 出货数量
	 */
	@Schema(description = "出货数量")
	@Excel(name = "出货数量")
	private Integer quantity;
	/**
	 * 销售单价
	 */
	@Schema(description = "销售单价")
	@Excel(name = "销售单价")
	private BigDecimal salePrice;
	/**
	 * 小计
	 */
	@Schema(description = "小计")
	@Excel(name = "小计")
	private BigDecimal subtotal;
	/**
	 * 出库批次ID
	 */
	@Schema(description = "出库批次ID")
	@Excel(name = "出库批次ID")
	private Long batchId;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
}