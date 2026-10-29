package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerPatrolRiskItemEntity;
import com.xiaohucare.app.ledger.vo.LedgerPatrolRiskItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 巡访隐患明细 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerPatrolRiskItemWrapper
		extends BaseEntityWrapper<LedgerPatrolRiskItemEntity, LedgerPatrolRiskItemVO> {

	public static LedgerPatrolRiskItemWrapper build() {
		return new LedgerPatrolRiskItemWrapper();
	}

	@Override
	public LedgerPatrolRiskItemVO entityVO(LedgerPatrolRiskItemEntity item) {
		if (item == null) {
			return null;
		}
		return BeanUtil.copy(item, LedgerPatrolRiskItemVO.class);
	}
}
