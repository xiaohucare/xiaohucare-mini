package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡访工单 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerPatrolOrderVO对象")
public class LedgerPatrolOrderVO extends LedgerPatrolOrderEntity {
	private static final long serialVersionUID = 1L;

	@Schema(description = "隐患项总数(列表/详情聚合，非表字段)")
	private Integer riskItemCount;
	@Schema(description = "已整改隐患数(列表/详情聚合，非表字段)")
	private Integer riskFixedCount;
}
