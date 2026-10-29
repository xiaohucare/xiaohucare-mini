
package com.xiaohucare.app.care.wrapper;

import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 入住登记费用项表 包装类，返回视图层所需的字段
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class CheckinFeeItemWrapper extends BaseEntityWrapper<CheckinFeeItemEntity, CheckinFeeItemVO> {

	public static CheckinFeeItemWrapper build() {
		return new CheckinFeeItemWrapper();
	}

	@Override
	public CheckinFeeItemVO entityVO(CheckinFeeItemEntity checkinFeeItem) {
		if (checkinFeeItem == null) {
			return null;
		}
		return BeanUtil.copy(checkinFeeItem, CheckinFeeItemVO.class);
	}

}
