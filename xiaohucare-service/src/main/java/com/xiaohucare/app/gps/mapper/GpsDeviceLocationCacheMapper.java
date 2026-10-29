package com.xiaohucare.app.gps.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.gps.entity.GpsDeviceLocationCacheEntity;
/**
 * 设备位置缓存表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Mapper
public interface GpsDeviceLocationCacheMapper extends BaseMapper<GpsDeviceLocationCacheEntity> {
}
