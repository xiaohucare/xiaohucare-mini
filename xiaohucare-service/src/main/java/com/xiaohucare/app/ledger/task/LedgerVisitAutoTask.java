package com.xiaohucare.app.ledger.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaohucare.app.ledger.service.LedgerVisitAutoService;

/**
 * 探访工单自动生成定时任务。
 *
 * <p>每 30 分钟扫描一次到期且启用的探访计划并按规则生成工单；
 * 需在启动类开启 {@code @EnableScheduling}（xiaohucare-platform 已开启）。</p>
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Component
public class LedgerVisitAutoTask {

	private static final Logger log = LoggerFactory.getLogger(LedgerVisitAutoTask.class);

	@Autowired
	private LedgerVisitAutoService ledgerVisitAutoService;

	/**
	 * 每 30 分钟执行一次探访工单自动生成。
	 */
	@Scheduled(cron = "0 0/30 * * * ?")
	public void autoGenerateVisitOrders() {
		try {
			int count = ledgerVisitAutoService.generateDuePlans();
			if (count > 0) {
				log.info("[探访工单自动化] 本次生成工单 {} 张", count);
			}
		} catch (Exception e) {
			log.error("[探访工单自动化] 执行异常", e);
		}
	}
}
