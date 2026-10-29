package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;
import com.xiaohucare.app.ledger.mapper.LedgerVisitPlanMapper;
import com.xiaohucare.app.ledger.service.LedgerVisitPlanService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 探访/巡访计划配置 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerVisitPlanServiceImpl extends BaseServiceImpl<LedgerVisitPlanMapper, LedgerVisitPlanEntity>
		implements LedgerVisitPlanService {
}
