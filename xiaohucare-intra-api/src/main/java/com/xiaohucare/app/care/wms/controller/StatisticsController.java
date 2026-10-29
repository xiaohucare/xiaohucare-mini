package com.xiaohucare.app.care.wms.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.wms.entity.BatchEntity;
import com.xiaohucare.app.care.wms.entity.PurchaseOrderEntity;
import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.mapper.SalesItemMapper;
import com.xiaohucare.app.care.wms.mapper.WmsBrandMapper;
import com.xiaohucare.app.care.wms.service.BatchService;
import com.xiaohucare.app.care.wms.service.PurchaseOrderService;
import com.xiaohucare.app.care.wms.service.SalesOrderService;
import com.xiaohucare.app.care.wms.service.SkuService;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * WMS统计报表Controller
 * @author xiaohucare
 * @date 2026-04-12
 */
@Tag(name = "WMS统计报表")
@RestController
@RequestMapping("intra/wms/statistics")
public class StatisticsController extends BaseController {
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    @Autowired
    private SkuService skuService;
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    
    @Autowired
    private BatchService batchService;
    
    
    @Autowired
    private WmsBrandMapper brandMapper;
    
    
    @Autowired
    private SalesItemMapper salesItemMapper;
    
    /**
     * 销售统计
     */
    @Operation(summary = "销售统计")
    @GetMapping("/sales")
    @PreAuthorize("@ss.hasPermi('wms:statistics:list')")
    public R<Map<String, Object>> salesStats(@RequestParam Map<String, Object> params)  {
        Map<String, Object> result = new HashMap<>();
        
        // 今日销售统计
        QueryWrapper<SalesOrderEntity> todayQuery = new QueryWrapper<>();
        todayQuery.eq("status", 1);
        todayQuery.eq("is_delete", 0);
        todayQuery.apply("DATE(create_at) = CURDATE()");
        
        List<SalesOrderEntity> todayOrders = salesOrderService.list(todayQuery);
        BigDecimal todaySales = todayOrders.stream()
            .map(order -> order.getActualAmount() != null ? order.getActualAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        result.put("todaySales", todaySales);
        result.put("todayOrders", todayOrders.size());
        
        // 昨日销售统计（用于计算趋势）
        QueryWrapper<SalesOrderEntity> yesterdayQuery = new QueryWrapper<>();
        yesterdayQuery.eq("status", 1);
        yesterdayQuery.eq("is_delete", 0);
        yesterdayQuery.apply("DATE(create_at) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)");
        
        List<SalesOrderEntity> yesterdayOrders = salesOrderService.list(yesterdayQuery);
        BigDecimal yesterdaySales = yesterdayOrders.stream()
            .map(order -> order.getActualAmount() != null ? order.getActualAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算趋势
        double trend = 0;
        if(yesterdaySales.compareTo(BigDecimal.ZERO) > 0) {
            trend = todaySales.subtract(yesterdaySales)
                .divide(yesterdaySales, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
        }
        result.put("todayTrend", trend);
        
        // 销售趋势图（最近7天）
        List<Map<String, Object>> salesTrend = new ArrayList<>();
        for(int i = 6; i >= 0; i--) {
            Date date = DateUtils.addDays(new Date(), -i);
            String dateStr = DateUtils.parseDateToStr("yyyy-MM-dd", date);
            
            QueryWrapper<SalesOrderEntity> trendQuery = new QueryWrapper<>();
            trendQuery.eq("status", 1);
            trendQuery.eq("is_delete", 0);
            trendQuery.apply("DATE(create_at) = '" + dateStr + "'");
            
            List<SalesOrderEntity> dayOrders = salesOrderService.list(trendQuery);
            BigDecimal dayAmount = dayOrders.stream()
                .map(order -> order.getActualAmount() != null ? order.getActualAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> trendData = new HashMap<>();
            trendData.put("date", dateStr);
            trendData.put("amount", dayAmount);
            salesTrend.add(trendData);
        }
        result.put("salesTrend", salesTrend);
        
        // 支付方式分布
        Map<String, Object> payMethodDistribution = new HashMap<>();
        QueryWrapper<SalesOrderEntity> payQuery = new QueryWrapper<>();
        payQuery.eq("status", 1);
        payQuery.eq("is_delete", 0);
        
        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            String startDate = params.get("startDate").toString();
            String endDate = params.get("endDate").toString();
            try {
				payQuery.between("create_at",
				    DateUtils.getStartOfDate(DateUtils.str_ToDate(startDate)),
				    DateUtils.getEndOfDate(DateUtils.str_ToDate(endDate))
				);
			} catch (ParseException e) {
			}
        } else {
            payQuery.apply("DATE(create_at) = CURDATE()");
        }
       
        List<SalesOrderEntity> payOrders = salesOrderService.list(payQuery);
        for(SalesOrderEntity order : payOrders) {
            String payMethod = order.getPayMethod() != null ? order.getPayMethod() : "UNKNOWN";
            BigDecimal amount = order.getActualAmount() != null ? order.getActualAmount() : BigDecimal.ZERO;
            
            BigDecimal currentTotal = (BigDecimal) payMethodDistribution.getOrDefault(payMethod, BigDecimal.ZERO);
            payMethodDistribution.put(payMethod, currentTotal.add(amount));
        }
        result.put("payMethodDistribution", payMethodDistribution);
        
        return R.data(result);
    }
    
    /**
     * 库存统计
     */
    @Operation(summary = "库存统计")
    @GetMapping("/inventory")
    @PreAuthorize("@ss.hasPermi('wms:statistics:list')")
    public R<Map<String, Object>> inventoryStats() {
        Map<String, Object> result = new HashMap<>();
        
        // 库存预警数量
        QueryWrapper<SkuEntity> warningQuery = new QueryWrapper<>();
        warningQuery.eq("is_delete", 0);
        warningQuery.apply("total_stock <= warning_stock");
        long warningCount = skuService.count(warningQuery);
        result.put("stockWarning", warningCount);
        
        // 即将过期数量（30天内）
        QueryWrapper<BatchEntity> expiryQuery = new QueryWrapper<>();
        expiryQuery.eq("status", 1);
        expiryQuery.isNotNull("expiry_date");
        expiryQuery.apply("expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)");
        long expiryCount = batchService.count(expiryQuery);
        result.put("expiryWarning", expiryCount);
        
        // 总SKU数
        QueryWrapper<SkuEntity> skuQuery = new QueryWrapper<>();
        skuQuery.eq("is_delete", 0);
        long totalSkus = skuService.count(skuQuery);
        result.put("totalSkus", totalSkus);
        
        // 总库存数量
        List<SkuEntity> skuList = skuService.list(skuQuery);
        int totalStock = skuList.stream()
            .mapToInt(sku -> sku.getTotalStock() != null ? sku.getTotalStock() : 0)
            .sum();
        result.put("totalStock", totalStock);
        
        return R.data(result);
    }
    
    /**
     * 进货统计
     */
    @Operation(summary = "进货统计")
    @GetMapping("/purchase")
    @PreAuthorize("@ss.hasPermi('wms:statistics:list')")
    public R<Map<String, Object>> purchaseStats(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        QueryWrapper<PurchaseOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        
        
        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            queryWrapper.between("purchase_date", params.get("startDate"), params.get("endDate"));
        }
        
        List<PurchaseOrderEntity> orders = purchaseOrderService.list(queryWrapper);
        
        // 进货总金额
        BigDecimal totalAmount = orders.stream()
            .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        result.put("totalAmount", totalAmount);
        result.put("orderCount", orders.size());
        
        return R.data(result);
    }
    
    /**
     * 品牌统计
     */
    @Operation(summary = "品牌统计")
    @GetMapping("/brand")
    @PreAuthorize("@ss.hasPermi('wms:statistics:list')")
    public R<Map<String, Object>> brandStats(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        // 品牌销售排行
        List<Map<String, Object>> brandRanking = brandMapper.getBrandSalesRanking(startDate, endDate);
        result.put("brandRanking", brandRanking);
        
        // 商品销售排行
        List<Map<String, Object>> productRanking = brandMapper.getProductSalesRanking(startDate, endDate);
        result.put("productRanking", productRanking);
        
        return R.data(result);
    }
    
    /**
     * 过期预警
     */
    @Operation(summary = "过期预警")
    @GetMapping("/expiry")
    @PreAuthorize("@ss.hasPermi('wms:statistics:list')")
    public R<List<Map<String, Object>>> expiryWarning() {
        QueryWrapper<BatchEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        queryWrapper.isNotNull("expiry_date");
        queryWrapper.apply("expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)");
        queryWrapper.orderByAsc("expiry_date");
        
        List<BatchEntity> batches = batchService.list(queryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for(BatchEntity batch : batches) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", batch.getId());
            map.put("batchNo", batch.getBatchNo());
            map.put("skuId", batch.getSkuId());
            map.put("stockQuantity", batch.getStockQuantity());
            map.put("expiryDate", batch.getExpiryDate());
            map.put("supplier", batch.getSupplier());
            
            // 计算剩余天数
            if(batch.getExpiryDate() != null) {
                long diff = batch.getExpiryDate().getTime() - System.currentTimeMillis();
                long remainingDays = diff / (24 * 60 * 60 * 1000);
                map.put("remainingDays", remainingDays);
            }
            
            result.add(map);
        }
        
        return R.data(result);
    }
    
    /**
     * 出货数据统计分析
     */
    @Operation(summary = "出货数据统计分析")
    @GetMapping("/sales-analysis")
    @PreAuthorize("@ss.hasAnyPermi('wms:statistics:list,wms:sales:analysis')")
    public R<Map<String, Object>> salesAnalysis(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        // 统计维度：year/month/day/product
        String groupBy = params.get("groupBy") != null ? params.get("groupBy").toString() : "day";
        
        // 获取筛选参数
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        Long customerId = params.get("customerId") != null ? Long.valueOf(params.get("customerId").toString()) : null;
        List<Map<String, Object>> statisticsData = new ArrayList<>();
        
        // 根据统计维度调用不同的查询方法
        if("product".equals(groupBy)) {
            // 按产品统计
            statisticsData = salesItemMapper.selectSalesStatsByProduct(startDate, endDate, customerId);
        } else if("customer".equals(groupBy)) {
            // 按客户统计
            statisticsData = salesItemMapper.selectSalesStatsByCustomer(startDate, endDate, customerId);
        } else {
            // 按时间统计（年/月/天）
            statisticsData = salesItemMapper.selectSalesStatsByTime(groupBy, startDate, endDate, customerId);
        }
        
        result.put("groupBy", groupBy);
        result.put("data", statisticsData);
        
        // 汇总数据
        Map<String, Object> summaryData = salesItemMapper.selectSalesSummary(startDate, endDate, customerId);
        if(summaryData != null) {
            result.put("summary", summaryData);
        }
        
        return R.data(result);
    }
    
    /**
     * 时间维度交叉统计（二维）- 支持客户/品牌/产品
     */
    @Operation(summary = "时间维度交叉统计")
    @GetMapping("/customer-time-cross")
    @PreAuthorize("@ss.hasAnyPermi('wms:statistics:list,wms:sales:analysis')")
    public R<Map<String, Object>> customerTimeCrossStats(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取筛选参数
        String timeGranularity = params.get("timeGranularity") != null ? params.get("timeGranularity").toString() : "month";
        String crossDimension = params.get("crossDimension") != null ? params.get("crossDimension").toString() : "customer";
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        Long customerId = params.get("customerId") != null ? Long.valueOf(params.get("customerId").toString()) : null;
        // 查询交叉统计数据
        List<Map<String, Object>> crossData = salesItemMapper.selectTimeCrossStats(
            timeGranularity, crossDimension, startDate, endDate, customerId
        );
        
        // 组织数据结构：{ rows: [...], periods: [...], data: {...} }
        Map<String, Object> organizedData = organizeCrossData(crossData);
        
        // 汇总数据
        Map<String, Object> summaryData = salesItemMapper.selectSalesSummary(startDate, endDate, customerId);
        if(summaryData != null) {
            result.put("summary", summaryData);
        }
        
        result.putAll(organizedData);
        
        return R.data(result);
    }
    
    /**
     * 组织交叉统计数据
     */
    private Map<String, Object> organizeCrossData(List<Map<String, Object>> crossData) {
        Map<String, Object> result = new HashMap<>();
        
        // 提取所有时间段（排序）
        List<String> periods = new ArrayList<>();
        // 提取所有行（客户/品牌/产品）
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Map<String, Object>> rowMap = new LinkedHashMap<>();
        // 数据矩阵：rowId -> period -> {amount, quantity, orderCount}
        Map<String, Map<String, Map<String, Object>>> dataMatrix = new LinkedHashMap<>();
        
        for(Map<String, Object> row : crossData) {
            String rowId = String.valueOf(row.get("rowId"));
            String period = String.valueOf(row.get("period"));
            
            // 收集时间段
            if(!periods.contains(period)) {
                periods.add(period);
            }
            
            // 收集行数据
            if(!rowMap.containsKey(rowId)) {
                Map<String, Object> rowData = new HashMap<>();
                rowData.put("id", row.get("rowId"));
                rowData.put("name", row.get("rowName"));
                rowData.put("extra", row.get("rowPhone"));
                rowMap.put(rowId, rowData);
                rows.add(rowData);
                dataMatrix.put(rowId, new LinkedHashMap<>());
            }
            
            // 填充数据
            Map<String, Object> periodData = new HashMap<>();
            periodData.put("amount", row.get("amount"));
            periodData.put("quantity", row.get("quantity"));
            periodData.put("orderCount", row.get("orderCount"));
            dataMatrix.get(rowId).put(period, periodData);
        }
        
        // 排序时间段
        Collections.sort(periods);
        
        result.put("rows", rows);
        result.put("periods", periods);
        result.put("data", dataMatrix);
        
        return result;
    }
}
