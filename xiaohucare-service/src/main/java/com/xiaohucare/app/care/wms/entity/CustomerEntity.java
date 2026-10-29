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
 * 客户表（商品购买客户）
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_customer")
public class CustomerEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 0 客户 1 供应商
	 */
	private Integer kind;
	/**
	 * 客户姓名
	 */
	@Schema(description = "客户姓名")
	@Excel(name = "客户姓名")
	private String customerName;
	/**
	 * 联系电话
	 */
	@Schema(description = "联系电话")
	@Excel(name = "联系电话")
	private String customerPhone;
	/**
	 * 客户类型（WALK_IN-散客 MEMBER-会员 e_GUARDIAN-家属）
	 */
	@Schema(description = "客户类型（WALK_IN-散客 MEMBER-会员 e_GUARDIAN-家属）")
	@Excel(name = "客户类型（WALK_IN-散客 MEMBER-会员 e_GUARDIAN-家属）")
	private String customerType;
	/**
	 * 关联家属ID（如果客户是在院长者家属）
	 */
	@Schema(description = "关联家属ID（如果客户是在院长者家属）")
	@Excel(name = "关联家属ID（如果客户是在院长者家属）")
	private Long guardianId;
	/**
	 * 会员等级
	 */
	@Schema(description = "会员等级")
	@Excel(name = "会员等级")
	private String memberLevel;
	/**
	 * 会员余额
	 */
	@Schema(description = "会员余额")
	@Excel(name = "会员余额")
	private BigDecimal memberBalance;
	/**
	 * 累计消费金额
	 */
	@Schema(description = "累计消费金额")
	@Excel(name = "累计消费金额")
	private BigDecimal totalConsumption;
	/**
	 * 累计订单数
	 */
	@Schema(description = "累计订单数")
	@Excel(name = "累计订单数")
	private Integer totalOrders;
	/**
	 * 最后购买日期
	 */
	@Schema(description = "最后购买日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "最后购买日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date lastPurchaseDate;
	/**
	 * 地址
	 */
	@Schema(description = "地址")
	@Excel(name = "地址")
	private String address;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
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