package com.xiaohucare.app.care.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaohucare.app.care.entity.StoreMessageTipsEntity;
import com.xiaohucare.app.care.mapper.StoreMessageTipsMapper;
import com.xiaohucare.app.care.service.StoreMessageTipsService;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 店铺通知提示表
 *
 * @author xiaohucare
 * @date 2026-10-26
 */
@Service
public class StoreMessageTipsServiceImpl extends BaseServiceImpl<StoreMessageTipsMapper, StoreMessageTipsEntity> implements StoreMessageTipsService {

	/** last_notice 字段最大长度 */
	private static final int LAST_NOTICE_MAX_LEN = 145;

	@Override
	public void refreshOnNewNotice( Integer tipsType, Long noticeId, String noticeTitle, String noticeContent) {
		if (noticeId == null) {
			return;
		}
		int type = tipsType == null ? 0 : tipsType;
		Date now = new Date();
		String brief = buildBrief(noticeContent);

		QueryWrapper<StoreMessageTipsEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tips_type", type);
		StoreMessageTipsEntity tips = getOne(queryWrapper, false);

		if (tips == null) {
			tips = new StoreMessageTipsEntity();
			tips.setTipsType(type);
			tips.setCount(1);
			tips.setLastNotice(brief);
			tips.setLastNoticeTitle(noticeTitle);
			tips.setLastDate(now);
			tips.setLastId(noticeId);
			tips.setNoticeIds(String.valueOf(noticeId));
			tips.setCreateAt(now);
			tips.setIsDelete(0);
			save(tips);
		} else {
			tips.setCount((tips.getCount() == null ? 0 : tips.getCount()) + 1);
			tips.setLastNotice(brief);
			tips.setLastNoticeTitle(noticeTitle);
			tips.setLastDate(now);
			tips.setLastId(noticeId);
			tips.setNoticeIds(appendNoticeId(tips.getNoticeIds(), noticeId));
			tips.setUpdateAt(now);
			updateById(tips);
		}
	}

	@Override
	public boolean resetUnread( Integer tipsType) {
		UpdateWrapper<StoreMessageTipsEntity> updateWrapper = new UpdateWrapper<>();
		if (tipsType != null) {
			updateWrapper.eq("tips_type", tipsType);
		}
		updateWrapper.set("count", 0).set("notice_ids", null).set("update_at", new Date());
		return update(updateWrapper);
	}

	/**
	 * 去除富文本标签并截断为简短摘要
	 */
	private String buildBrief(String content) {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		String text = content.replaceAll("<[^>]+>", "").trim();
		if (text.length() <= LAST_NOTICE_MAX_LEN) {
			return text;
		}
		return text.substring(0, LAST_NOTICE_MAX_LEN);
	}

	/**
	 * 追加通知ID到逗号分隔的ID集合
	 */
	private String appendNoticeId(String noticeIds, Long noticeId) {
		if (StringUtils.isBlank(noticeIds)) {
			return String.valueOf(noticeId);
		}
		return noticeIds + "," + noticeId;
	}

}
