package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;
import com.xiaohucare.app.ledger.vo.LedgerVisitOrderVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 探访工单 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerVisitOrderWrapper extends BaseEntityWrapper<LedgerVisitOrderEntity, LedgerVisitOrderVO> {

	public static LedgerVisitOrderWrapper build() {
		return new LedgerVisitOrderWrapper();
	}

	@Override
	public LedgerVisitOrderVO entityVO(LedgerVisitOrderEntity order) {
		if (order == null) {
			return null;
		}
		return BeanUtil.copy(order, LedgerVisitOrderVO.class);
	}
}

