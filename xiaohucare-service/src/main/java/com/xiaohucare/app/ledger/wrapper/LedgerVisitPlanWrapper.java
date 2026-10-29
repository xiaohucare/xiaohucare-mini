package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;
import com.xiaohucare.app.ledger.vo.LedgerVisitPlanVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 探访/巡访计划配置 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerVisitPlanWrapper extends BaseEntityWrapper<LedgerVisitPlanEntity, LedgerVisitPlanVO> {

	public static LedgerVisitPlanWrapper build() {
		return new LedgerVisitPlanWrapper();
	}

	@Override
	public LedgerVisitPlanVO entityVO(LedgerVisitPlanEntity plan) {
		if (plan == null) {
			return null;
		}
		return BeanUtil.copy(plan, LedgerVisitPlanVO.class);
	}
}
