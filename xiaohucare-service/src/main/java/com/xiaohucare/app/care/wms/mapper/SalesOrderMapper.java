package com.xiaohucare.app.care.wms.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;
/**
 * 出货单表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrderEntity> {
}
