
package com.xiaohucare.app.care.wrapper;

import com.xiaohucare.app.care.entity.StoreMessageEntity;
import com.xiaohucare.app.care.vo.StoreMessageVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 店铺通知表(门店消息) 包装类,返回视图层所需的字段
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
public class StoreMessageWrapper extends BaseEntityWrapper<StoreMessageEntity, StoreMessageVO> {

	public static StoreMessageWrapper build() {
		return new StoreMessageWrapper();
	}

	@Override
	public StoreMessageVO entityVO(StoreMessageEntity storeMessage) {
		if (storeMessage == null) {
			return null;
		}
		return BeanUtil.copy(storeMessage, StoreMessageVO.class);
	}

}
