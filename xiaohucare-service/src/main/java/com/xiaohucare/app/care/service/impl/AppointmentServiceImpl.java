package com.xiaohucare.app.care.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.mapper.AppointmentMapper;
import com.xiaohucare.app.care.service.AppointmentService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class AppointmentServiceImpl extends BaseServiceImpl<AppointmentMapper, AppointmentEntity> implements AppointmentService {

}