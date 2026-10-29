package com.xiaohucare.app.care.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.mapper.CheckinFeeItemMapper;
import com.xiaohucare.app.care.service.CheckinFeeItemService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.app.care.wrapper.CheckinFeeItemWrapper;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

@Service
public class CheckinFeeItemServiceImpl extends BaseServiceImpl<CheckinFeeItemMapper, CheckinFeeItemEntity> implements CheckinFeeItemService {

	@Override
	public List<CheckinFeeItemVO> listByCheckin(Long checkinId, Integer actualDays) {
		QueryWrapper<CheckinFeeItemEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(checkinId != null, "checkin_id", checkinId);
		queryWrapper.orderByDesc("id");
		List<CheckinFeeItemEntity> list = this.list(queryWrapper);
		List<CheckinFeeItemVO> voList = CheckinFeeItemWrapper.build().listVO(list);
		// 按天/按年/按月项目回显折算金额，保证交接页与结算单口径一致
		for (CheckinFeeItemVO vo : voList) {
			vo.setAmount(ChargeCalcKit.calcItemAmount(vo.getChargeMode(), vo.getUnitPrice(), vo.getQuantity(), actualDays));
		}
		return voList;
	}
}
