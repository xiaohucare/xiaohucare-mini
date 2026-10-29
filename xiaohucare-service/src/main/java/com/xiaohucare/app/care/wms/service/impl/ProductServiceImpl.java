package com.xiaohucare.app.care.wms.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.wms.entity.ProductEntity;
import com.xiaohucare.app.care.wms.mapper.ProductMapper;
import com.xiaohucare.app.care.wms.service.ProductService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, ProductEntity> implements ProductService {

}