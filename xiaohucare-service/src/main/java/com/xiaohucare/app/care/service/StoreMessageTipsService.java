package com.xiaohucare.app.care.service;

import com.xiaohucare.app.care.entity.StoreMessageTipsEntity;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 店铺通知提示表
 *
 * @author xiaohucare
 * @date 2026-10-26
 */
public interface StoreMessageTipsService extends BaseService<StoreMessageTipsEntity> {

	/**
	 * 新增消息时刷新对应分类的未读提示(累加未读数、更新最后通知、追加通知ID集合)
	 *
	 * @param tipsType    分类: 0=系统 1=活动 2=交易
	 * @param noticeId    消息ID
	 * @param noticeTitle 消息标题
	 * @param noticeContent 消息内容
	 */
	void refreshOnNewNotice( Integer tipsType, Long noticeId, String noticeTitle, String noticeContent);

	/**
	 * 将某分类的未读提示清零(全部已读)
	 *
	 * @param tipsType 分类
	 * @return 是否成功
	 */
	boolean resetUnread( Integer tipsType);
}
