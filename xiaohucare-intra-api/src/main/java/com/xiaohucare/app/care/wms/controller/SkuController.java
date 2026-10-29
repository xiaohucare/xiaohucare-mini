package com.xiaohucare.app.care.wms.controller;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
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
import com.xiaohucare.app.care.wms.entity.ProductEntity;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.service.ProductService;
import com.xiaohucare.app.care.wms.service.SkuService;
import com.xiaohucare.app.care.wms.vo.SkuVO;
import com.xiaohucare.app.care.wms.wrapper.SkuWrapper;
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
 * 商品SKU表
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@RestController
@RequestMapping("intra/wms/sku")
public class SkuController extends BaseController{
    @Autowired
    private SkuService skuService;
    @Autowired
    private ProductService productService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
                @Parameter(name = "keyword", description = "搜索关键字（SKU名称/编码/条形码）"),
                @Parameter(name = "categoryId", description = "分类ID"),
        })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:sku:list')")
    public R<VPage<SkuVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<SkuEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SkuEntity.class);
        
        // 关键字搜索：SKU名称、SKU编码、条形码
        if (StringUtils.isNotNull(params.get("keyword"))) {
            String keyword = params.get("keyword").toString();
            queryWrapper.and(wrapper -> wrapper
                .like("sku_name", keyword)
                .or().like("sku_code", keyword)
                .or().like("barcode", keyword)
            );
        }
        // 分类筛选
        if (params.containsKey("categoryId")) {
            queryWrapper.eq("category_id", params.get("categoryId"));
        }
        // 只查询有库存的
        queryWrapper.gt("total_stock", 0);
        queryWrapper.orderByDesc("id");
        IPage<SkuEntity> pages = skuService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SkuWrapper.build().toVPageVO(pages));
    }
    @Operation(summary = "列表")
    @Parameters({
    	@Parameter(name = "keyword", description = "搜索关键字（SKU名称/编码/条形码）"),
    	@Parameter(name = "categoryId", description = "分类ID"),
    })
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('wms:sku:list')")
    public R<VPage<SkuVO>> list(@RequestParam Map<String, Object> params){
    	QueryWrapper<SkuEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SkuEntity.class);
    	
    	// 关键字搜索：SKU名称、SKU编码、条形码
    	if (StringUtils.isNotNull(params.get("keyword"))) {
    		String keyword = params.get("keyword").toString();
    		queryWrapper.and(wrapper -> wrapper
    				.like("sku_name", keyword)
    				.or().like("sku_code", keyword)
    				.or().like("barcode", keyword)
    				);
    	}
    	// 分类筛选
    	if (params.containsKey("categoryId")) {
    		queryWrapper.eq("category_id", params.get("categoryId"));
    	}
    	queryWrapper.orderByDesc("id");
    	IPage<SkuEntity> pages = skuService.page(VQueryKit.getPage(params), queryWrapper);
    	return R.data(SkuWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:sku:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SkuEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SkuEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=skuService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:sku:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SkuEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SkuEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SkuEntity> skuList = skuService.list(queryWrapper);
		ExcelUtil<SkuEntity> util = new ExcelUtil<>(SkuEntity.class);
		util.exportExcel(response, skuList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:sku:query')")
    public R<SkuVO> detail(@PathVariable("id") Long id){
		SkuEntity sku = skuService.getById(id);
	    return R.data(SkuWrapper.build().entityVO(sku));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:sku:add,wms:sku:edit')")
    public R<SkuVO> submit(@Valid @RequestBody SkuVO sku){
   		// 自动同步分类 ID（从商品表获取）
   		if (sku.getProductId() != null && sku.getCategoryId() == null) {
   			ProductEntity product = productService.getById(sku.getProductId());
   			if (product != null) {
   				sku.setCategoryId(product.getCategoryId());
   				sku.setMarketPrice(product.getMarketPrice());
   				sku.setProductName(product.getProductName());
   			}
   		}
   		
   		if(sku.getId()==null) {
            sku.setCreateAt(new Date());
            sku.setIsDelete(0);
        } 
        sku.setUpdateAt(new Date());
    	if(skuService.saveOrUpdate(sku)) {
    		 return R.ok(sku);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:sku:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(skuService.removeByIds(Arrays.asList(ids)));
    }

}
