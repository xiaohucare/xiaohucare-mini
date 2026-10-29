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
import com.xiaohucare.app.gps.entity.GpsActivityStatisticsEntity;
import com.xiaohucare.app.gps.service.GpsActivityStatisticsService;
import com.xiaohucare.app.gps.vo.GpsActivityStatisticsVO;
import com.xiaohucare.app.gps.wrapper.GpsActivityStatisticsWrapper;
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
 * 宠物活动统计表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpsactivitystatistics")
public class GpsActivityStatisticsController extends BaseController{
    @Autowired
    private GpsActivityStatisticsService gpsActivityStatisticsService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:list')")
    public R<VPage<GpsActivityStatisticsVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsActivityStatisticsEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsActivityStatisticsEntity.class);
        queryWrapper.orderByDesc("id");
          
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<GpsActivityStatisticsEntity> pages = gpsActivityStatisticsService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(GpsActivityStatisticsWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<GpsActivityStatisticsEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsActivityStatisticsEntity.class);
         queryWrapper.select("count(id) as totalCount");
           
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=gpsActivityStatisticsService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsActivityStatisticsEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsActivityStatisticsEntity.class);
		queryWrapper.orderByDesc("id");
          
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsActivityStatisticsEntity> gpsActivityStatisticsList = gpsActivityStatisticsService.list(queryWrapper);
		ExcelUtil<GpsActivityStatisticsEntity> util = new ExcelUtil<>(GpsActivityStatisticsEntity.class);
		util.exportExcel(response, gpsActivityStatisticsList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:detail')")
    public R<GpsActivityStatisticsVO> detail(@PathVariable("id") Long id){
		GpsActivityStatisticsEntity gpsActivityStatistics = gpsActivityStatisticsService.getById(id);
	    return R.data(GpsActivityStatisticsWrapper.build().entityVO(gpsActivityStatistics));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:submit')")
    public R<GpsActivityStatisticsVO> submit(@Valid @RequestBody GpsActivityStatisticsVO gpsActivityStatistics){
   		if(gpsActivityStatistics.getId()==null) {
            gpsActivityStatistics.setCreateAt(new Date());
            gpsActivityStatistics.setIsDelete(0);
        } 
        gpsActivityStatistics.setUpdateAt(new Date());
    	if(gpsActivityStatisticsService.saveOrUpdate(gpsActivityStatistics)) {
    		 return R.ok(gpsActivityStatistics);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:gpsactivitystatistics:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsActivityStatisticsService.removeByIds(Arrays.asList(ids)));
    }

}
