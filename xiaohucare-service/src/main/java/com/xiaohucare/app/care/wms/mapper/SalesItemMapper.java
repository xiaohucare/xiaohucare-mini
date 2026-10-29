package com.xiaohucare.app.care.wms.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.vo.SalesItemVO;

/**
 * 出货明细表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface SalesItemMapper extends BaseMapper<SalesItemEntity> {
    
    /**
     * 出货数据统计（按时间维度）
     */
    List<Map<String, Object>> selectSalesStatsByTime(
        @Param("groupBy") String groupBy,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("customerId") Long customerId
    );
    
    /**
     * 出货数据统计（按产品维度）
     */
    List<Map<String, Object>> selectSalesStatsByProduct(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("customerId") Long customerId
    );
    
    /**
     * 出货数据统计（按客户维度）
     */
    List<Map<String, Object>> selectSalesStatsByCustomer(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("customerId") Long customerId
    );
    
    /**
     * 出货数据汇总
     */
    Map<String, Object> selectSalesSummary(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("customerId") Long customerId
    );
    
    /**
     * 时间维度交叉统计（二维）- 支持客户/品牌/产品
     */
    List<Map<String, Object>> selectTimeCrossStats(
        @Param("timeGranularity") String timeGranularity,
        @Param("crossDimension") String crossDimension,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("customerId") Long customerId
    );
    
    /**
     * 根据订单ID查询明细（连表查询SKU信息）
     */
    List<SalesItemVO> listByOrderIdWithSkuInfo(@Param("orderId") Long orderId);
}
