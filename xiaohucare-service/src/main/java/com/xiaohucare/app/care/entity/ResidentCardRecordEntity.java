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
 * 会员卡交易流水表
 * 
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_resident_card_record")
public class ResidentCardRecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 交易流水号，唯一，如：TRD20240315001
	 */
	@Schema(description = "交易流水号，唯一，如：TRD20240315001")
	@Excel(name = "交易流水号，唯一，如：TRD20240315001")
	private String tradeNo;
	/**
	 * 会员卡ID，关联 member_card.id
	 */
	@Schema(description = "会员卡ID，关联 member_card.id")
	@Excel(name = "会员卡ID，关联 member_card.id")
	private Long cardId;
	/**
	 * 会员卡号（冗余）
	 */
	@Schema(description = "会员卡号（冗余）")
	@Excel(name = "会员卡号（冗余）")
	private String cardNo;
	/**
	 * 长者ID（冗余）
	 */
	@Schema(description = "长者ID（冗余）")
	@Excel(name = "长者ID（冗余）")
	private Long residentId;
	/**
	 * 长者姓名（冗余）
	 */
	@Schema(description = "长者姓名（冗余）")
	@Excel(name = "长者姓名（冗余）")
	private String residentName;
	/**
	 * 交易类型：consume-消费 recharge-充值 refund-退款 open-开卡
	 */
	@Schema(description = "交易类型：consume-消费 recharge-充值 refund-退款 open-开卡")
	@Excel(name = "交易类型：consume-消费 recharge-充值 refund-退款 open-开卡")
	private String tradeType;
	/**
	 * 交易金额（充值/消费的正向金额）
	 */
	@Schema(description = "交易金额（充值/消费的正向金额）")
	@Excel(name = "交易金额（充值/消费的正向金额）")
	private BigDecimal amount;
	/**
	 * 本次赠送金额（充值赠送入账）
	 */
	@Schema(description = "本次赠送金额（充值赠送入账）")
	@Excel(name = "本次赠送金额（充值赠送入账）")
	private BigDecimal giftAmount;
	/**
	 * 现金余额（交易前）
	 */
	@Schema(description = "现金余额（交易前）")
	@Excel(name = "现金余额（交易前）")
	private BigDecimal beforeBalance;
	/**
	 * 现金余额（交易后）
	 */
	@Schema(description = "现金余额（交易后）")
	@Excel(name = "现金余额（交易后）")
	private BigDecimal afterBalance;
	/**
	 * 赠送余额（交易前）
	 */
	@Schema(description = "赠送余额（交易前）")
	@Excel(name = "赠送余额（交易前）")
	private BigDecimal beforeGift;
	/**
	 * 赠送余额（交易后）
	 */
	@Schema(description = "赠送余额（交易后）")
	@Excel(name = "赠送余额（交易后）")
	private BigDecimal afterGift;
	/**
	 * 服务类型：care-住养 care_svc-照护服务（消费时填写）
	 */
	@Schema(description = "服务类型：care-住养 care_svc-照护服务（消费时填写）")
	@Excel(name = "服务类型：care-住养 care_svc-照护服务（消费时填写）")
	private String serviceType;
	/**
	 * 关联登记单ID（康养消费时填写）
	 */
	@Schema(description = "关联登记单ID（康养消费时填写）")
	@Excel(name = "关联登记单ID（康养消费时填写）")
	private Long checkinId;
	/**
	 * 关联结算单ID
	 */
	@Schema(description = "关联结算单ID")
	@Excel(name = "关联结算单ID")
	private Long settlementId;
	/**
	 * 是否使用了赠送余额：0-否 1-是
	 */
	@Schema(description = "是否使用了赠送余额：0-否 1-是")
	@Excel(name = "是否使用了赠送余额：0-否 1-是")
	private Integer useGift;
	/**
	 * 支付方式：wechat alipay cash card（充值时填写）
	 */
	@Schema(description = "支付方式：wechat alipay cash card（充值时填写）")
	@Excel(name = "支付方式：wechat alipay cash card（充值时填写）")
	private String payMethod;
	/**
	 * 交易时间
	 */
	@Schema(description = "交易时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date tradeTime;
	/**
	 * 操作员姓名
	 */
	@Schema(description = "操作员姓名")
	@Excel(name = "操作员姓名")
	private String operator;
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