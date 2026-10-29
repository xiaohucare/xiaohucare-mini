package com.xiaohucare.app.care.wms.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;
import com.xiaohucare.app.care.wms.mapper.SalesOrderMapper;
import com.xiaohucare.app.care.wms.service.SalesOrderService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class SalesOrderServiceImpl extends BaseServiceImpl<SalesOrderMapper, SalesOrderEntity> implements SalesOrderService {

}