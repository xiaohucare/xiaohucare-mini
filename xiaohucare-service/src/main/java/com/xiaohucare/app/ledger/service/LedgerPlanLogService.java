package com.xiaohucare.app.ledger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohucare.app.ledger.entity.LedgerPlanLogEntity;

/**
 * 计划生成工单执行日志 Service(追加型日志表,无逻辑删除,直接用 MP IService)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public interface LedgerPlanLogService extends IService<LedgerPlanLogEntity> {
}
