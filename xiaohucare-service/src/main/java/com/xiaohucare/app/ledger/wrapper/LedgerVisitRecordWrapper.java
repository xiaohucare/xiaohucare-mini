package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerVisitRecordEntity;
import com.xiaohucare.app.ledger.vo.LedgerVisitRecordVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 上门探访记录 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerVisitRecordWrapper extends BaseEntityWrapper<LedgerVisitRecordEntity, LedgerVisitRecordVO> {

	public static LedgerVisitRecordWrapper build() {
		return new LedgerVisitRecordWrapper();
	}

	@Override
	public LedgerVisitRecordVO entityVO(LedgerVisitRecordEntity record) {
		if (record == null) {
			return null;
		}
		return BeanUtil.copy(record, LedgerVisitRecordVO.class);
	}
}
