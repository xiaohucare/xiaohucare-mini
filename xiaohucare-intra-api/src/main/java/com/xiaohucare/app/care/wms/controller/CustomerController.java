package com.xiaohucare.app.care.wms.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.wms.entity.CustomerEntity;
import com.xiaohucare.app.care.wms.service.CustomerService;
import com.xiaohucare.app.care.wms.service.SalesOrderService;
import com.xiaohucare.app.care.wms.vo.CustomerVO;
import com.xiaohucare.app.care.wms.wrapper.CustomerWrapper;
import com.xiaohucare.common.enums.BusinessType;
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
 * 客户表（商品购买客户）
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/customer")
public class CustomerController extends BaseController{
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private SalesOrderService salesOrderService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "customerName", description = "客户/供应商名称"),
		        @Parameter(name = "customerPhone", description = "联系电话"),
		        @Parameter(name = "customerType", description = "客户类型"),
		        @Parameter(name = "kind", description = "类型(0-客户 1-供应商)"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:customer:list')")
    public R<VPage<CustomerVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<CustomerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        
        
        // 类型筛选（客户/供应商）
        if(StringUtils.isNotNull(params.get("kind"))) {
            queryWrapper.eq("kind", Integer.valueOf(params.get("kind").toString()));
        }
        
        // 客户姓名/供应商名称筛选
        if(StringUtils.isNotNull(params.get("customerName"))) {
            queryWrapper.like("customer_name", params.get("customerName"));
        }
        // 联系电话筛选
        if(StringUtils.isNotNull(params.get("customerPhone"))) {
            queryWrapper.like("customer_phone", params.get("customerPhone"));
        }
        // 客户类型筛选
        if(StringUtils.isNotNull(params.get("customerType"))) {
            queryWrapper.eq("customer_type", params.get("customerType"));
        }
        
        queryWrapper.orderByDesc("id");
        IPage<CustomerEntity> pages = customerService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(CustomerWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @Operation(summary = "客户统计")
    @PreAuthorize("@ss.hasPermi('wms:customer:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException{
        Map<String, Object> result = new HashMap<>();
        
        // 客户总数
        QueryWrapper<CustomerEntity> totalQuery = new QueryWrapper<>();
        totalQuery.eq("is_delete", 0);
        totalQuery.eq("status", 1);
        long totalCustomers = customerService.count(totalQuery);
        result.put("totalCustomers", totalCustomers);
        
        // 会员客户数
        QueryWrapper<CustomerEntity> memberQuery = new QueryWrapper<>();
        memberQuery.eq("is_delete", 0);
        memberQuery.eq("status", 1);
        memberQuery.eq("customer_type", "MEMBER");
        long memberCustomers = customerService.count(memberQuery);
        result.put("memberCustomers", memberCustomers);
        
        // 散客数
        QueryWrapper<CustomerEntity> walkInQuery = new QueryWrapper<>();
        walkInQuery.eq("is_delete", 0);
        walkInQuery.eq("status", 1);
        walkInQuery.eq("customer_type", "WALK_IN");
        long walkInCustomers = customerService.count(walkInQuery);
        result.put("walkInCustomers", walkInCustomers);
        
        // 家属客户数
        QueryWrapper<CustomerEntity> ownerQuery = new QueryWrapper<>();
        ownerQuery.eq("is_delete", 0);
        ownerQuery.eq("status", 1);
        ownerQuery.eq("customer_type", "e_GUARDIAN");
        long ownerCustomers = customerService.count(ownerQuery);
        result.put("ownerCustomers", ownerCustomers);
        
        // 活跃客户数（30天内有购买）
        QueryWrapper<CustomerEntity> activeQuery = new QueryWrapper<>();
        activeQuery.eq("is_delete", 0);
        activeQuery.eq("status", 1);
        activeQuery.isNotNull("last_purchase_date");
        activeQuery.apply("last_purchase_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)");
        long activeCustomers = customerService.count(activeQuery);
        result.put("activeCustomers", activeCustomers);
        
        // 客户分类统计
        List<Map<String, Object>> customerTypeStat = new ArrayList<>();
        
        QueryWrapper<CustomerEntity> typeQuery = new QueryWrapper<>();
        typeQuery.eq("is_delete", 0);
        typeQuery.eq("status", 1);
        typeQuery.select("customer_type, count(*) as count");
        typeQuery.groupBy("customer_type");
        List<CustomerEntity> typeList = customerService.list(typeQuery);
        
        for(CustomerEntity customer : typeList) {
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("customerType", customer.getCustomerType());
            typeMap.put("typeName", getCustomerTypeName(customer.getCustomerType()));
            typeMap.put("count", customerService.count(new QueryWrapper<CustomerEntity>()
                .eq("is_delete", 0)
                .eq("status", 1)
                .eq("customer_type", customer.getCustomerType())));
            customerTypeStat.add(typeMap);
        }
        result.put("customerTypeStat", customerTypeStat);
        
        return R.data(result);
    }
    
    /**
     * 获取客户类型名称
     */
    private String getCustomerTypeName(String type) {
        if(type == null) return "未知";
        switch(type) {
            case "WALK_IN": return "散客";
            case "MEMBER": return "会员";
            case "e_GUARDIAN": return "家属";
            default: return "未知";
        }
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:customer:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<CustomerEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				CustomerEntity.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<CustomerEntity> customerList = customerService.list(queryWrapper);
		ExcelUtil<CustomerEntity> util = new ExcelUtil<>(CustomerEntity.class);
		util.exportExcel(response, customerList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:customer:query')")
    public R<CustomerVO> detail(@PathVariable("id") Long id){
		CustomerEntity customer = customerService.getById(id);
	    return R.data(CustomerWrapper.build().entityVO(customer));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:customer:add,wms:customer:edit')")
    public R<CustomerVO> submit(@Valid @RequestBody CustomerVO customer){
   		if(customer.getId()==null) {
            customer.setCreateAt(new Date());
            customer.setIsDelete(0);
        } 
        customer.setUpdateAt(new Date());
    	if(customerService.saveOrUpdate(customer)) {
    		 return R.ok(customer);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:customer:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(customerService.removeByIds(Arrays.asList(ids)));
    }

}
