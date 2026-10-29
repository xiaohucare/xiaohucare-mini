package com.xiaohucare.app.care.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.StoreMessageEntity;

/**
 * 店铺通知表(门店消息)
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Mapper
public interface StoreMessageMapper extends BaseMapper<StoreMessageEntity> {
}
