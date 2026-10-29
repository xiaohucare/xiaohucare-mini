package com.xiaohucare.app.care.wms.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.BatchEntity;
/**
 * 库存批次表
 * 
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@Mapper
public interface BatchMapper extends BaseMapper<BatchEntity> {
}
