package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 探访工单 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerVisitOrderVO对象")
public class LedgerVisitOrderVO extends LedgerVisitOrderEntity {
	private static final long serialVersionUID = 1L;
}
