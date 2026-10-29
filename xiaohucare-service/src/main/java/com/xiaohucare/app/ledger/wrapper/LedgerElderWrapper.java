package com.xiaohucare.app.ledger.wrapper;

import com.xiaohucare.app.ledger.entity.LedgerElderEntity;
import com.xiaohucare.app.ledger.vo.LedgerElderVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 智慧台账-老人档案 包装类
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerElderWrapper extends BaseEntityWrapper<LedgerElderEntity, LedgerElderVO> {

	public static LedgerElderWrapper build() {
		return new LedgerElderWrapper();
	}

	@Override
	public LedgerElderVO entityVO(LedgerElderEntity ledgerElder) {
		if (ledgerElder == null) {
			return null;
		}
		return BeanUtil.copy(ledgerElder, LedgerElderVO.class);
	}
}
