package com.xiaohucare.app.ledger.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohucare.app.ledger.entity.LedgerPlanLogEntity;
import com.xiaohucare.app.ledger.mapper.LedgerPlanLogMapper;
import com.xiaohucare.app.ledger.service.LedgerPlanLogService;

/**
 * 计划生成工单执行日志 Service 实现
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerPlanLogServiceImpl extends ServiceImpl<LedgerPlanLogMapper, LedgerPlanLogEntity>
		implements LedgerPlanLogService {
}
