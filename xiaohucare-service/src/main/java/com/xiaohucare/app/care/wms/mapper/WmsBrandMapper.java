package com.xiaohucare.app.care.wms.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.WmsBrandEntity;
/**
 * 商品品牌表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface WmsBrandMapper extends BaseMapper<WmsBrandEntity> {
    
    /**
     * 品牌销售排行
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 品牌销售排行列表
     */
    List<Map<String, Object>> getBrandSalesRanking(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 商品销售排行
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 商品销售排行列表
     */
    List<Map<String, Object>> getProductSalesRanking(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
