package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.ledger.entity.LedgerPatrolRiskItemEntity;
import com.xiaohucare.app.ledger.mapper.LedgerPatrolRiskItemMapper;
import com.xiaohucare.app.ledger.service.LedgerPatrolRiskItemService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 巡访隐患明细 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerPatrolRiskItemServiceImpl
		extends BaseServiceImpl<LedgerPatrolRiskItemMapper, LedgerPatrolRiskItemEntity>
		implements LedgerPatrolRiskItemService {
}
