package com.xiaohucare.app.ledger.vo;

import com.xiaohucare.app.ledger.entity.LedgerElderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 智慧台账-老人档案 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "LedgerElderVO对象")
public class LedgerElderVO extends LedgerElderEntity {
	private static final long serialVersionUID = 1L;
}
