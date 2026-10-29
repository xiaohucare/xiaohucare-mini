package com.xiaohucare.app.ledger.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.ledger.entity.LedgerOrderLogEntity;

/**
 * 工单流转日志 Mapper
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Mapper
public interface LedgerOrderLogMapper extends BaseMapper<LedgerOrderLogEntity> {
}
