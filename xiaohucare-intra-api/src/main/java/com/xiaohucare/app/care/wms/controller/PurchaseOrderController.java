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
import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;
import com.xiaohucare.app.care.wms.entity.PurchaseOrderEntity;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.entity.StockLogEntity;
import com.xiaohucare.app.care.wms.service.BatchService;
import com.xiaohucare.app.care.wms.service.CustomerService;
import com.xiaohucare.app.care.wms.service.PurchaseItemService;
import com.xiaohucare.app.care.wms.service.PurchaseOrderService;
import com.xiaohucare.app.care.wms.service.SkuService;
import com.xiaohucare.app.care.wms.service.StockLogService;
import com.xiaohucare.app.care.wms.vo.PurchaseItemVO;
import com.xiaohucare.app.care.wms.vo.PurchaseOrderVO;
import com.xiaohucare.app.care.wms.wrapper.PurchaseOrderWrapper;
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
 * 进货单表
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@RestController
@RequestMapping("intra/wms/purchaseorder")
public class PurchaseOrderController extends BaseController{
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    
    @Autowired
    private PurchaseItemService purchaseItemService;
    
    @Autowired
    private SkuService skuService;
    
    @Autowired
    private BatchService batchService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StockLogService stockLogService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "orderNo", description = "进货单号"),
		        @Parameter(name = "supplier", description = "供应商"),
		        @Parameter(name = "status", description = "状态(0-待入库 1-已入库)"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:purchase:list')")
    public R<VPage<PurchaseOrderVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<PurchaseOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        
        
        // 进货单号筛选
        if(StringUtils.isNotNull(params.get("orderNo"))) {
            queryWrapper.like("order_no", params.get("orderNo"));
        }
        // 供应商筛选
        if(StringUtils.isNotNull(params.get("customerName"))) {
            queryWrapper.like("customer_name", params.get("customerName"));
        }
        // 状态筛选
        if(StringUtils.isNotNull(params.get("status"))) {
            queryWrapper.eq("status", Integer.valueOf(params.get("status").toString()));
        }
        
        queryWrapper.orderByDesc("id");
        IPage<PurchaseOrderEntity> pages = purchaseOrderService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(PurchaseOrderWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:purchase:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<PurchaseOrderEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), PurchaseOrderEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=purchaseOrderService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:purchase:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<PurchaseOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				PurchaseOrderEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<PurchaseOrderEntity> purchaseOrderList = purchaseOrderService.list(queryWrapper);
		ExcelUtil<PurchaseOrderEntity> util = new ExcelUtil<>(PurchaseOrderEntity.class);
		util.exportExcel(response, purchaseOrderList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:purchase:query')")
    public R<PurchaseOrderVO> detail(@PathVariable("id") Long id){
		PurchaseOrderEntity purchaseOrder = purchaseOrderService.getById(id);
		PurchaseOrderVO vo = PurchaseOrderWrapper.build().entityVO(purchaseOrder);
		vo.setPurchaseItemVOList(purchaseItemService.listByOrderId(id));
	    return R.data(vo);
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:purchase:add,wms:purchase:edit')")
    @Transactional(rollbackFor = Exception.class)
    public R<PurchaseOrderVO> submit(@Valid @RequestBody PurchaseOrderVO purchaseOrder){
   		if(purchaseOrder.getId()==null) {
            purchaseOrder.setCreateAt(new Date());
            purchaseOrder.setIsDelete(0);
            // 生成进货单号
            purchaseOrder.setOrderNo("PO" + System.currentTimeMillis());
            purchaseOrder.setStatus(0); // 默认待入库
        } 
        purchaseOrder.setUpdateAt(new Date());
        purchaseOrder.setCreateBy(this.getUsername());
        if(purchaseOrder.getCustomerId()!=null) {
        	CustomerEntity customer =	customerService.getById(purchaseOrder.getCustomerId());
        	if(customer!=null) {
        		purchaseOrder.setCustomerName(customer.getCustomerName());
        	}
        }
    	if(purchaseOrderService.saveOrUpdate(purchaseOrder)) {
    	    // 保存明细
    	    if(purchaseOrder.getId() != null && purchaseOrder.getPurchaseItemVOList() != null) {
    	        // 先删除旧明细
    	        QueryWrapper<PurchaseItemEntity> deleteQuery = new QueryWrapper<>();
    	        deleteQuery.eq("order_id", purchaseOrder.getId());
    	        purchaseItemService.remove(deleteQuery);
    	        // 保存新明细
    	        for(PurchaseItemVO item : purchaseOrder.getPurchaseItemVOList()) {
    	            item.setOrderId(purchaseOrder.getId());
    	            item.setCreateAt(new Date());
    	            item.setUpdateAt(new Date());
    	            // 计算小计
    	            if(item.getQuantity() != null && item.getPurchasePrice() != null) {
    	                item.setSubtotal(item.getPurchasePrice().multiply(new BigDecimal(item.getQuantity())));
    	            }
    	            purchaseItemService.saveOrUpdate(item);
    	        }
    	    }
    		
    		 return R.ok(purchaseOrder);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:purchase:remove')")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> delete(@RequestBody Long[] ids){
        // 级联删除明细
        for(Long id : ids) {
            QueryWrapper<PurchaseItemEntity> deleteQuery = new QueryWrapper<>();
            deleteQuery.eq("order_id", id);
            purchaseItemService.remove(deleteQuery);
        }
        return R.ok(purchaseOrderService.removeByIds(Arrays.asList(ids)));
    }
    
    /**
     * 入库确认
     */
    @Operation(summary = "入库确认")
    @PostMapping("/confirm/{id}")
    @PreAuthorize("@ss.hasPermi('wms:purchase:confirm')")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> confirm(@PathVariable("id") Long id){
        // 1. 查询进货单
        PurchaseOrderEntity order = purchaseOrderService.getById(id);
        if(order == null) {
            return R.error("进货单不存在");
        }
        
        if(order.getStatus() != 0) {
            return R.error("进货单已确认");
        }
        if(order.getCustomerId()!=null) {
        	CustomerEntity customer =	customerService.getById(order.getCustomerId());
        	if(customer!=null) {
        		 order.setCustomerName(customer.getCustomerName());
        	}
        }
        // 2. 计算金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        Integer totalCount=0;
        
        // 3. 查询进货明细
        QueryWrapper<PurchaseItemEntity> itemQuery = new QueryWrapper<>();
        itemQuery.eq("order_id", id);
        List<PurchaseItemEntity> items = purchaseItemService.list(itemQuery);
        
        // 4. 处理每个明细项
        for(PurchaseItemEntity item : items) {
            // 4.1 更新SKU库存
            SkuEntity sku = skuService.getById(item.getSkuId());
            if(sku != null) {
                sku.setTotalStock(sku.getTotalStock() + item.getQuantity());
                skuService.updateById(sku);
            }
            
            Integer quantity =item.getQuantity();
            totalAmount = totalAmount.add(item.getSubtotal());
            totalCount = totalCount+quantity;
            
            // 4.2 创建批次
            BatchEntity batch = new BatchEntity();
            batch.setSkuId(item.getSkuId());
            batch.setBatchNo(item.getBatchNo() != null ? item.getBatchNo() : "B" + System.currentTimeMillis());
            batch.setPurchaseOrderId(id);
            batch.setStockQuantity(item.getQuantity());
            batch.setInitialQuantity(item.getQuantity());
            batch.setPurchasePrice(item.getPurchasePrice());
            batch.setPurchaseDate(new Date());
            batch.setProductionDate(item.getProductionDate());
            batch.setExpiryDate(item.getExpiryDate());
            batch.setSupplier(order.getCustomerName());
            batch.setCustomerId(order.getCustomerId());
            batch.setCustomerName(order.getCustomerName());
            batch.setStatus(1); // 在库
            batchService.save(batch);
            // 4.3 记录库存日志
            StockLogEntity log = new StockLogEntity();
            log.setSkuId(item.getSkuId());
            log.setBatchId(batch.getId());
            log.setChangeType("PURCHASE");
            log.setChangeQuantity(item.getQuantity());
            log.setBeforeStock(sku != null ? sku.getTotalStock() - item.getQuantity() : 0);
            log.setAfterStock(sku != null ? sku.getTotalStock() : item.getQuantity());
            log.setRelatedOrderNo(order.getOrderNo());
            log.setRemark("进货入库 - " + order.getOrderNo());
            stockLogService.save(log);
            PurchaseItemEntity dbItem=new PurchaseItemEntity();
            dbItem.setId(item.getId());
            dbItem.setBatchNo(batch.getBatchNo());
            purchaseItemService.updateById(dbItem);
        }
        // 2. 更新进货单状态
        order.setTotalCount(totalCount);
        order.setTotalSkuCount(items.size());
        order.setStatus(1); // 已入库
        purchaseOrderService.updateById(order);
        return R.ok(true);
    }

}
