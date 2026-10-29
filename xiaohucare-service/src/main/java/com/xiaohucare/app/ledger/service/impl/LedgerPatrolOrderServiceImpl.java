package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;
import com.xiaohucare.app.ledger.mapper.LedgerPatrolOrderMapper;
import com.xiaohucare.app.ledger.service.LedgerPatrolOrderService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 巡访工单 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerPatrolOrderServiceImpl extends BaseServiceImpl<LedgerPatrolOrderMapper, LedgerPatrolOrderEntity>
		implements LedgerPatrolOrderService {
}
