package com.xiaohucare.app.ledger.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;

/**
 * 探访/巡访计划配置 Mapper
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Mapper
public interface LedgerVisitPlanMapper extends BaseMapper<LedgerVisitPlanEntity> {
}
