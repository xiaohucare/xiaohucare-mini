package com.xiaohucare.app.care.wms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.wms.entity.BatchEntity;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.entity.StockLogEntity;
import com.xiaohucare.app.care.wms.mapper.SkuMapper;
import com.xiaohucare.app.care.wms.service.BatchService;
import com.xiaohucare.app.care.wms.service.SkuService;
import com.xiaohucare.app.care.wms.service.StockLogService;
import com.xiaohucare.app.care.wms.vo.SkuVO;
import com.xiaohucare.app.care.wms.wrapper.SkuWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 库存管理Controller
 * 
 * @author xiaohucare
 * @date 2026-04-12
 */
@Tag(name = "库存管理")
@RestController
@RequestMapping("intra/wms/stock")
public class StockController extends BaseController{
    
    @Autowired
    private SkuService skuService;
    
    @Autowired
    private BatchService batchService;
    
    @Autowired
    private StockLogService stockLogService;
    
    @Autowired
    private SkuMapper skuMapper;
    
    /**
     * 库存列表（SKU列表）
     */
    @Operation(summary = "库存列表")
    @Parameters({
        @Parameter(name = "productName", description = "商品名称"),
        @Parameter(name = "skuCode", description = "SKU编码"),
        @Parameter(name = "stockStatus", description = "库存状态(normal/warning/out_of_stock)")
    })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:stock:list')")
    public R<VPage<SkuVO>> browse(@RequestParam Map<String, Object> params){
        IPage<SkuVO> pages = skuMapper.selectSkuPage(VQueryKit.getPage(params), params);
        return R.data(new VPage<>(pages));
    }
    
    /**
     * 库存预警列表
     */
    @Operation(summary = "库存预警列表")
    @GetMapping("/warning")
    @PreAuthorize("@ss.hasPermi('wms:stock:list')")
    public R<VPage<SkuVO>> warningList(@RequestParam Map<String, Object> params){
        params.put("stockStatus", "warning");
        return browse(params);
    }
    
    /**
     * 库存详情（含批次信息）
     */
    @Operation(summary = "库存详情")
    @GetMapping("/detail/{skuId}")
    @PreAuthorize("@ss.hasPermi('wms:stock:query')")
    public R<Map<String, Object>> detail(@PathVariable("skuId") Long skuId){
        Map<String, Object> result = new HashMap<>();
        
        // SKU信息
        SkuEntity sku = skuService.getById(skuId);
        result.put("sku", SkuWrapper.build().entityVO(sku));
        
        // 批次列表
        QueryWrapper<BatchEntity> batchQuery = new QueryWrapper<>();
        batchQuery.eq("sku_id", skuId);
        batchQuery.eq("status", 1); // 在库
        batchQuery.orderByAsc("purchase_date"); // FIFO排序
        List<BatchEntity> batches = batchService.list(batchQuery);
        result.put("batches", batches);
        
        // 库存统计
        int totalStock = batches.stream().mapToInt(BatchEntity::getStockQuantity).sum();
        result.put("totalStock", totalStock);
        
        return R.data(result);
    }
    
    /**
     * 批次列表
     */
    @Operation(summary = "批次列表")
    @Parameters({
        @Parameter(name = "skuId", description = "SKU ID"),
        @Parameter(name = "status", description = "状态(0-已耗尽 1-在库 2-已过期)")
    })
    @GetMapping("/batch/browse")
    @PreAuthorize("@ss.hasPermi('wms:stock:list')")
    public R<VPage<BatchEntity>> batchBrowse(@RequestParam Map<String, Object> params){
        QueryWrapper<BatchEntity> queryWrapper = new QueryWrapper<>();
        
        if(StringUtils.isNotNull(params.get("skuId"))) {
            queryWrapper.eq("sku_id", Long.valueOf(params.get("skuId").toString()));
        }
        if(StringUtils.isNotNull(params.get("status"))) {
            queryWrapper.eq("status", Integer.valueOf(params.get("status").toString()));
        }
        
        queryWrapper.orderByDesc("id");
        IPage<BatchEntity> pages = batchService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(new VPage<>(pages));
    }
    
    /**
     * 库存变动日志
     */
    @Operation(summary = "库存变动日志")
    @Parameters({
        @Parameter(name = "skuId", description = "SKU ID"),
        @Parameter(name = "changeType", description = "变动类型(PURCHASE/SALES/RETURN/ADJUST/EXPIRE)")
    })
    @GetMapping("/log/browse")
    @PreAuthorize("@ss.hasPermi('wms:stock:list')")
    public R<VPage<StockLogEntity>> logBrowse(@RequestParam Map<String, Object> params){
        QueryWrapper<StockLogEntity> queryWrapper = new QueryWrapper<>();
        
        if(StringUtils.isNotNull(params.get("skuId"))) {
            queryWrapper.eq("sku_id", Long.valueOf(params.get("skuId").toString()));
        }
        if(StringUtils.isNotNull(params.get("changeType"))) {
            queryWrapper.eq("change_type", params.get("changeType").toString());
        }
        
        queryWrapper.orderByDesc("id");
        IPage<StockLogEntity> pages = stockLogService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(new VPage<>(pages));
    }
    
    /**
     * 库存统计
     */
    @Operation(summary = "库存统计")
    @GetMapping("/stat")
    @PreAuthorize("@ss.hasPermi('wms:stock:list')")
    public R<Map<String, Object>> stockStat(){
        Map<String, Object> result = new HashMap<>();
        
        // 总SKU数
        QueryWrapper<SkuEntity> totalQuery = new QueryWrapper<>();
        totalQuery.eq("is_delete", 0);
        totalQuery.eq("status", 1);
        long totalSkus = skuService.count(totalQuery);
        result.put("totalSkus", totalSkus);
        
        // 总库存数量
        List<SkuEntity> allSkus = skuService.list(totalQuery);
        int totalStock = allSkus.stream().mapToInt(s -> s.getTotalStock() != null ? s.getTotalStock() : 0).sum();
        result.put("totalStock", totalStock);
        
        // 预警SKU数
        QueryWrapper<SkuEntity> warningQuery = new QueryWrapper<>();
        warningQuery.eq("is_delete", 0);
        warningQuery.eq("status", 1);
        warningQuery.apply("total_stock <= warning_stock AND total_stock > 0");
        long warningSkus = skuService.count(warningQuery);
        result.put("warningSkus", warningSkus);
        
        // 缺货SKU数
        QueryWrapper<SkuEntity> outQuery = new QueryWrapper<>();
        outQuery.eq("is_delete", 0);
        outQuery.eq("status", 1);
        outQuery.eq("total_stock", 0);
        long outSkus = skuService.count(outQuery);
        result.put("outSkus", outSkus);
        
        return R.data(result);
    }
}
