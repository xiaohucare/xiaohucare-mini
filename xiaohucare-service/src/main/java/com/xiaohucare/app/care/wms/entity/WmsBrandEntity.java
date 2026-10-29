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
 * 商品品牌表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_brand")
public class WmsBrandEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	@Excel(name = "品牌名称")
	private String brandName;
	/**
	 * 品牌编码
	 */
	@Schema(description = "品牌编码")
	@Excel(name = "品牌编码")
	private String brandCode;
	/**
	 * 品牌Logo URL
	 */
	@Schema(description = "品牌Logo URL")
	@Excel(name = "品牌Logo URL")
	private String brandLogo;
	/**
	 * 品牌国家
	 */
	@Schema(description = "品牌国家")
	@Excel(name = "品牌国家")
	private String country;
	/**
	 * 品牌描述
	 */
	@Schema(description = "品牌描述")
	@Excel(name = "品牌描述")
	private String description;
	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@Excel(name = "排序")
	private Integer sortOrder;
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