package com.xiaohucare.app.care.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.mapper.CheckinMapper;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.vo.CheckinVO;
import com.xiaohucare.app.care.wrapper.CheckinWrapper;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class CheckinServiceImpl extends BaseServiceImpl<CheckinMapper, CheckinEntity> implements CheckinService {
	@Override
	public CheckinVO getByAppointmentId(Long appointmentId) {
		      QueryWrapper<CheckinEntity> queryWrapper=new QueryWrapper<>();
		     queryWrapper.eq("appointment_id", appointmentId);
	         queryWrapper.orderByDesc("id");
	         queryWrapper.last(" limit 1");
	         CheckinEntity groomingJob = this.getOne(queryWrapper);
	        return CheckinWrapper.build().entityVO(groomingJob);
	}
	
}