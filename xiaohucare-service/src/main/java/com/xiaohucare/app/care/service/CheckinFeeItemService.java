package com.xiaohucare.app.care.service;

import java.util.List;

import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 入住登记费用项表
 *
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public interface CheckinFeeItemService extends BaseService<CheckinFeeItemEntity> {

	/**
	 * 按登记单查询费用项（一人一单）
	 * 
	 * @param checkinId  e_care_checkin.id
	 * @param actualDays 实际住养天数，用于按天/按年/按月项目折算金额
	 */
	List<CheckinFeeItemVO> listByCheckin(Long checkinId, Integer actualDays);
}
