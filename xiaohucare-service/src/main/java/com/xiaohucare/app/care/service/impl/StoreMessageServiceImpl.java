package com.xiaohucare.app.care.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaohucare.app.care.entity.StoreMessageEntity;
import com.xiaohucare.app.care.mapper.StoreMessageMapper;
import com.xiaohucare.app.care.service.StoreMessageService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 店铺通知表(门店消息)
 *
 * @author xiaohucare
 * @date 2026-10-26
 */
@Service
public class StoreMessageServiceImpl extends BaseServiceImpl<StoreMessageMapper, StoreMessageEntity> implements StoreMessageService {

	@Override
	public boolean increaseViewCount(Long id) {
		if (id == null) {
			return false;
		}
		UpdateWrapper<StoreMessageEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id).setSql("view_count = IFNULL(view_count, 0) + 1");
		return update(updateWrapper);
	}

}
