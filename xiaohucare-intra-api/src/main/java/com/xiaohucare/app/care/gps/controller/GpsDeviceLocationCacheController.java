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
import com.xiaohucare.app.gps.entity.GpsDeviceLocationCacheEntity;
import com.xiaohucare.app.gps.service.GpsDeviceLocationCacheService;
import com.xiaohucare.app.gps.vo.GpsDeviceLocationCacheVO;
import com.xiaohucare.app.gps.wrapper.GpsDeviceLocationCacheWrapper;
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
 * 设备位置缓存表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpsdevicelocationcache")
public class GpsDeviceLocationCacheController extends BaseController{
    @Autowired
    private GpsDeviceLocationCacheService gpsDeviceLocationCacheService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:locationcache:list')")
    public R<VPage<GpsDeviceLocationCacheVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsDeviceLocationCacheEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsDeviceLocationCacheEntity.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<GpsDeviceLocationCacheEntity> pages = gpsDeviceLocationCacheService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(GpsDeviceLocationCacheWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:locationcache:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<GpsDeviceLocationCacheEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsDeviceLocationCacheEntity.class);
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=gpsDeviceLocationCacheService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:locationcache:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsDeviceLocationCacheEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsDeviceLocationCacheEntity.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsDeviceLocationCacheEntity> gpsDeviceLocationCacheList = gpsDeviceLocationCacheService.list(queryWrapper);
		ExcelUtil<GpsDeviceLocationCacheEntity> util = new ExcelUtil<>(GpsDeviceLocationCacheEntity.class);
		util.exportExcel(response, gpsDeviceLocationCacheList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:locationcache:detail')")
    public R<GpsDeviceLocationCacheVO> detail(@PathVariable("id") Long id){
		GpsDeviceLocationCacheEntity gpsDeviceLocationCache = gpsDeviceLocationCacheService.getById(id);
	    return R.data(GpsDeviceLocationCacheWrapper.build().entityVO(gpsDeviceLocationCache));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:locationcache:submit')")
    public R<GpsDeviceLocationCacheVO> submit(@Valid @RequestBody GpsDeviceLocationCacheVO gpsDeviceLocationCache){
   		if(gpsDeviceLocationCache.getId()==null) {
            gpsDeviceLocationCache.setCreateAt(new Date());
            gpsDeviceLocationCache.setIsDelete(0);
        } 
        gpsDeviceLocationCache.setUpdateAt(new Date());
    	if(gpsDeviceLocationCacheService.saveOrUpdate(gpsDeviceLocationCache)) {
    		 return R.ok(gpsDeviceLocationCache);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:locationcache:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsDeviceLocationCacheService.removeByIds(Arrays.asList(ids)));
    }

}
