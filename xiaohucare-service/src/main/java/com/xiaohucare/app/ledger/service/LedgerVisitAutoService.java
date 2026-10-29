package com.xiaohucare.app.ledger.service;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;

/**
 * 探访工单自动化 Service。
 *
 * <p>职责：按启用的探访计划扫描匹配老人，幂等生成探访工单；推进计划下次执行时间；
 * 生成前自动完成机构可自定义字典初始化。</p>
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public interface LedgerVisitAutoService {

	/**
	 * 扫描全部到期且启用的探访计划并生成工单。
	 *
	 * @return 本次生成的工单总数
	 */
	int generateDuePlans();

	/**
	 * 按单个计划生成探访工单(手动触发或定时任务调用)。
	 *
	 * @param plan 计划配置
	 * @return 本计划生成的工单数
	 */
	int generateByPlan(LedgerVisitPlanEntity plan);
}
