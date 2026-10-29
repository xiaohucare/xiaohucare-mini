package com.xiaohucare.app.ledger.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaohucare.app.ledger.service.LedgerPatrolAutoService;

/**
 * 巡访工单自动生成定时任务。
 *
 * <p>每 30 分钟扫描一次到期且启用的巡访计划(plan_type=2)并按规则生成工单；
 * 需在启动类开启 {@code @EnableScheduling}（xiaohucare-platform 已开启）。</p>
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Component
public class LedgerPatrolAutoTask {

	private static final Logger log = LoggerFactory.getLogger(LedgerPatrolAutoTask.class);

	@Autowired
	private LedgerPatrolAutoService ledgerPatrolAutoService;

	/**
	 * 每 30 分钟执行一次巡访工单自动生成(错开探访任务 15 分钟)。
	 */
	@Scheduled(cron = "0 15/30 * * * ?")
	public void autoGeneratePatrolOrders() {
		try {
			int count = ledgerPatrolAutoService.generateDuePlans();
			if (count > 0) {
				log.info("[巡访工单自动化] 本次生成工单 {} 张", count);
			}
		} catch (Exception e) {
			log.error("[巡访工单自动化] 执行异常", e);
		}
	}
}
