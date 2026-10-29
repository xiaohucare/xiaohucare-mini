package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.ledger.entity.LedgerVisitRecordEntity;
import com.xiaohucare.app.ledger.mapper.LedgerVisitRecordMapper;
import com.xiaohucare.app.ledger.service.LedgerVisitRecordService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 上门探访记录 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerVisitRecordServiceImpl extends BaseServiceImpl<LedgerVisitRecordMapper, LedgerVisitRecordEntity>
		implements LedgerVisitRecordService {
}
