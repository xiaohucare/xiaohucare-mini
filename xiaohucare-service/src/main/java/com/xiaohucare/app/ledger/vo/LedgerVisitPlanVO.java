package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 探访/巡访计划配置 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerVisitPlanVO对象")
public class LedgerVisitPlanVO extends LedgerVisitPlanEntity {
	private static final long serialVersionUID = 1L;
}
