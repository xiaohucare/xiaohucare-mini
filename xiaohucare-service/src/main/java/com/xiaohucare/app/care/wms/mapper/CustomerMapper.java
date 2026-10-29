package com.xiaohucare.app.care.wms.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.CustomerEntity;
/**
 * 客户表（商品购买客户）
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerEntity> {
}
