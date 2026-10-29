package com.xiaohucare.app.care.service;

import com.xiaohucare.app.care.entity.StoreMessageEntity;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 店铺通知表(门店消息)
 *
 * @author xiaohucare
 * @date 2026-10-26
 */
public interface StoreMessageService extends BaseService<StoreMessageEntity> {

	/**
	 * 浏览详情时累加浏览次数
	 *
	 * @param id 消息ID
	 * @return 是否成功
	 */
	boolean increaseViewCount(Long id);
}
