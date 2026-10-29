package com.xiaohucare.app.ledger.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.ledger.entity.LedgerPlanLogEntity;

/**
 * 计划生成工单执行日志 Mapper
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Mapper
public interface LedgerPlanLogMapper extends BaseMapper<LedgerPlanLogEntity> {
}
