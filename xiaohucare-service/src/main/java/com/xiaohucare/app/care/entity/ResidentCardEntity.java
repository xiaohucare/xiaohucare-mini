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
 * 会员卡表
 * 
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_resident_card")
public class ResidentCardEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 会员卡号，唯一，如：VIP20240001
	 */
	@Schema(description = "会员卡号，唯一，如：VIP20240001")
	@Excel(name = "会员卡号，唯一，如：VIP20240001")
	private String cardNo;
	/**
	 * 长者主人ID，关联 e_owner.id
	 */
	@Schema(description = "长者主人ID，关联 e_owner.id")
	@Excel(name = "长者主人ID，关联 e_owner.id")
	private Long residentId;
	/**
	 * 客户姓名（冗余，提升查询性能）
	 */
	@Schema(description = "客户姓名（冗余，提升查询性能）")
	@Excel(name = "客户姓名（冗余，提升查询性能）")
	private String residentName;
	/**
	 * 客户手机号（冗余）
	 */
	@Schema(description = "客户手机号（冗余）")
	@Excel(name = "客户手机号（冗余）")
	private String residentPhone;
	/**
	 * 开卡套餐ID，关联 member_card_package.id
	 */
	@Schema(description = "开卡套餐ID，关联 member_card_package.id")
	@Excel(name = "开卡套餐ID，关联 member_card_package.id")
	private Long packageId;
	/**
	 * 会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡
	 */
	@Schema(description = "会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡")
	@Excel(name = "会员等级: 0-普通卡 1-银卡 2-金卡 3-钻石卡")
	private Integer cardLevel;
	/**
	 * 现金余额（消费/充值变动）
	 */
	@Schema(description = "现金余额（消费/充值变动）")
	@Excel(name = "现金余额（消费/充值变动）")
	private BigDecimal balance;
	/**
	 * 赠送余额（开卡/充值赠送，优先消费）
	 */
	@Schema(description = "赠送余额（开卡/充值赠送，优先消费）")
	@Excel(name = "赠送余额（开卡/充值赠送，优先消费）")
	private BigDecimal giftBalance;
	/**
	 * 累计充值金额
	 */
	@Schema(description = "累计充值金额")
	@Excel(name = "累计充值金额")
	private BigDecimal totalRecharge;
	/**
	 * 累计消费金额
	 */
	@Schema(description = "累计消费金额")
	@Excel(name = "累计消费金额")
	private BigDecimal totalConsume;
	/**
	 * 开卡日期
	 */
	@Schema(description = "开卡日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "开卡日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date openDate;
	/**
	 * 有效期截止日（NULL表示永久）
	 */
	@Schema(description = "有效期截止日（NULL表示永久）")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "有效期截止日（NULL表示永久）", width = 30, dateFormat = "yyyy-MM-dd")
	private Date expireDate;
	/**
	 * 状态：0-注销 1-正常 2-挂失
	 */
	@Schema(description = "状态：0-注销 1-正常 2-挂失")
	@Excel(name = "状态：0-注销 1-正常 2-挂失")
	private Integer status;
	/**
	 * 开卡支付方式：wechat alipay cash card
	 */
	@Schema(description = "开卡支付方式：wechat alipay cash card")
	@Excel(name = "开卡支付方式：wechat alipay cash card")
	private String payMethod;
	/**
	 * 开卡实付金额
	 */
	@Schema(description = "开卡实付金额")
	@Excel(name = "开卡实付金额")
	private BigDecimal openAmount;
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