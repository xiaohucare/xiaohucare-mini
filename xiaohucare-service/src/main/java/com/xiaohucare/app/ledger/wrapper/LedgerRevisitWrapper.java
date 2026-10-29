package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerRevisitEntity;
import com.xiaohucare.app.ledger.vo.LedgerRevisitVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 工单回访记录 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerRevisitWrapper extends BaseEntityWrapper<LedgerRevisitEntity, LedgerRevisitVO> {

	public static LedgerRevisitWrapper build() {
		return new LedgerRevisitWrapper();
	}

	@Override
	public LedgerRevisitVO entityVO(LedgerRevisitEntity revisit) {
		if (revisit == null) {
			return null;
		}
		return BeanUtil.copy(revisit, LedgerRevisitVO.class);
	}
}
