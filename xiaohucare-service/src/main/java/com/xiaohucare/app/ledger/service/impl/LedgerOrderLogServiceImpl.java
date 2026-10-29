package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohucare.app.ledger.entity.LedgerOrderLogEntity;
import com.xiaohucare.app.ledger.mapper.LedgerOrderLogMapper;
import com.xiaohucare.app.ledger.service.LedgerOrderLogService;

/**
 * 工单流转日志 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerOrderLogServiceImpl extends ServiceImpl<LedgerOrderLogMapper, LedgerOrderLogEntity>
		implements LedgerOrderLogService {
}
