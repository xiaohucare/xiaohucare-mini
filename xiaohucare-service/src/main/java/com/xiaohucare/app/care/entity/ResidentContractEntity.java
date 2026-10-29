package com.xiaohucare.app.care.entity;

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
 * 合同记录（含模板行）
 *
 * 说明：合同与模板共用 e_care_resident_contract，
 * 模板行 contract_type=100 且 contract_no 以 TP- 开头，resident_id 为空。
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_resident_contract")
public class ResidentContractEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 模板合同类型值 */
	public static final int TYPE_TEMPLATE = 100;
	/** 合同编号前缀：定制合同 */
	public static final String NO_PREFIX_CONTRACT = "HT";
	/** 合同编号前缀：模板 */
	public static final String NO_PREFIX_TEMPLATE = "TP";
	/** 平台通用模板的机构ID约定值 */
	public static final long STORE_ID_COMMON = 0L;

	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 合同编号
	 */
	@Schema(description = "合同编号")
	@Excel(name = "合同编号")
	private String contractNo;
	/**
	 * 合同类型 0 企业通用合同 1 定制合同 100 模版合同
	 */
	@Schema(description = "合同类型 0 企业通用合同 1 定制合同 100 模版合同")
	@Excel(name = "合同类型")
	private Integer contractType;
	/**
	 * 住户ID
	 */
	@Schema(description = "住户ID")
	@Excel(name = "住户ID")
	private Long residentId;
	/**
	 * 住户名称
	 */
	@Schema(description = "住户名称")
	@Excel(name = "住户名称")
	private String residentName;
	/**
	 * 住户编号
	 */
	@Schema(description = "住户编号")
	@Excel(name = "住户编号")
	private String residentNo;
	/**
	 * 模板ID
	 */
	@Schema(description = "模板ID")
	@Excel(name = "模板ID")
	private Long templateId;
	/**
	 * 协议名称（模板名称）
	 */
	@Schema(description = "协议名称")
	@Excel(name = "协议名称")
	private String templateName;
	/**
	 * 签署日期
	 */
	@Schema(description = "签署日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "签署日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date signDate;
	/**
	 * 生效日期
	 */
	@Schema(description = "生效日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date effectiveDate;
	/**
	 * 到期日期
	 */
	@Schema(description = "到期日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "到期日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date expiryDate;
	/**
	 * 签署合同（PDF地址）；模板行此列存放的是空白模板 PDF 文件
	 */
	@Schema(description = "签署合同（PDF地址），模板行为模板PDF文件")
	@Excel(name = "签署合同")
	private String signPdfurl;
	/**
	 * 状态 0 待签署 1 已签署 2 已完成 3 已作废
	 */
	@Schema(description = "状态 0 待签署 1 已签署 2 已完成 3 已作废")
	@Excel(name = "状态")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
	/**
	 * 签署年份
	 */
	@Schema(description = "签署年份")
	@Excel(name = "签署年份")
	private String signYear;
	/**
	 * 签署类型
	 */
	@Schema(description = "签署类型")
	@Excel(name = "签署类型")
	private Integer signType;

}
