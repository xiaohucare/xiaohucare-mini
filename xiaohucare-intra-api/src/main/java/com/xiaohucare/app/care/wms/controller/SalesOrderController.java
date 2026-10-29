package com.xiaohucare.app.care.wms.controller;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.wms.entity.BatchEntity;
import com.xiaohucare.app.care.wms.entity.CustomerEntity;
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.entity.StockLogEntity;
import com.xiaohucare.app.care.wms.service.BatchService;
import com.xiaohucare.app.care.wms.service.CustomerService;
import com.xiaohucare.app.care.wms.service.SalesItemService;
import com.xiaohucare.app.care.wms.service.SalesOrderService;
import com.xiaohucare.app.care.wms.service.SkuService;
import com.xiaohucare.app.care.wms.service.StockLogService;
import com.xiaohucare.app.care.wms.vo.SalesOrderVO;
import com.xiaohucare.app.care.wms.wrapper.SalesOrderWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
/**
 * 出货单表
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/salesorder")
public class SalesOrderController extends BaseController{
    @Autowired
    private SalesOrderService salesOrderService;
    
    @Autowired
    private SalesItemService salesItemService;
    
    @Autowired
    private SkuService skuService;
    
    @Autowired
    private BatchService batchService;
    
    @Autowired
    private StockLogService stockLogService;
    @Autowired
    private CustomerService customerService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:salesorder:list')")
    public R<VPage<SalesOrderVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<SalesOrderEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SalesOrderEntity.class);
        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<SalesOrderEntity> pages = salesOrderService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SalesOrderWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:salesorder:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SalesOrderEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SalesOrderEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=salesOrderService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:salesorder:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SalesOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SalesOrderEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SalesOrderEntity> salesOrderList = salesOrderService.list(queryWrapper);
		ExcelUtil<SalesOrderEntity> util = new ExcelUtil<>(SalesOrderEntity.class);
		util.exportExcel(response, salesOrderList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:salesorder:query')")
    public R<SalesOrderVO> detail(@PathVariable("id") Long id){
		SalesOrderEntity salesOrder = salesOrderService.getById(id);
		SalesOrderVO salesOrderVO=SalesOrderWrapper.build().entityVO(salesOrder);
        salesOrderVO.setSalesItemVOList(salesItemService.listByOrderId(id));
	    return R.data(salesOrderVO);
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:salesorder:add,wms:salesorder:edit')")
    public R<SalesOrderVO> submit(@Valid @RequestBody SalesOrderVO salesOrder){
   		if(salesOrder.getId()==null) {
            salesOrder.setCreateAt(new Date());
            salesOrder.setIsDelete(0);
        } 
        salesOrder.setUpdateAt(new Date());
    	if(salesOrderService.saveOrUpdate(salesOrder)) {
    		 return R.ok(salesOrder);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:salesorder:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(salesOrderService.removeByIds(Arrays.asList(ids)));
    }
    
    /**
     * 销售开单（checkout）
     */
    @Operation(summary = "销售开单")
    @PostMapping("/checkout")
    @PreAuthorize("@ss.hasPermi('wms:salesorder:checkout')")
    @Transactional(rollbackFor = Exception.class)
    public R<SalesOrderVO> checkout(@RequestBody Map<String, Object> params) {
        try {
            // 1. 创建出货单
            SalesOrderEntity order = new SalesOrderEntity();
            order.setOrderNo("SO" + System.currentTimeMillis());
            order.setCustomerId(Long.valueOf(params.get("customerId").toString()));
            if(order.getCustomerId()!=null) {
            	CustomerEntity customer =	customerService.getById(order.getCustomerId());
            	if(customer!=null) {
            		 order.setCustomerName(customer.getCustomerName());
                     order.setCustomerPhone(customer.getCustomerPhone());
            	}
            }
            if(params.containsKey("guardianId") && params.get("guardianId") != null) {
                order.setGuardianId(Long.valueOf(params.get("guardianId").toString()));
            }
            order.setPayMethod(params.get("payMethod").toString());
            if(params.containsKey("remark")) {
                order.setRemark(params.get("remark").toString());
            }
            order.setStatus(1);
            order.setCreateAt(new Date());
            order.setIsDelete(0);
            // 2. 计算金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            Integer totalCount=0;
            List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
            for(Map<String, Object> item : items) {
                Long skuId = Long.valueOf(item.get("skuId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                BigDecimal salePrice = new BigDecimal(item.get("salePrice").toString());
                totalAmount = totalAmount.add(salePrice.multiply(new BigDecimal(quantity)));
                totalCount = totalCount+quantity;
            }
            order.setTotalCount(totalCount);
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(params.containsKey("discountAmount") ? 
                new BigDecimal(params.get("discountAmount").toString()) : BigDecimal.ZERO);
            order.setActualAmount(totalAmount.subtract(order.getDiscountAmount()));
            
            salesOrderService.save(order);
            
            // 3. 处理明细，FIFO扣减库存
            for(Map<String, Object> item : items) {
                Long skuId = Long.valueOf(item.get("skuId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                BigDecimal salePrice = new BigDecimal(item.get("salePrice").toString());
                
                // 3.1 创建出货明细
                SalesItemEntity salesItem = new SalesItemEntity();
                salesItem.setOrderId(order.getId());
                salesItem.setSkuId(skuId);
                salesItem.setQuantity(quantity);
                salesItem.setSalePrice(salePrice);
                salesItem.setSubtotal(salePrice.multiply(new BigDecimal(quantity)));
                
                // 获取生产日期和有效期（如果有）
                if(item.containsKey("productionDate") && item.get("productionDate") != null) {
                    // 根据业务需要，可以记录到明细中
                }
                if(item.containsKey("expirationDate") && item.get("expirationDate") != null) {
                    // 根据业务需要，可以记录到明细中
                }
                salesItemService.save(salesItem);
                
                // 3.2 FIFO扣减库存
                int remainingQty = quantity;
                QueryWrapper<BatchEntity> batchQuery = new QueryWrapper<>();
                batchQuery.eq("sku_id", skuId);
                batchQuery.eq("status", 1);
                batchQuery.gt("stock_quantity", 0);
                batchQuery.orderByAsc("purchase_date");
                
                List<BatchEntity> batches = batchService.list(batchQuery);
                
                // 检查库存是否充足
                int totalAvailableStock = batches.stream().mapToInt(BatchEntity::getStockQuantity).sum();
                if(totalAvailableStock < quantity) {
                    throw new RuntimeException("SKU库存不足，当前可用库存：" + totalAvailableStock + "，需要：" + quantity);
                }
                
                for(BatchEntity batch : batches) {
                    if(remainingQty <= 0) break;
                    
                    int beforeStock = batch.getStockQuantity();
                    int deductQty = Math.min(remainingQty, batch.getStockQuantity());
                    int afterStock = beforeStock - deductQty;
                    
                    batch.setStockQuantity(afterStock);
                    if(batch.getStockQuantity() == 0) {
                        batch.setStatus(0); // 已耗尽
                    }
                    batchService.updateById(batch);
                    
                    remainingQty -= deductQty;
                    
                    // 3.3 记录库存变动日志
                    StockLogEntity log = new StockLogEntity();
                    log.setSkuId(skuId);
                    log.setBatchId(batch.getId());
                    log.setChangeType("OUT");
                    log.setChangeQuantity(-deductQty);
                    log.setBeforeStock(beforeStock);
                    log.setAfterStock(afterStock);
                    log.setRelatedOrderNo(order.getOrderNo());
                    log.setRemark("销售出货");
                    log.setCreateAt(new Date());
                    log.setIsDelete(0);
                    stockLogService.save(log);
                }
            }
            
            // 3.4 统一更新所有涉及SKU的总库存
            for(Map<String, Object> item : items) {
                Long skuId = Long.valueOf(item.get("skuId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                
                SkuEntity sku = skuService.getById(skuId);
                if(sku != null) {
                    sku.setTotalStock(sku.getTotalStock() - quantity);
                    skuService.updateById(sku);
                }
            }
            
            return R.data(SalesOrderWrapper.build().entityVO(order));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("开单失败：" + e.getMessage());
        }
    }

}
