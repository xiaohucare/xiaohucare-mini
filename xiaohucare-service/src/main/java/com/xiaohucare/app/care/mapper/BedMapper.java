package com.xiaohucare.app.care.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.BedEntity;
/**
 * 康养床位表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Mapper
public interface BedMapper extends BaseMapper<BedEntity> {
}
