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
 * 商品SKU表
 * 
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_sku")
public class SkuEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	@Excel(name = "商品ID")
	private Long productId;
	/**
	 * 分类ID（冗余字段，用于快速筛选）
	 */
	@Schema(description = "分类ID（冗余字段，用于快速筛选）")
	@Excel(name = "分类ID")
	private Long categoryId;
	/**
	 * SKU编码
	 */
	@Schema(description = "SKU编码")
	@Excel(name = "SKU编码")
	private String skuCode;
	/**
	 * SKU名称（如：护理垫-品牌A-100片）
	 */
	@Schema(description = "SKU名称（如：护理垫-品牌A-100片）")
	@Excel(name = "SKU名称（如：护理垫-品牌A-100片）")
	private String skuName;
	/**
	 * SKU图片URL
	 */
	@Schema(description = "SKU图片URL")
	@Excel(name = "SKU图片URL")
	private String productImage;
	/**
	 * 规格属性JSON（如：{"品牌":"品牌A","重量":"5kg"}）
	 */
	private String specifications;
	/**
	 * 进货价
	 */
	@Schema(description = "进货价")
	@Excel(name = "进货价")
	private BigDecimal purchasePrice;
	/**
	 * 销售价
	 */
	@Schema(description = "销售价")
	@Excel(name = "销售价")
	private BigDecimal salePrice;
	/**
	 * 市场价
	 */
	@Schema(description = "市场价")
	@Excel(name = "市场价")
	private BigDecimal marketPrice;
	/**
	 * 总库存（所有批次合计）
	 */
	@Schema(description = "总库存（所有批次合计）")
	@Excel(name = "总库存（所有批次合计）")
	private Integer totalStock;
	/**
	 * 预警库存
	 */
	@Schema(description = "预警库存")
	@Excel(name = "预警库存")
	private Integer warningStock;
	/**
	 * 状态（0-禁用 1-启用）
	 */
	@Schema(description = "状态（0-禁用 1-启用）")
	@Excel(name = "状态（0-禁用 1-启用）")
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

}