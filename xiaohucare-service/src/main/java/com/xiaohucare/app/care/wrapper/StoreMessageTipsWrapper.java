
package com.xiaohucare.app.care.wrapper;

import com.xiaohucare.app.care.entity.StoreMessageTipsEntity;
import com.xiaohucare.app.care.vo.StoreMessageTipsVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 店铺通知提示表 包装类,返回视图层所需的字段
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
public class StoreMessageTipsWrapper extends BaseEntityWrapper<StoreMessageTipsEntity, StoreMessageTipsVO> {

	public static StoreMessageTipsWrapper build() {
		return new StoreMessageTipsWrapper();
	}

	@Override
	public StoreMessageTipsVO entityVO(StoreMessageTipsEntity storeMessageTips) {
		if (storeMessageTips == null) {
			return null;
		}
		return BeanUtil.copy(storeMessageTips, StoreMessageTipsVO.class);
	}

}
