package com.xiaohucare.app.ledger.dto;

import com.xiaohucare.app.ledger.entity.LedgerVisitRecordEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上门探访记录 数据传输对象
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LedgerVisitRecordDTO extends LedgerVisitRecordEntity {
	private static final long serialVersionUID = 1L;
}
