package com.xiaohucare.app.ledger.service;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;

/**
 * 巡访工单自动化 Service。
 *
 * <p>职责：按启用的巡访计划(plan_type=2)扫描匹配老人，幂等生成巡访工单；推进计划下次执行时间；
 * 生成前自动完成机构可自定义字典初始化。</p>
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public interface LedgerPatrolAutoService {

	/**
	 * 扫描全部到期且启用的巡访计划并生成工单。
	 *
	 * @return 本次生成的工单总数
	 */
	int generateDuePlans();

	/**
	 * 按单个巡访计划生成工单(手动触发或定时任务调用)。
	 *
	 * @param plan 计划配置(plan_type=2)
	 * @return 本计划生成的工单数
	 */
	int generateByPlan(LedgerVisitPlanEntity plan);
}
