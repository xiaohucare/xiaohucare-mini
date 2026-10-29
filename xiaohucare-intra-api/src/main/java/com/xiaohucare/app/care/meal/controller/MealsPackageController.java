package com.xiaohucare.app.care.meal.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.meal.entity.MealsPackageEntity;
import com.xiaohucare.app.care.meal.service.MealsPackageService;
import com.xiaohucare.app.care.meal.vo.MealsPackageVO;
import com.xiaohucare.app.care.meal.wrapper.MealsPackageWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
/**
 * 餐饮套餐表
 * @author xiaohucare
 * @date 2026-10-20 10:00:00
 */
@Tag(name = "餐饮套餐表")
@RestController
@RequestMapping("intra/meal/mealspackage")
public class MealsPackageController extends BaseController{
    @Autowired
    private MealsPackageService mealsPackageService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:list')")
    public R<VPage<MealsPackageVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<MealsPackageEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("packageName","mealType","status"), MealsPackageEntity.class);
        queryWrapper.orderByDesc("id");
        
        queryWrapper.like(StringUtils.isNotNull(params.get("packageName")),"package_name", params.get("packageName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("mealType")),"meal_type", params.get("mealType"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")),"status", params.get("status"));
        IPage<MealsPackageEntity> pages = mealsPackageService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(MealsPackageWrapper.build().toVPageVO(pages));
    }
     @Operation(summary = "列表")
    @GetMapping("/listAll")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:list')")
    public R<List<MealsPackageVO>> listAll(@RequestParam Map<String, Object> params){
        QueryWrapper<MealsPackageEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("packageName","mealType","status"), MealsPackageEntity.class);
        queryWrapper.orderByDesc("id");
        
        queryWrapper.like(StringUtils.isNotNull(params.get("packageName")),"package_name", params.get("packageName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("mealType")),"meal_type", params.get("mealType"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")),"status", params.get("status"));
        List<MealsPackageEntity> list = mealsPackageService.list(queryWrapper);
        return R.data(MealsPackageWrapper.build().listVO(list));
    }
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:info')")
    public R<MealsPackageVO> detail(@PathVariable("id") Integer id){
		MealsPackageEntity mealsPackage = mealsPackageService.getById(id);
	    return R.data(MealsPackageWrapper.build().entityVO(mealsPackage));
    }
     /**
     * 保存&更新 
     */
    @Log(title = "餐饮套餐表", businessType = BusinessType.ADDANDEDIT)
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:save')")
    public R<MealsPackageVO> submit(@Valid @RequestBody MealsPackageVO mealsPackage){
    	 if(mealsPackage.getId()==null) {
           return add(mealsPackage);
         }else {
           return edit(mealsPackage);
        }
    }
    
    @Log(title = "餐饮套餐表", businessType = BusinessType.INSERT)
    @Operation(summary = "新增")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:save')")
    public R<MealsPackageVO> add(@Valid @RequestBody MealsPackageVO mealsPackage){
   		if(mealsPackage.getId()==null) {
            mealsPackage.setCreateAt(new Date());
            mealsPackage.setIsDelete(0);
            mealsPackage.setUpdateAt(new Date());
        } 
    	// 包含菜单数组转逗号串持久化
    	mealsPackage.setMenuIdStr(joinIds(mealsPackage.getMenuIds()));
    	if(mealsPackageService.save(mealsPackage)) {
    		 return R.ok(mealsPackage);
    	 }
    	return R.error("数据错误");
    }
    
    @Log(title = "餐饮套餐表", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新")
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:update')")
    public R<MealsPackageVO> edit(@Valid @RequestBody MealsPackageVO mealsPackage){
   		mealsPackage.setUpdateAt(new Date());
    	// 包含菜单数组转逗号串持久化
    	mealsPackage.setMenuIdStr(joinIds(mealsPackage.getMenuIds()));
    	if(mealsPackageService.updateById(mealsPackage)) {
    		 return R.ok(mealsPackage);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermi('meal:mealspackage:delete')")
    @Log(title = "餐饮套餐表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(mealsPackageService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * List<Integer> 转逗号分隔串
     */
    private String joinIds(List<Integer> ids){
        if(ids == null || ids.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Integer id : ids){
            if(id == null){
                continue;
            }
            if(sb.length() > 0){
                sb.append(",");
            }
            sb.append(id);
        }
        return sb.toString();
    }

}
