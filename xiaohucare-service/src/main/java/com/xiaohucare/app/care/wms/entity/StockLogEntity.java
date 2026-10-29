package com.xiaohucare.app.care.wms.entity;

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
 * 库存变动记录表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_stock_log")
public class StockLogEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * SKU ID
	 */
	@Schema(description = "SKU ID")
	@Excel(name = "SKU ID")
	private Long skuId;
	/**
	 * 批次ID
	 */
	@Schema(description = "批次ID")
	@Excel(name = "批次ID")
	private Long batchId;
	/**
	 * 变动类型（PURCHASE-进货 SALES-出货 RETURN-退货 ADJUST-调整 EXPIRE-过期）
	 */
	@Schema(description = "变动类型（PURCHASE-进货 SALES-出货 RETURN-退货 ADJUST-调整 EXPIRE-过期）")
	@Excel(name = "变动类型（PURCHASE-进货 SALES-出货 RETURN-退货 ADJUST-调整 EXPIRE-过期）")
	private String changeType;
	/**
	 * 变动数量（正数入库，负数出库）
	 */
	@Schema(description = "变动数量（正数入库，负数出库）")
	@Excel(name = "变动数量（正数入库，负数出库）")
	private Integer changeQuantity;
	/**
	 * 变动前库存
	 */
	@Schema(description = "变动前库存")
	@Excel(name = "变动前库存")
	private Integer beforeStock;
	/**
	 * 变动后库存
	 */
	@Schema(description = "变动后库存")
	@Excel(name = "变动后库存")
	private Integer afterStock;
	/**
	 * 关联单号
	 */
	@Schema(description = "关联单号")
	@Excel(name = "关联单号")
	private String relatedOrderNo;
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
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
}