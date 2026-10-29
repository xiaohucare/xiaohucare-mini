package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;
import com.xiaohucare.app.ledger.vo.LedgerPatrolOrderVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 巡访工单 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerPatrolOrderWrapper extends BaseEntityWrapper<LedgerPatrolOrderEntity, LedgerPatrolOrderVO> {

	public static LedgerPatrolOrderWrapper build() {
		return new LedgerPatrolOrderWrapper();
	}

	@Override
	public LedgerPatrolOrderVO entityVO(LedgerPatrolOrderEntity order) {
		if (order == null) {
			return null;
		}
		return BeanUtil.copy(order, LedgerPatrolOrderVO.class);
	}
}
