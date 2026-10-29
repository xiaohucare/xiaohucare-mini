package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerVisitRecordEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上门探访记录 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerVisitRecordVO对象")
public class LedgerVisitRecordVO extends LedgerVisitRecordEntity {
	private static final long serialVersionUID = 1L;
}
