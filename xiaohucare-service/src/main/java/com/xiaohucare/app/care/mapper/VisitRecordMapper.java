package com.xiaohucare.app.care.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.VisitRecordEntity;
/**
 * 来访/外出记录表
 * @author xiaohucare
 * @date 2025-11-23 22:33:31
 */
@Mapper
public interface VisitRecordMapper extends BaseMapper<VisitRecordEntity> {
}
