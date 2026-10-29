package com.xiaohucare.app.care.wms.mapper;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.vo.SkuVO;
/**
 * 商品SKU表
 * 
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@Mapper
public interface SkuMapper extends BaseMapper<SkuEntity> {
    
    /**
     * 分页查询SKU（含商品名称、单位、规格）
     */
    IPage<SkuVO> selectSkuPage(IPage<SkuEntity> page, @Param("params") Map<String, Object> params);
}
