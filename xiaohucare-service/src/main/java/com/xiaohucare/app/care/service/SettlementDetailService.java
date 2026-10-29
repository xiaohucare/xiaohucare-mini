package com.xiaohucare.app.care.service;
import java.util.List;

import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.SettlementDetailEntity;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 结算明细表
 *
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public interface SettlementDetailService extends BaseService<SettlementDetailEntity> {

	/**
	 * 按结算单重建明细（先删后插，幂等）：
	 * 基础康养费一行 + 各费用项一行，口径与结算单聚合列一致，便于逐笔对账。
	 *
	 * @param settlement 已落库的结算单（需有 id）
	 * @param checkin    登记单（取实际天数、长者姓名）
	 * @param feeItems   费用项列表（为空则仅重建基础康养费行）
	 */
	void rebuild(SettlementEntity settlement, CheckinEntity checkin, List<CheckinFeeItemVO> feeItems);
}

