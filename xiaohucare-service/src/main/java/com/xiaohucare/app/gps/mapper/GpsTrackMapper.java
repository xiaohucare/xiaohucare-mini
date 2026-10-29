package com.xiaohucare.app.gps.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.gps.entity.GpsTrackEntity;
/**
 * 宠物GPS轨迹表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Mapper
public interface GpsTrackMapper extends BaseMapper<GpsTrackEntity> {
}
