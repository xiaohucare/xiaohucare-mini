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
 * 进货单表
 * 
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_purchase_order")
public class PurchaseOrderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 进货单号
	 */
	@Schema(description = "进货单号")
	@Excel(name = "进货单号")
	private String orderNo;
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
	/**
	 * 总金额
	 */
	@Schema(description = "总金额")
	@Excel(name = "总金额")
	private BigDecimal totalAmount;

	private Integer totalCount;
	private Integer totalSkuCount;

	/**
	 * 进货日期
	 */
	@Schema(description = "进货日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "进货日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date purchaseDate;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
	/**
	 * 状态（0-草稿 1-已入库）
	 */
	@Schema(description = "状态（0-草稿 1-已入库）")
	@Excel(name = "状态（0-草稿 1-已入库）")
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