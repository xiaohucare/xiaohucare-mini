package com.xiaohucare.app.care.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.SettlementDetailEntity;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.mapper.SettlementDetailMapper;
import com.xiaohucare.app.care.service.SettlementDetailService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class SettlementDetailServiceImpl extends BaseServiceImpl<SettlementDetailMapper, SettlementDetailEntity> implements SettlementDetailService {

	/**
	 * 基础康养费的明细类型编码，与费用项“0-基础”同义
	 */
	private static final String ITEM_TYPE_BASE = "0";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void rebuild(SettlementEntity settlement, CheckinEntity checkin, List<CheckinFeeItemVO> feeItems) {
		if (settlement == null || settlement.getId() == null) {
			return;
		}
		// 幂等：先清空本结算单旧明细，再按当前口径重建
		QueryWrapper<SettlementDetailEntity> del = new QueryWrapper<>();
		del.eq("settlement_id", settlement.getId());
		remove(del);

		Date now = new Date();
		Integer days = checkin != null ? checkin.getActualDays() : null;
		String residentName = checkin != null ? checkin.getResidentName() : null;
		Long checkinId = settlement.getCheckinId();

		// 1) 基础康养费（套餐床位/护理费）：单列一行，便于与结算单 bed_fee 对账
		BigDecimal bedFee = settlement.getBedFee() != null ? settlement.getBedFee() : BigDecimal.ZERO;
		if (bedFee.compareTo(BigDecimal.ZERO) > 0) {
			SettlementDetailEntity base = newDetail(settlement.getId(), checkinId, residentName, now);
			base.setItemName("康养费");
			base.setItemType(ITEM_TYPE_BASE);
			base.setChargeMode(ChargeCalcKit.MODE_PER_DAY);
			base.setUnitPrice(BigDecimal.ZERO);
			base.setQuantity(1);
			base.setDays(days);
			base.setSubtotal(bedFee);
			save(base);
		}

		// 2) 费用项明细：跳过“0-基础”（已单列），与结算聚合口径保持一致
		if (feeItems != null) {
			for (CheckinFeeItemVO item : feeItems) {
				Integer type = item.getItemType();
				if (type == null || type == 0) {
					continue;
				}
				BigDecimal sub = ChargeCalcKit.calcItemAmount(item.getChargeMode(), item.getUnitPrice(), item.getQuantity(), days);
				SettlementDetailEntity detail = newDetail(settlement.getId(), checkinId, residentName, now);
				detail.setItemId(item.getItemId());
				detail.setItemName(StringUtils.isNotBlank(item.getItemName()) ? item.getItemName() : "费用项");
				detail.setItemType(String.valueOf(type));
				detail.setChargeMode(item.getChargeMode() != null ? item.getChargeMode() : ChargeCalcKit.MODE_PER_TIME);
				detail.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
				detail.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1);
				detail.setDays(days);
				detail.setSubtotal(sub);
				save(detail);
			}
		}
	}

	/** 组装明细公共字段 */
	private SettlementDetailEntity newDetail(Long settlementId, Long checkinId, String residentName, Date now) {
		SettlementDetailEntity detail = new SettlementDetailEntity();
		detail.setSettlementId(settlementId);
		detail.setCheckinId(checkinId);
		detail.setResidentName(StringUtils.isNotBlank(residentName) ? residentName : "未知长者");
		detail.setCreateAt(now);
		detail.setUpdateAt(now);
		detail.setIsDelete(0);
		return detail;
	}
}