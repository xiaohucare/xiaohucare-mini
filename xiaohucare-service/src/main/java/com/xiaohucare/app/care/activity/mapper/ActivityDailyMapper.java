package com.xiaohucare.app.care.activity.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.activity.entity.ActivityDailyEntity;
/**
 * 日常活动记录表
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
@Mapper
public interface ActivityDailyMapper extends BaseMapper<ActivityDailyEntity> {
}