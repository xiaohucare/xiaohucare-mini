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
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.service.SalesItemService;
import com.xiaohucare.app.care.wms.vo.SalesItemVO;
import com.xiaohucare.app.care.wms.wrapper.SalesItemWrapper;
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
 * 出货明细表
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/salesitem")
public class SalesItemController extends BaseController{
    @Autowired
    private SalesItemService salesItemService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:salesitem:list')")
    public R<VPage<SalesItemVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<SalesItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SalesItemEntity.class);
        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<SalesItemEntity> pages = salesItemService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SalesItemWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:salesitem:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SalesItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SalesItemEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=salesItemService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:salesitem:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SalesItemEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SalesItemEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SalesItemEntity> salesItemList = salesItemService.list(queryWrapper);
		ExcelUtil<SalesItemEntity> util = new ExcelUtil<>(SalesItemEntity.class);
		util.exportExcel(response, salesItemList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:salesitem:query')")
    public R<SalesItemVO> detail(@PathVariable("id") Long id){
		SalesItemEntity salesItem = salesItemService.getById(id);
	    return R.data(SalesItemWrapper.build().entityVO(salesItem));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:salesitem:add,wms:salesitem:edit')")
    public R<SalesItemVO> submit(@Valid @RequestBody SalesItemVO salesItem){
   		if(salesItem.getId()==null) {
            salesItem.setCreateAt(new Date());
            salesItem.setIsDelete(0);
        } 
        salesItem.setUpdateAt(new Date());
    	if(salesItemService.saveOrUpdate(salesItem)) {
    		 return R.ok(salesItem);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:salesitem:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(salesItemService.removeByIds(Arrays.asList(ids)));
    }

}
