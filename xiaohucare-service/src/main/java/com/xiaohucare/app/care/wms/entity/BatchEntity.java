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
 * 库存批次表
 * 
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_batch")
public class BatchEntity extends BaseEntity {
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
	 * 批次号
	 */
	@Schema(description = "批次号")
	@Excel(name = "批次号")
	private String batchNo;
	/**
	 * 进货单ID
	 */
	@Schema(description = "进货单ID")
	@Excel(name = "进货单ID")
	private Long purchaseOrderId;
	/**
	 * 当前库存数量
	 */
	@Schema(description = "当前库存数量")
	@Excel(name = "当前库存数量")
	private Integer stockQuantity;
	/**
	 * 初始入库数量
	 */
	@Schema(description = "初始入库数量")
	@Excel(name = "初始入库数量")
	private Integer initialQuantity;
	/**
	 * 进货价（该批次）
	 */
	@Schema(description = "进货价（该批次）")
	@Excel(name = "进货价（该批次）")
	private BigDecimal purchasePrice;
	/**
	 * 进货日期
	 */
	@Schema(description = "进货日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "进货日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date purchaseDate;
	/**
	 * 生产日期
	 */
	@Schema(description = "生产日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date productionDate;
	/**
	 * 有效期
	 */
	@Schema(description = "有效期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "有效期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date expiryDate;
	/**
	 * 供应商
	 */
	@Schema(description = "供应商")
	@Excel(name = "供应商")
	private String supplier;
	/**
	 * 仓库位置
	 */
	@Schema(description = "仓库位置")
	@Excel(name = "仓库位置")
	private String warehouseLocation;
	/**
	 * 状态（0-已耗尽 1-在库 2-已过期）
	 */
	@Schema(description = "状态（0-已耗尽 1-在库 2-已过期）")
	@Excel(name = "状态（0-已耗尽 1-在库 2-已过期）")
	private Integer status;
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
	 * 客户ID
	 */
	@Schema(description = "供应商ID")
	@Excel(name = "供应商ID")
	private Long customerId;
	/**
	 * 客户姓名
	 */
	@Schema(description = "供应商")
	@Excel(name = "供应商")
	private String customerName;
}