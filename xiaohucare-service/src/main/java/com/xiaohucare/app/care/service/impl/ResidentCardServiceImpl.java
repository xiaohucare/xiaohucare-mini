package com.xiaohucare.app.care.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.ResidentCardEntity;
import com.xiaohucare.app.care.entity.ResidentCardPackageEntity;
import com.xiaohucare.app.care.entity.ResidentCardRecordEntity;
import com.xiaohucare.app.care.mapper.ResidentCardMapper;
import com.xiaohucare.app.care.mapper.ResidentCardPackageMapper;
import com.xiaohucare.app.care.mapper.ResidentCardRecordMapper;
import com.xiaohucare.app.care.service.ResidentCardService;
import com.xiaohucare.app.care.vo.ResidentCardVO;
import com.xiaohucare.app.care.wrapper.ResidentCardWrapper;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;


@Service
public class ResidentCardServiceImpl extends BaseServiceImpl<ResidentCardMapper, ResidentCardEntity> implements ResidentCardService {

	@Autowired
	private ResidentCardRecordMapper cardRecordMapper;

	@Autowired
	private ResidentCardPackageMapper cardPackageMapper;

	@Override
	public ResidentCardVO getCardByResidentId(Long residentId) {
		QueryWrapper<ResidentCardEntity> qw = new QueryWrapper<>();
		qw.eq("resident_id", residentId).eq("is_delete", 0).eq("status", 1).orderByDesc("id").last("LIMIT 1");
		ResidentCardEntity card = this.getOne(qw);
		return ResidentCardWrapper.build().entityVO(card);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResidentCardVO openCard(ResidentCardVO vo) {
		if (vo == null || vo.getResidentId() == null) {
			return null;
		}
		// 套餐信息（允许缺省；缺省时以调用方传入金额开卡）
		ResidentCardPackageEntity cardPackage = vo.getPackageId() != null
				? cardPackageMapper.selectById(vo.getPackageId()) : null;
		if (cardPackage != null) {
			vo.setCardLevel(cardPackage.getCardLevel());
		}
		// 实付入现金余额、赠送入赠送余额：开卡/充值时均不能丢失套餐赠送额
		BigDecimal paidAmount = vo.getOpenAmount() != null ? vo.getOpenAmount()
				: (cardPackage != null ? nvl(cardPackage.getActualAmount()) : BigDecimal.ZERO);
		BigDecimal giftAmount = vo.getGiftBalance() != null ? vo.getGiftBalance()
				: (cardPackage != null ? nvl(cardPackage.getGiftAmount()) : BigDecimal.ZERO);
		Date now = new Date();

		// 该长者是否已有有效会员卡
		ResidentCardEntity existingCard = getOne(new QueryWrapper<ResidentCardEntity>()
				.eq("resident_id", vo.getResidentId())
				.eq("status", 1)
				.eq("is_delete", 0)
				.orderByDesc("id")
				.last("LIMIT 1"));

		if (existingCard != null) {
			// 已有有效卡：转为套餐充值，避免重复建卡
			BigDecimal beforeBalance = nvl(existingCard.getBalance());
			BigDecimal beforeGift = nvl(existingCard.getGiftBalance());
			vo.setId(existingCard.getId());
			vo.setCardNo(existingCard.getCardNo());
			vo.setStatus(1);
			vo.setBalance(beforeBalance.add(paidAmount));
			vo.setGiftBalance(beforeGift.add(giftAmount));
			vo.setTotalRecharge(nvl(existingCard.getTotalRecharge()).add(paidAmount));
			vo.setTotalConsume(nvl(existingCard.getTotalConsume()));
			vo.setOpenDate(existingCard.getOpenDate());
			vo.setUpdateAt(now);
			updateById(vo);

			writeRecord(existingCard.getId(), existingCard.getCardNo(), existingCard.getResidentId(),
					existingCard.getResidentName(), "recharge", paidAmount, giftAmount,
					beforeBalance, vo.getBalance(), beforeGift, vo.getGiftBalance(), vo.getPayMethod(),
					cardPackage != null ? "套餐充值：" + cardPackage.getName() : "开卡时自动充值");
			return vo;
		}

		// 首次开卡
		String cardNo = "VIP" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now);
		vo.setCardNo(cardNo);
		vo.setStatus(1);
		vo.setBalance(paidAmount);
		vo.setGiftBalance(giftAmount);
		vo.setTotalRecharge(paidAmount);
		vo.setTotalConsume(BigDecimal.ZERO);
		vo.setOpenDate(now);
		vo.setCreateAt(now);
		vo.setUpdateAt(now);
		vo.setIsDelete(0);
		saveOrUpdate(vo);

		writeRecord(vo.getId(), cardNo, vo.getResidentId(), vo.getResidentName(), "open",
				paidAmount, giftAmount, BigDecimal.ZERO, vo.getBalance(), BigDecimal.ZERO, vo.getGiftBalance(),
				vo.getPayMethod(), cardPackage != null ? "首次开卡：" + cardPackage.getName() : "首次开卡");
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean recharge(Map<String, Object> params) {
		if (params == null || params.get("cardId") == null) {
			return false;
		}
		Long cardId = Long.valueOf(params.get("cardId").toString());
		ResidentCardEntity card = getById(cardId);
		if (card == null || card.getStatus() == null || card.getStatus() != 1) {
			return false;
		}

		BigDecimal amount;
		BigDecimal giftAmount;
		ResidentCardPackageEntity cardPackage = null;
		Object packageIdObj = params.get("packageId");
		if (packageIdObj != null && !"".equals(packageIdObj.toString())) {
			// 套餐充值：金额与赠送均以套餐为准，避免前端篡改
			cardPackage = cardPackageMapper.selectById(Long.valueOf(packageIdObj.toString()));
			if (cardPackage == null) {
				return false;
			}
			amount = nvl(cardPackage.getActualAmount());
			giftAmount = nvl(cardPackage.getGiftAmount());
		} else {
			if (params.get("amount") == null) {
				return false;
			}
			amount = new BigDecimal(params.get("amount").toString());
			giftAmount = params.get("giftAmount") != null
					? new BigDecimal(params.get("giftAmount").toString()) : BigDecimal.ZERO;
		}
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		BigDecimal beforeBalance = nvl(card.getBalance());
		BigDecimal beforeGift = nvl(card.getGiftBalance());
		card.setBalance(beforeBalance.add(amount));
		card.setGiftBalance(beforeGift.add(giftAmount));
		card.setTotalRecharge(nvl(card.getTotalRecharge()).add(amount));
		if (cardPackage != null) {
			// 套餐充值同步会员等级与套餐归属
			card.setPackageId(cardPackage.getId());
			card.setCardLevel(cardPackage.getCardLevel());
		}
		card.setUpdateAt(new Date());
		updateById(card);

		String remark = params.get("remark") != null ? params.get("remark").toString() : null;
		if (cardPackage != null) {
			remark = (remark == null || remark.isEmpty() ? "" : remark + "；") + "套餐充值：" + cardPackage.getName();
		}
		writeRecord(card.getId(), card.getCardNo(), card.getResidentId(), card.getResidentName(),
				"recharge", amount, giftAmount, beforeBalance, card.getBalance(), beforeGift, card.getGiftBalance(),
				params.get("payMethod") != null ? params.get("payMethod").toString() : null, remark);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean consume(Map<String, Object> params) {
		if (params == null || params.get("cardId") == null || params.get("amount") == null) {
			return false;
		}
		Long cardId = Long.valueOf(params.get("cardId").toString());
		BigDecimal amount = new BigDecimal(params.get("amount").toString());
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		boolean useGift = params.containsKey("useGift") && Boolean.TRUE.equals(params.get("useGift"));
		ResidentCardEntity card = getById(cardId);
		if (card == null || card.getStatus() == null || card.getStatus() != 1) {
			return false;
		}
		BigDecimal beforeBalance = nvl(card.getBalance());
		BigDecimal beforeGift = nvl(card.getGiftBalance());
		// 赠送余额仅在勾选后可用，可用额度不足时不允许透支
		BigDecimal usable = useGift ? beforeBalance.add(beforeGift) : beforeBalance;
		if (usable.compareTo(amount) < 0) {
			throw new RuntimeException("会员卡可用余额不足，当前可用¥" + usable.setScale(2, java.math.RoundingMode.HALF_UP));
		}
		// 优先扣减赠送余额
		if (useGift && beforeGift.compareTo(BigDecimal.ZERO) > 0) {
			if (beforeGift.compareTo(amount) >= 0) {
				card.setGiftBalance(beforeGift.subtract(amount));
			} else {
				BigDecimal remain = amount.subtract(beforeGift);
				card.setGiftBalance(BigDecimal.ZERO);
				card.setBalance(beforeBalance.subtract(remain));
			}
		} else {
			card.setBalance(beforeBalance.subtract(amount));
		}
		card.setTotalConsume(nvl(card.getTotalConsume()).add(amount));
		card.setUpdateAt(new Date());
		updateById(card);

		// 写入消费流水
		ResidentCardRecordEntity record = new ResidentCardRecordEntity();
		record.setTradeNo(nextTradeNo());
		record.setCardId(cardId);
		record.setCardNo(card.getCardNo());
		record.setResidentId(card.getResidentId());
		record.setResidentName(card.getResidentName());
		record.setTradeType("consume");
		record.setAmount(amount);
		record.setGiftAmount(BigDecimal.ZERO);
		record.setBeforeBalance(beforeBalance);
		record.setAfterBalance(card.getBalance());
		record.setBeforeGift(beforeGift);
		record.setAfterGift(card.getGiftBalance());
		record.setServiceType(params.containsKey("serviceType") ? params.get("serviceType").toString() : null);
		record.setUseGift(useGift ? 1 : 0);
		record.setRemark(params.containsKey("remark") ? (String) params.get("remark") : null);
		record.setTradeTime(new Date());
		record.setCreateAt(new Date());
		record.setIsDelete(0);
		return cardRecordMapper.insert(record) > 0;
	}

	@Override
	public boolean toggleStatus(Long id, Integer status) {
		ResidentCardEntity card = getById(id);
		if (card == null) {
			return false;
		}
		card.setStatus(status);
		card.setUpdateAt(new Date());
		return updateById(card);
	}

	@Override
	public Map<String, Object> getStat() {
		return baseMapper.selectStat();
	}

	/** 写入会员卡交易流水 */
	private void writeRecord(Long cardId, String cardNo, Long residentId, String residentName,
			String tradeType, BigDecimal amount, BigDecimal giftAmount, BigDecimal beforeBalance, BigDecimal afterBalance,
			BigDecimal beforeGift, BigDecimal afterGift, String payMethod, String remark) {
		ResidentCardRecordEntity record = new ResidentCardRecordEntity();
		record.setTradeNo(nextTradeNo());
		record.setCardId(cardId);
		record.setCardNo(cardNo);
		record.setResidentId(residentId);
		record.setResidentName(residentName);
		record.setTradeType(tradeType);
		record.setAmount(nvl(amount));
		record.setGiftAmount(nvl(giftAmount));
		record.setBeforeBalance(nvl(beforeBalance));
		record.setAfterBalance(nvl(afterBalance));
		record.setBeforeGift(nvl(beforeGift));
		record.setAfterGift(nvl(afterGift));
		record.setPayMethod(payMethod);
		record.setRemark(remark);
		record.setTradeTime(new Date());
		record.setCreateAt(new Date());
		record.setIsDelete(0);
		cardRecordMapper.insert(record);
	}

	/** 金额空值兜底 */
	private BigDecimal nvl(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	/** 交易流水号：时间戳 + 3 位随机，避免同毫秒重复 */
	private String nextTradeNo() {
		return "TRD" + System.currentTimeMillis() + (int) (Math.random() * 900 + 100);
	}
}
