package com.xiaohucare.app.care.meal.controller;
import java.text.ParseException;
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
import com.xiaohucare.app.care.meal.entity.MealsMenusEntity;
import com.xiaohucare.app.care.meal.service.MealsMenusService;
import com.xiaohucare.app.care.meal.vo.MealsMenusVO;
import com.xiaohucare.app.care.meal.wrapper.MealsMenusWrapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
/**
 * 餐饮菜单表
 * @author xiaohucare
 * @date 2025-11-11 22:48:39
 */
@Tag(name = "餐饮菜单表")
@RestController
@RequestMapping("intra/meal/mealsmenus")
public class MealsMenusController extends BaseController{
    @Autowired
    private MealsMenusService mealsMenusService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:list')")
    public R<VPage<MealsMenusVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<MealsMenusEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), MealsMenusEntity.class);
        queryWrapper.orderByDesc("id");
        
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<MealsMenusEntity> pages = mealsMenusService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(MealsMenusWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<MealsMenusEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), MealsMenusEntity.class);
         queryWrapper.select("count(id) as total,sum(id) as all");
         
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=mealsMenusService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('meal:mealsmenus:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<MealsMenusEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				MealsMenusEntity.class);
		queryWrapper.orderByDesc("id");
		
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<MealsMenusEntity> mealsMenusList = mealsMenusService.list(queryWrapper);
		ExcelUtil<MealsMenusEntity> util = new ExcelUtil<>(MealsMenusEntity.class);
		util.exportExcel(response, mealsMenusList, "参数数据");
	}
   
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:info')")
    public R<MealsMenusVO> detail(@PathVariable("id") Integer id){
		MealsMenusEntity mealsMenus = mealsMenusService.getById(id);
	    return R.data(MealsMenusWrapper.build().entityVO(mealsMenus));
    }
     /**
     * 保存&更新 
     */
    @Log(title = "餐饮菜单表", businessType = BusinessType.ADDANDEDIT)
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:save')")
    public R<MealsMenusVO> submit(@Valid @RequestBody MealsMenusVO mealsMenus){
    	 if(mealsMenus.getId()==null) {
           return add(mealsMenus);
         }else {
           return edit(mealsMenus);
        }
    }
    
    @Log(title = "餐饮菜单表", businessType = BusinessType.INSERT)
    @Operation(summary = "新增")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:save')")
    public R<MealsMenusVO> add(@Valid @RequestBody MealsMenusVO mealsMenus){
   		if(mealsMenus.getId()==null) {
            mealsMenus.setCreateAt(new Date());
            mealsMenus.setIsDelete(0);
        } 
   		mealsMenus.setUpdateAt(new Date());
    	if(mealsMenusService.save(mealsMenus)) {
    		 return R.ok(mealsMenus);
    	 }
    	return R.error("数据错误");
    }
    
    @Log(title = "餐饮菜单表", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新")
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:update')")
    public R<MealsMenusVO> edit(@Valid @RequestBody MealsMenusVO mealsMenus){
   		mealsMenus.setUpdateAt(new Date());
    	if(mealsMenusService.updateById(mealsMenus)) {
    		 return R.ok(mealsMenus);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermi('meal:mealsmenus:delete')")
    @Log(title = "餐饮菜单表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(mealsMenusService.removeByIds(Arrays.asList(ids)));
    }

}
