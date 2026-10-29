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
import com.xiaohucare.app.care.meal.entity.MealsRecordEntity;
import com.xiaohucare.app.care.meal.service.MealsRecordService;
import com.xiaohucare.app.care.meal.vo.MealsRecordVO;
import com.xiaohucare.app.care.meal.wrapper.MealsRecordWrapper;
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
 * 餐饮记录表
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
@Tag(name = "餐饮记录表")
@RestController
@RequestMapping("intra/meal/mealsrecord")
public class MealsRecordController extends BaseController{
    @Autowired
    private MealsRecordService mealsRecordService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:list')")
    public R<VPage<MealsRecordVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<MealsRecordEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), MealsRecordEntity.class);
        queryWrapper.orderByDesc("id");
        
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<MealsRecordEntity> pages = mealsRecordService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(MealsRecordWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<MealsRecordEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), MealsRecordEntity.class);
         queryWrapper.select("count(id) as total,sum(id) as all");
         
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=mealsRecordService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('meal:mealsrecord:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<MealsRecordEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				MealsRecordEntity.class);
		queryWrapper.orderByDesc("id");
		
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<MealsRecordEntity> mealsRecordList = mealsRecordService.list(queryWrapper);
		ExcelUtil<MealsRecordEntity> util = new ExcelUtil<>(MealsRecordEntity.class);
		util.exportExcel(response, mealsRecordList, "参数数据");
	}
   
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:info')")
    public R<MealsRecordVO> detail(@PathVariable("id") Integer id){
		MealsRecordEntity mealsRecord = mealsRecordService.getById(id);
	    return R.data(MealsRecordWrapper.build().entityVO(mealsRecord));
    }
     /**
     * 保存&更新 
     */
    @Log(title = "餐饮记录表", businessType = BusinessType.ADDANDEDIT)
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:save')")
    public R<MealsRecordVO> submit(@Valid @RequestBody MealsRecordVO mealsRecord){
    	 if(mealsRecord.getId()==null) {
           return add(mealsRecord);
         }else {
           return edit(mealsRecord);
        }
    }
    
    @Log(title = "餐饮记录表", businessType = BusinessType.INSERT)
    @Operation(summary = "新增")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:save')")
    public R<MealsRecordVO> add(@Valid @RequestBody MealsRecordVO mealsRecord){
   		if(mealsRecord.getId()==null) {
            mealsRecord.setCreateAt(new Date());
            mealsRecord.setIsDelete(0);
            mealsRecord.setUpdateAt(new Date());
        } 
    	// 专人专餐老人数组转逗号串持久化
    	mealsRecord.setResidentIdStr(joinIds(mealsRecord.getResidentIds()));
    	if(mealsRecordService.save(mealsRecord)) {
    		 return R.ok(mealsRecord);
    	 }
    	return R.error("数据错误");
    }
    
    @Log(title = "餐饮记录表", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新")
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:update')")
    public R<MealsRecordVO> edit(@Valid @RequestBody MealsRecordVO mealsRecord){
   		mealsRecord.setUpdateAt(new Date());
    	// 专人专餐老人数组转逗号串持久化
    	mealsRecord.setResidentIdStr(joinIds(mealsRecord.getResidentIds()));
    	if(mealsRecordService.updateById(mealsRecord)) {
    		 return R.ok(mealsRecord);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermi('meal:mealsrecord:delete')")
    @Log(title = "餐饮记录表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(mealsRecordService.removeByIds(Arrays.asList(ids)));
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
