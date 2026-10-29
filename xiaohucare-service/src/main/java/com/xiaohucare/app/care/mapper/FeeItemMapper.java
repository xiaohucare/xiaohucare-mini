package com.xiaohucare.app.care.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.FeeItemEntity;
/**
 * 收费类目表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
@Mapper
public interface FeeItemMapper extends BaseMapper<FeeItemEntity> {
}
