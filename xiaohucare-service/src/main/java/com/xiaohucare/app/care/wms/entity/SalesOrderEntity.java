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
 * 出货单表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_wms_sales_order")
public class SalesOrderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 出货单号
	 */
	@Schema(description = "出货单号")
	@Excel(name = "出货单号")
	private String orderNo;
	/**
	 * 客户ID
	 */
	@Schema(description = "客户ID")
	@Excel(name = "客户ID")
	private Long customerId;
	/**
	 * 客户姓名
	 */
	@Schema(description = "客户姓名")
	@Excel(name = "客户姓名")
	private String customerName;
	/**
	 * 客户电话
	 */
	@Schema(description = "客户电话")
	@Excel(name = "客户电话")
	private String customerPhone;
	/**
	 * 关联家属ID（如果客户是长者家属）
	 */
	@Schema(description = "关联家属ID（如果客户是长者家属）")
	@Excel(name = "关联家属ID（如果客户是长者家属）")
	private Long guardianId;

	private Integer totalCount;
	/**
	 * 总金额
	 */
	@Schema(description = "总金额")
	@Excel(name = "总金额")
	private BigDecimal totalAmount;
	/**
	 * 优惠金额
	 */
	@Schema(description = "优惠金额")
	@Excel(name = "优惠金额")
	private BigDecimal discountAmount;
	/**
	 * 实收金额
	 */
	@Schema(description = "实收金额")
	@Excel(name = "实收金额")
	private BigDecimal actualAmount;
	/**
	 * 支付方式（CASH/WECHAT/ALIPAY/CARD/MEMBER_CARD）
	 */
	@Schema(description = "支付方式（CASH/WECHAT/ALIPAY/CARD/MEMBER_CARD）")
	@Excel(name = "支付方式（CASH/WECHAT/ALIPAY/CARD/MEMBER_CARD）")
	private String payMethod;
	/**
	 * 支付附件URL（支付凭证截图，多个用逗号分隔）
	 */
	@Schema(description = "支付附件URL（支付凭证截图，多个用逗号分隔）")
	@Excel(name = "支付附件URL")
	private String payAttachment;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
	/**
	 * 状态（0-已取消 1-已完成 2-已退货）
	 */
	@Schema(description = "状态（0-已取消 1-已完成 2-已退货）")
	@Excel(name = "状态（0-已取消 1-已完成 2-已退货）")
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