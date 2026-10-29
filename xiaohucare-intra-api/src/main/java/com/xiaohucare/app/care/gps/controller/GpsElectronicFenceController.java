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
import com.xiaohucare.app.gps.entity.GpsElectronicFenceEntity;
import com.xiaohucare.app.gps.service.GpsElectronicFenceService;
import com.xiaohucare.app.gps.vo.GpsElectronicFenceVO;
import com.xiaohucare.app.gps.wrapper.GpsElectronicFenceWrapper;
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
 * 电子围栏表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpselectronicfence")
public class GpsElectronicFenceController extends BaseController{
    @Autowired
    private GpsElectronicFenceService gpsElectronicFenceService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:fence:list')")
    public R<VPage<GpsElectronicFenceVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsElectronicFenceEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsElectronicFenceEntity.class);
        queryWrapper.orderByDesc("id");
          
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<GpsElectronicFenceEntity> pages = gpsElectronicFenceService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(GpsElectronicFenceWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:fence:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<GpsElectronicFenceEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsElectronicFenceEntity.class);
         queryWrapper.select("count(id) as totalCount");
           
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=gpsElectronicFenceService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:fence:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsElectronicFenceEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsElectronicFenceEntity.class);
		queryWrapper.orderByDesc("id");
          
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsElectronicFenceEntity> gpsElectronicFenceList = gpsElectronicFenceService.list(queryWrapper);
		ExcelUtil<GpsElectronicFenceEntity> util = new ExcelUtil<>(GpsElectronicFenceEntity.class);
		util.exportExcel(response, gpsElectronicFenceList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:fence:detail')")
    public R<GpsElectronicFenceVO> detail(@PathVariable("id") Long id){
		GpsElectronicFenceEntity gpsElectronicFence = gpsElectronicFenceService.getById(id);
	    return R.data(GpsElectronicFenceWrapper.build().entityVO(gpsElectronicFence));
    }
    
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:fence:submit')")
    public R<GpsElectronicFenceVO> submit(@Valid @RequestBody GpsElectronicFenceVO gpsElectronicFence){
   		if(gpsElectronicFence.getId()==null) {
            gpsElectronicFence.setCreateAt(new Date());
            gpsElectronicFence.setIsDelete(0);
            // 自动生成围栏编码
            if(StringUtils.isEmpty(gpsElectronicFence.getFenceCode())) {
                gpsElectronicFence.setFenceCode("FENCE_" + System.currentTimeMillis());
            }
        } 
        gpsElectronicFence.setUpdateAt(new Date());
    	if(gpsElectronicFenceService.saveOrUpdate(gpsElectronicFence)) {
    		 return R.ok(gpsElectronicFence);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:fence:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsElectronicFenceService.removeByIds(Arrays.asList(ids)));
    }

}
