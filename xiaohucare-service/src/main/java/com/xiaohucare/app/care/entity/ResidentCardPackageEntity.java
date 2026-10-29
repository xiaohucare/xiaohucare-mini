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
 * 会员卡套餐表
 * 
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_resident_card_package")
public class ResidentCardPackageEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 套餐名称，如：金卡充值套餐A
	 */
	@Schema(description = "套餐名称，如：金卡充值套餐A")
	@Excel(name = "套餐名称，如：金卡充值套餐A")
	private String name;
	/**
	 * 会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡
	 */
	@Schema(description = "会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡")
	@Excel(name = "会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡")
	private Integer cardLevel;
	/**
	 * 原始金额（元）
	 */
	@Schema(description = "原始金额（元）")
	@Excel(name = "原始金额（元）")
	private BigDecimal originalAmount;
	/**
	 * 优惠金额（元）
	 */
	@Schema(description = "优惠金额（元）")
	@Excel(name = "优惠金额（元）")
	private BigDecimal discountAmount;
	/**
	 * 实际收费金额 = original - discount
	 */
	@Schema(description = "实际收费金额 = original - discount")
	@Excel(name = "实际收费金额 = original - discount")
	private BigDecimal actualAmount;
	/**
	 * 赠送金额（入账到赠送余额）
	 */
	@Schema(description = "赠送金额（入账到赠送余额）")
	@Excel(name = "赠送金额（入账到赠送余额）")
	private BigDecimal giftAmount;
	/**
	 * 套餐说明，如：有效期12个月、不可退换等
	 */
	@Schema(description = "套餐说明，如：有效期12个月、不可退换等")
	@Excel(name = "套餐说明，如：有效期12个月、不可退换等")
	private String description;
	/**
	 * 是否热销推荐：0-否 1-是
	 */
	@Schema(description = "是否热销推荐：0-否 1-是")
	@Excel(name = "是否热销推荐：0-否 1-是")
	private Integer isHot;
	/**
	 * 状态：0-禁用 1-启用
	 */
	@Schema(description = "状态：0-禁用 1-启用")
	@Excel(name = "状态：0-禁用 1-启用")
	private Integer status;
	/**
	 * 排序（升序）
	 */
	@Schema(description = "排序（升序）")
	@Excel(name = "排序（升序）")
	private Integer sortOrder;
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