package com.xiaohucare.app.care.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.ComboFeeItemEntity;
/**
 * 套餐费用项关联表
 * 
 * @author xiaohucare
 * @date 2026-03-21 13:47:57
 */
@Mapper
public interface ComboFeeItemMapper extends BaseMapper<ComboFeeItemEntity> {
}
