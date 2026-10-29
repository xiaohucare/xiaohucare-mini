package com.xiaohucare.app.gps.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.gps.entity.GpsFenceAlarmRecordEntity;
/**
 * 围栏报警记录表
 * 
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@Mapper
public interface GpsFenceAlarmRecordMapper extends BaseMapper<GpsFenceAlarmRecordEntity> {
}
