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
 * 商品分类表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_category")
public class WmsCategoryEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 父分类ID
	 */
	@Schema(description = "父分类ID")
	@Excel(name = "父分类ID")
	private Long parentId;
	/**
	 * 分类名称
	 */
	@Schema(description = "分类名称")
	@Excel(name = "分类名称")
	private String categoryName;
	/**
	 * 分类编码
	 */
	@Schema(description = "分类编码")
	@Excel(name = "分类编码")
	private String categoryCode;
	/**
	 * 图标URL
	 */
	@Schema(description = "图标URL")
	@Excel(name = "图标URL")
	private String icon;
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
	/**
	 * 删除标志
	 */
	@Schema(description = "删除标志")
	@Excel(name = "删除标志")
	private Integer isDelete;
}
