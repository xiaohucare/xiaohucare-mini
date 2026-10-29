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
import com.xiaohucare.app.care.entity.BuildingEntity;
import com.xiaohucare.app.care.service.BuildingService;
import com.xiaohucare.app.care.vo.BuildingVO;
import com.xiaohucare.app.care.wrapper.BuildingWrapper;
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
 * 康养楼栋表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/hotel/building")
public class BuildingController extends BaseController{
    @Autowired
    private BuildingService buildingService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "buildingName", description = "楼栋名称"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:building:list')")
    public R<VPage<BuildingVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<BuildingEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("buildingName"), BuildingEntity.class);
        
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("buildingName")),"building_name", params.get("buildingName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")),"status", params.get("status"));
        IPage<BuildingEntity> pages = buildingService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(BuildingWrapper.build().toVPageVO(pages));
    }
    /**
     * 全量列表（供房态树/下拉选择）
     */
    @Operation(summary = "全量列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('care:building:list')")
    public R<List<BuildingVO>> list(){
        QueryWrapper<BuildingEntity> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
        List<BuildingEntity> list = buildingService.list(queryWrapper);
        return R.data(BuildingWrapper.build().listVO(list));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:building:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<BuildingEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("buildingName"), BuildingEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("buildingName")),"building_name", params.get("buildingName"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );
        }
        Map<String, Object>  stat=buildingService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "楼栋管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:building:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<BuildingEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("buildingName"),
				BuildingEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("buildingName")), "building_name", params.get("buildingName"));
		 List<BuildingEntity> buildingList = buildingService.list(queryWrapper);
		ExcelUtil<BuildingEntity> util = new ExcelUtil<>(BuildingEntity.class);
		util.exportExcel(response, buildingList, "楼栋数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:building:detail')")
    public R<BuildingVO> detail(@PathVariable("id") Long id){
		BuildingEntity building = buildingService.getById(id);
	    return R.data(BuildingWrapper.build().entityVO(building));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:building:submit')")
    public R<BuildingVO> submit(@Valid @RequestBody BuildingVO building){
   		if(building.getId()==null) {
            building.setCreateAt(new Date());
            building.setIsDelete(0);
        }
        building.setUpdateAt(new Date());
    	if(buildingService.saveOrUpdate(building)) {
    		 return R.ok(building);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:building:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(buildingService.removeByIds(Arrays.asList(ids)));
    }

}
