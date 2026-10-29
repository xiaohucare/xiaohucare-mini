package com.xiaohucare.app.care.gps.controller;
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
import com.xiaohucare.app.gps.entity.GpsFenceCameraLinkEntity;
import com.xiaohucare.app.gps.service.GpsFenceCameraLinkService;
import com.xiaohucare.app.gps.vo.GpsFenceCameraLinkVO;
import com.xiaohucare.app.gps.wrapper.GpsFenceCameraLinkWrapper;
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
 * 围栏-摄像头联动配置表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpsfencecameralink")
public class GpsFenceCameraLinkController extends BaseController{
    @Autowired
    private GpsFenceCameraLinkService gpsFenceCameraLinkService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:fencecameralink:list')")
    public R<VPage<GpsFenceCameraLinkVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsFenceCameraLinkEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsFenceCameraLinkEntity.class);
        queryWrapper.orderByDesc("id");
          
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<GpsFenceCameraLinkEntity> pages = gpsFenceCameraLinkService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(GpsFenceCameraLinkWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:fencecameralink:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<GpsFenceCameraLinkEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsFenceCameraLinkEntity.class);
         queryWrapper.select("count(id) as totalCount");
           
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=gpsFenceCameraLinkService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:fencecameralink:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsFenceCameraLinkEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsFenceCameraLinkEntity.class);
		queryWrapper.orderByDesc("id");
          
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsFenceCameraLinkEntity> gpsFenceCameraLinkList = gpsFenceCameraLinkService.list(queryWrapper);
		ExcelUtil<GpsFenceCameraLinkEntity> util = new ExcelUtil<>(GpsFenceCameraLinkEntity.class);
		util.exportExcel(response, gpsFenceCameraLinkList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:fencecameralink:detail')")
    public R<GpsFenceCameraLinkVO> detail(@PathVariable("id") Long id){
		GpsFenceCameraLinkEntity gpsFenceCameraLink = gpsFenceCameraLinkService.getById(id);
	    return R.data(GpsFenceCameraLinkWrapper.build().entityVO(gpsFenceCameraLink));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:fencecameralink:submit')")
    public R<GpsFenceCameraLinkVO> submit(@Valid @RequestBody GpsFenceCameraLinkVO gpsFenceCameraLink){
   		if(gpsFenceCameraLink.getId()==null) {
            gpsFenceCameraLink.setCreateAt(new Date());
            gpsFenceCameraLink.setIsDelete(0);
        } 
        gpsFenceCameraLink.setUpdateAt(new Date());
    	if(gpsFenceCameraLinkService.saveOrUpdate(gpsFenceCameraLink)) {
    		 return R.ok(gpsFenceCameraLink);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:fencecameralink:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsFenceCameraLinkService.removeByIds(Arrays.asList(ids)));
    }

}
