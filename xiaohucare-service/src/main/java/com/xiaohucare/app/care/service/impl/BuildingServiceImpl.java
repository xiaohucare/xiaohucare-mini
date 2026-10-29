package com.xiaohucare.app.care.service.impl;

import org.springframework.stereotype.Service;

import com.xiaohucare.app.care.entity.BuildingEntity;
import com.xiaohucare.app.care.mapper.BuildingMapper;
import com.xiaohucare.app.care.service.BuildingService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

@Service
public class BuildingServiceImpl extends BaseServiceImpl<BuildingMapper, BuildingEntity> implements BuildingService {
}
