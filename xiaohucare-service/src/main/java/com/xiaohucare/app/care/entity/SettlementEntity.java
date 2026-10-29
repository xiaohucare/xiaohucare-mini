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
 * 结算表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_settlement")
public class SettlementEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 结算单号
	 */
	@Schema(description = "结算单号")
	@Excel(name = "结算单号")
	private String settlementNo;
	/**
	 * 登记ID
	 */
	@Schema(description = "登记ID")
	@Excel(name = "登记ID")
	private Long checkinId;
	/**
	 * 主人ID
	 */
	@Schema(description = "长者ID")
	@Excel(name = "长者ID")
	private Long residentId;
	/**
	 * 床位费用
	 */
	@Schema(description = "床位费用")
	@Excel(name = "床位费用")
	private BigDecimal bedFee;
	/**
	 * 护理费用
	 */
	@Schema(description = "护理费用")
	@Excel(name = "护理费用")
	private BigDecimal careFee;
	/**
	 * 餐费费用
	 */
	@Schema(description = "餐费费用")
	@Excel(name = "餐费费用")
	private BigDecimal mealFee;
	/**
	 * 增值服务费用
	 */
	@Schema(description = "增值服务费用")
	@Excel(name = "增值服务费用")
	private BigDecimal serviceFee;
	/**
	 * 其他费用
	 */
	@Schema(description = "其他费用")
	@Excel(name = "其他费用")
	private BigDecimal otherFee;
	/**
	 * 费用小计
	 */
	@Schema(description = "费用小计")
	@Excel(name = "费用小计")
	private BigDecimal subtotal;
	/**
	 * 优惠金额
	 */
	@Schema(description = "优惠金额")
	@Excel(name = "优惠金额")
	private BigDecimal discount;
	/**
	 * 费用总计
	 */
	@Schema(description = "费用总计")
	@Excel(name = "费用总计")
	private BigDecimal totalAmount;
	/**
	 * 已付押金
	 */
	@Schema(description = "已付押金")
	@Excel(name = "已付押金")
	private BigDecimal totalDeposit;
	/**
	 * 应收/应退金额
	 */
	@Schema(description = "应收/应退金额")
	@Excel(name = "应收/应退金额")
	private BigDecimal finalAmount;
	/**
	 * 押金状态（0-未支付 1-已支付 2-已退还）
	 */
	@Schema(description = "押金状态（0-未支付 1-已支付 2-已退还）")
	@Excel(name = "押金状态（0-未支付 1-已支付 2-已退还）")
	private Integer depositStatus;
	/**
	 * 押金支付金额
	 */
	@Schema(description = "押金支付金额")
	@Excel(name = "押金支付金额")
	private BigDecimal depositPayment;
	/**
	 * 押金退还时间
	 */
	@Schema(description = "押金退还时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "押金退还时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date depositRefundTime;
	/**
	 * 支付方式（CASH-现金 WECHAT-微信 ALIPAY-支付宝 CARD-银行卡 MEMBER_CARD-会员卡）
	 */
	@Schema(description = "支付方式（CASH-现金 WECHAT-微信 ALIPAY-支付宝 CARD-银行卡 MEMBER_CARD-会员卡）")
	@Excel(name = "支付方式（CASH-现金 WECHAT-微信 ALIPAY-支付宝 CARD-银行卡 MEMBER_CARD-会员卡）")
	private String paymentMethod;
	/**
	 * 实收金额
	 */
	@Schema(description = "实收金额")
	@Excel(name = "实收金额")
	private BigDecimal actualPayment;
	/**
	 * 支付备注
	 */
	@Schema(description = "支付备注")
	@Excel(name = "支付备注")
	private String paymentRemark;
	/**
	 * 状态（0-待结算 1-已结算 2-已退款）
	 */
	@Schema(description = "状态（0-待结算 1-已结算 2-已退款）")
	@Excel(name = "状态（0-待结算 1-已结算 2-已退款）")
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