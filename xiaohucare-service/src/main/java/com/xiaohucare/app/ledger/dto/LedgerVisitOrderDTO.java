package com.xiaohucare.app.ledger.dto;

import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 探访工单 数据传输对象
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LedgerVisitOrderDTO extends LedgerVisitOrderEntity {
	private static final long serialVersionUID = 1L;
}
