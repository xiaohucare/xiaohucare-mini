package com.xiaohucare.app.care.wms.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.wms.entity.CustomerEntity;
import com.xiaohucare.app.care.wms.mapper.CustomerMapper;
import com.xiaohucare.app.care.wms.service.CustomerService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, CustomerEntity> implements CustomerService {

}