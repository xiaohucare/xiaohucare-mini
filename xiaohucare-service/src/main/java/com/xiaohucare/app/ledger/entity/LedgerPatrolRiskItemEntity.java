package com.xiaohucare.app.ledger.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡访隐患明细
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_patrol_risk_item")
public class LedgerPatrolRiskItemEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "主键")
	private Long id;
	@Schema(description = "巡访工单ID")
	private Long orderId;
	@Schema(description = "隐患项编码(字典ledger_risk_item)")
	private String riskCode;
	@Schema(description = "隐患详细描述")
	private String riskDesc;
	@Schema(description = "隐患等级：1一般 2关注 3高风险")
	private Integer riskLevel;
	@Schema(description = "整改建议")
	private String handleSuggest;
	@Schema(description = "隐患现场照片URL集(逗号分隔)")
	private String itemImages;
	@Schema(description = "是否已整改：0否 1是")
	private Integer isFixed;
	@Schema(description = "整改说明")
	private String fixDesc;
	@Schema(description = "整改后照片URL集(逗号分隔)")
	private String fixImages;
	@Schema(description = "整改人(登录账号/姓名)")
	private String fixBy;
	@Schema(description = "整改完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fixTime;
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;
}
