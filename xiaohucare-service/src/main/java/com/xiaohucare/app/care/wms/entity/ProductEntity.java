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
 * 商品主表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_product")
public class ProductEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	@Excel(name = "商品名称")
	private String productName;
	/**
	 * 品牌ID
	 */
	@Schema(description = "品牌ID")
	@Excel(name = "品牌ID")
	private Long brandId;
	/**
	 * 分类ID
	 */
	@Schema(description = "分类ID")
	@Excel(name = "分类ID")
	private Long categoryId;
	/**
	 * 条形码
	 */
	@Schema(description = "条形码")
	@Excel(name = "条形码")
	private String barcode;
	/**
	 * 单位（个/袋/瓶/盒）
	 */
	@Schema(description = "单位（个/袋/瓶/盒）")
	@Excel(name = "单位（个/袋/瓶/盒）")
	private String unit;
	/**
	 * 商品类型（FOOD-食品 SUPPLY-用品 MEDICINE-药品 TOY-玩具 SNACK-零食）
	 */
	@Schema(description = "商品类型（FOOD-食品 SUPPLY-用品 MEDICINE-药品 TOY-玩具 SNACK-零食）")
	@Excel(name = "商品类型（FOOD-食品 SUPPLY-用品 MEDICINE-药品 TOY-玩具 SNACK-零食）")
	private String productType;
	/**
	 * 市场价
	 */
	@Schema(description = "市场价")
	@Excel(name = "市场价")
	private BigDecimal marketPrice;
	/**
	 * 商品描述
	 */
	@Schema(description = "商品描述")
	@Excel(name = "商品描述")
	private String description;
	/**
	 * 商品图片（多张逗号分隔）
	 */
	@Schema(description = "商品图片（多张逗号分隔）")
	@Excel(name = "商品图片（多张逗号分隔）")
	private String images;
	@Schema(description = "商品图片LOGO")
	@Excel(name = "商品图片LOGO")
	private String logo;
	/**
	 * 状态（0-下架 1-上架）
	 */
	@Schema(description = "状态（0-下架 1-上架）")
	@Excel(name = "状态（0-下架 1-上架）")
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