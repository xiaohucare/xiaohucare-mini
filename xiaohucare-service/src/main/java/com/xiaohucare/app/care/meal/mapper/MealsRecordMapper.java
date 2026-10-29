package com.xiaohucare.app.care.meal.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.meal.entity.MealsRecordEntity;
/**
 * 餐饮记录表
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
@Mapper
public interface MealsRecordMapper extends BaseMapper<MealsRecordEntity> {
}