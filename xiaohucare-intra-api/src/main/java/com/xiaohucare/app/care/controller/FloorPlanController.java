package com.xiaohucare.app.care.controller;
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
import com.xiaohucare.app.care.entity.FloorPlanEntity;
import com.xiaohucare.app.care.service.FloorPlanService;
import com.xiaohucare.app.care.vo.FloorPlanVO;
import com.xiaohucare.app.care.wrapper.FloorPlanWrapper;
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
 * 康养楼层表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/hotel/floorplan")
public class FloorPlanController extends BaseController{
    @Autowired
    private FloorPlanService floorPlanService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "floorName", description = "楼层名称"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:floorplan:list')")
    public R<VPage<FloorPlanVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<FloorPlanEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("floorName"), FloorPlanEntity.class);
        
        queryWrapper.orderByAsc("floor_no").orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("floorName")),"floor_name", params.get("floorName"));
        IPage<FloorPlanEntity> pages = floorPlanService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(FloorPlanWrapper.build().toVPageVO(pages));
    }
    /**
     * 按楼栋查询楼层（供房态树/下拉选择）
     */
    @Operation(summary = "按楼栋查询楼层")
    @GetMapping("/listByBuilding/{buildingId}")
    @PreAuthorize("@ss.hasPermi('care:floorplan:list')")
    public R<List<FloorPlanVO>> listByBuilding(@PathVariable("buildingId") Long buildingId){
        QueryWrapper<FloorPlanEntity> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.eq("building_id", buildingId);
        queryWrapper.orderByAsc("floor_no").orderByDesc("id");
        List<FloorPlanEntity> list = floorPlanService.list(queryWrapper);
        return R.data(FloorPlanWrapper.build().listVO(list));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:floorplan:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<FloorPlanEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("floorName"), FloorPlanEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("floorName")),"floor_name", params.get("floorName"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );
        }
        Map<String, Object>  stat=floorPlanService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "楼层管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:floorplan:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<FloorPlanEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("floorName"),
				FloorPlanEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("floorName")), "floor_name", params.get("floorName"));
		 List<FloorPlanEntity> floorPlanList = floorPlanService.list(queryWrapper);
		ExcelUtil<FloorPlanEntity> util = new ExcelUtil<>(FloorPlanEntity.class);
		util.exportExcel(response, floorPlanList, "楼层数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:floorplan:detail')")
    public R<FloorPlanVO> detail(@PathVariable("id") Long id){
		FloorPlanEntity floorPlan = floorPlanService.getById(id);
	    return R.data(FloorPlanWrapper.build().entityVO(floorPlan));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:floorplan:submit')")
    public R<FloorPlanVO> submit(@Valid @RequestBody FloorPlanVO floorPlan){
   		if(floorPlan.getId()==null) {
            floorPlan.setCreateAt(new Date());
            floorPlan.setIsDelete(0);
        }
        floorPlan.setUpdateAt(new Date());
    	if(floorPlanService.saveOrUpdate(floorPlan)) {
    		 return R.ok(floorPlan);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:floorplan:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(floorPlanService.removeByIds(Arrays.asList(ids)));
    }

}
