package com.xiaohucare.app.ledger.dto;

import com.xiaohucare.app.ledger.entity.LedgerElderEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 智慧台账-老人档案 数据传输对象
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LedgerElderDTO extends LedgerElderEntity {
	private static final long serialVersionUID = 1L;
}
