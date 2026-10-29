package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerRevisitEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工单回访记录 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerRevisitVO对象")
public class LedgerRevisitVO extends LedgerRevisitEntity {
	private static final long serialVersionUID = 1L;
}
