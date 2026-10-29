package com.xiaohucare.app.care.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.mapper.EmployeeMapper;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeMapper, EmployeeEntity> implements EmployeeService {

}