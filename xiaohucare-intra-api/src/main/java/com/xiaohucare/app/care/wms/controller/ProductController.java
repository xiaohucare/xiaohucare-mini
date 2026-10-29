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
import com.xiaohucare.app.care.wms.mapper.ProductMapper;
import com.xiaohucare.app.care.wms.service.ProductService;
import com.xiaohucare.app.care.wms.vo.ProductVO;
import com.xiaohucare.app.care.wms.wrapper.ProductWrapper;
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
 * 商品主表
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/product")
public class ProductController extends BaseController{
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "productName", description = "商品名称"),
		        @Parameter(name = "productType", description = "商品类型"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:product:list')")
    public R<VPage<ProductVO>> browse(@RequestParam Map<String, Object> params){
        IPage<ProductVO> pages = productMapper.selectProductPage(VQueryKit.getPage(params), params);
        return R.data(new VPage<>(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:product:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<ProductEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), ProductEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=productService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:product:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ProductEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				ProductEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<ProductEntity> productList = productService.list(queryWrapper);
		ExcelUtil<ProductEntity> util = new ExcelUtil<>(ProductEntity.class);
		util.exportExcel(response, productList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:product:query')")
    public R<ProductVO> detail(@PathVariable("id") Long id){
		ProductEntity product = productService.getById(id);
	    return R.data(ProductWrapper.build().entityVO(product));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:product:add,wms:product:edit')")
    public R<ProductVO> submit(@Valid @RequestBody ProductVO product){
   		if(product.getId()==null) {
            product.setCreateAt(new Date());
            product.setIsDelete(0);
        } 
        product.setUpdateAt(new Date());
    	if(productService.saveOrUpdate(product)) {
    		 return R.ok(product);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:product:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(productService.removeByIds(Arrays.asList(ids)));
    }

}
