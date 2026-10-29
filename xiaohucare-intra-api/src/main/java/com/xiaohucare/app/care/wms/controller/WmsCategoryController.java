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
import com.xiaohucare.app.care.wms.entity.WmsCategoryEntity;
import com.xiaohucare.app.care.wms.service.WmsCategoryService;
import com.xiaohucare.app.care.wms.vo.WmsCategoryVO;
import com.xiaohucare.app.care.wms.wrapper.WmsCategoryWrapper;
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
 * 商品分类表
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/category")
public class WmsCategoryController extends BaseController{
    @Autowired
    private WmsCategoryService categoryService;
    
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
        @Parameter(name = "categoryName", description = "分类名称"),
    })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:category:list')")
    public R<VPage<WmsCategoryVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<WmsCategoryEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("categoryName"), WmsCategoryEntity.class);
        queryWrapper.eq("is_delete", 0);
        
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.like(StringUtils.isNotNull(params.get("categoryName")), "category_name", params.get("categoryName"));
        IPage<WmsCategoryEntity> pages = categoryService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(WmsCategoryWrapper.build().toVPageVO(pages));
    }
    
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:category:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException{
        QueryWrapper<WmsCategoryEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("categoryName"), WmsCategoryEntity.class);
        queryWrapper.eq("is_delete", 0);
        
        queryWrapper.select("count(id) as total");
        queryWrapper.like(StringUtils.isNotNull(params.get("categoryName")), "category_name", params.get("categoryName"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
            );   
        }
        Map<String, Object> stat = categoryService.getMap(queryWrapper);
        return R.data(stat);
    }
    
    @Operation(summary = "导出")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('wms:category:export')")
    @PostMapping("/toExport")
    public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        QueryWrapper<WmsCategoryEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("categoryName"), WmsCategoryEntity.class);
        queryWrapper.eq("is_delete", 0);
        
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.like(StringUtils.isNotNull(params.get("categoryName")), "category_name", params.get("categoryName"));
        List<WmsCategoryEntity> categoryList = categoryService.list(queryWrapper);
        ExcelUtil<WmsCategoryEntity> util = new ExcelUtil<>(WmsCategoryEntity.class);
        util.exportExcel(response, categoryList, "商品分类数据");
    }
    
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:category:query')")
    public R<WmsCategoryVO> detail(@PathVariable("id") Long id){
        WmsCategoryEntity category = categoryService.getById(id);
        return R.data(WmsCategoryWrapper.build().entityVO(category));
    }
    
    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:category:add,wms:category:edit')")
    public R<WmsCategoryVO> submit(@Valid @RequestBody WmsCategoryVO category){
        if(category.getId() == null) {
            category.setCreateAt(new Date());
            category.setIsDelete(0);
        } 
        category.setUpdateAt(new Date());
        if(categoryService.saveOrUpdate(category)) {
            return R.ok(category);
        }
        return R.error("数据错误");
    }
    
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:category:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(categoryService.removeByIds(Arrays.asList(ids)));
    }
}
