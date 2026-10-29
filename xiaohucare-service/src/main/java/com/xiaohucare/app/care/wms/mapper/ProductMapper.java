package com.xiaohucare.app.care.wms.mapper;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.wms.entity.ProductEntity;
import com.xiaohucare.app.care.wms.vo.ProductVO;
/**
 * 商品主表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
    
    /**
     * 分页查询商品（含品牌名称、分类名称、SKU数量）
     */
    IPage<ProductVO> selectProductPage(IPage<ProductEntity> page, @Param("params") Map<String, Object> params);
}
