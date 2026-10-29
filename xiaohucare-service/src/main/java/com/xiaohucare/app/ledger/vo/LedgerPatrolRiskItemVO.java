package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerPatrolRiskItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡访隐患明细 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerPatrolRiskItemVO对象")
public class LedgerPatrolRiskItemVO extends LedgerPatrolRiskItemEntity {
	private static final long serialVersionUID = 1L;
}
