package com.xiaohucare.app.ledger.dto;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 探访/巡访计划配置 数据传输对象
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LedgerVisitPlanDTO extends LedgerVisitPlanEntity {
	private static final long serialVersionUID = 1L;
}
