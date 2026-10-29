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
import com.xiaohucare.app.gps.entity.GpsTrackEntity;
import com.xiaohucare.app.gps.service.GpsTrackService;
import com.xiaohucare.app.gps.vo.GpsTrackVO;
import com.xiaohucare.app.gps.wrapper.GpsTrackWrapper;
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
 * 宠物GPS轨迹表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpstrack")
public class GpsTrackController extends BaseController{
    @Autowired
    private GpsTrackService gpsTrackService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:track:list')")
    public R<VPage<GpsTrackVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsTrackEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsTrackEntity.class);
        queryWrapper.orderByDesc("id");
          
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
         // 支持 startTime 和 endTime 参数（优先级高于 createDate）
        if (params.containsKey("startTime") && params.containsKey("endTime")) {
            String startTimeStr = params.get("startTime") != null ? params.get("startTime").toString() : "";
            String endTimeStr = params.get("endTime") != null ? params.get("endTime").toString() : "";
            if (!startTimeStr.isEmpty() && !endTimeStr.isEmpty()) {
                try {
                    queryWrapper.between("create_at",
                            DateUtils.str_ToDate(startTimeStr),
                            DateUtils.str_ToDate(endTimeStr)
                    );
                } catch (Exception e) {
                    // 如果日期解析失败，记录日志并继续
                    System.err.println("日期解析失败: " + e.getMessage());
                }
            }
        } else if (params.containsKey("createDate")) {
            String createDateStr = params.get("createDate") != null ? params.get("createDate").toString() : "";
            if (!createDateStr.isEmpty()) {
                try {
                    queryWrapper.between("create_at",
                            DateUtils.getStartOfDate(DateUtils.str_ToDate(createDateStr)),
                            DateUtils.getEndOfDate(DateUtils.str_ToDate(createDateStr))
                    );
                } catch (Exception e) {
                    // 如果日期解析失败，记录日志并继续
                    System.err.println("日期解析失败: " + e.getMessage());
                }
            }
        }
        IPage<GpsTrackEntity> pages = gpsTrackService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(GpsTrackWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:track:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException {
        QueryWrapper<GpsTrackEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"), GpsTrackEntity.class);
        // 查询统计信息：总数量、平均速度、最高速度
        queryWrapper.select("count(id) as totalCount,ifnull(avg(speed), 0) as avgSpeed,ifnull(max(speed), 0) as maxSpeed,ifnull(sum(speed), 0) as sumSpeed");
          
        queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
        // 支持 startTime 和 endTime 参数（优先级高于 createDate）
        if (params.containsKey("startTime") && params.containsKey("endTime")) {
            String startTimeStr = params.get("startTime") != null ? params.get("startTime").toString() : "";
            String endTimeStr = params.get("endTime") != null ? params.get("endTime").toString() : "";
            if (!startTimeStr.isEmpty() && !endTimeStr.isEmpty()) {
                try {
                    queryWrapper.between("create_at",
                            DateUtils.str_ToDate(startTimeStr),
                            DateUtils.str_ToDate(endTimeStr)
                    );
                } catch (Exception e) {
                    // 如果日期解析失败，记录日志并继续
                    System.err.println("日期解析失败: " + e.getMessage());
                }
            }
        } else if (params.containsKey("createDate")) {
            String createDateStr = params.get("createDate") != null ? params.get("createDate").toString() : "";
            if (!createDateStr.isEmpty()) {
                try {
                    queryWrapper.between("create_at",
                            DateUtils.getStartOfDate(DateUtils.str_ToDate(createDateStr)),
                            DateUtils.getEndOfDate(DateUtils.str_ToDate(createDateStr))
                    );
                } catch (Exception e) {
                    // 如果日期解析失败，记录日志并继续
                    System.err.println("日期解析失败: " + e.getMessage());
                }
            }
        }
        Map<String, Object> stat = gpsTrackService.getMap(queryWrapper);
        
        // 计算总里程（基于平均速度和时间间隔的估算）
        // 注意：实际应用中可能需要更精确的地理距离计算
        double totalCount = Double.parseDouble(stat.getOrDefault("totalCount", "0").toString());
        double avgSpeed = Double.parseDouble(stat.getOrDefault("avgSpeed", "0").toString());
        
        // 简单估算：假设平均每小时行驶，总里程 = 平均速度 * 小时数
        // 这里使用一个合理的默认值，实际项目中应根据业务需求调整
        double totalDistance = 0;
        if (totalCount > 0) {
            // 基于轨迹点数量和平均速度的粗略估算
            totalDistance = avgSpeed * (totalCount / 10.0); // 每10个点约1公里，可根据实际情况调整
        }
        
        stat.put("totalDistance", Math.round(totalDistance * 100.0) / 100.0);
        
        // 确保所有需要的字段都存在
        stat.putIfAbsent("totalCount", 0);
        stat.putIfAbsent("totalDistance", 0.0);
        stat.putIfAbsent("avgSpeed", 0.0);
        stat.putIfAbsent("maxSpeed", 0.0);
        
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:gpstrack:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsTrackEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsTrackEntity.class);
                  
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsTrackEntity> gpsTrackList = gpsTrackService.list(queryWrapper);
		ExcelUtil<GpsTrackEntity> util = new ExcelUtil<>(GpsTrackEntity.class);
		util.exportExcel(response, gpsTrackList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:gpstrack:detail')")
    public R<GpsTrackVO> detail(@PathVariable("id") Long id){
		GpsTrackEntity gpsTrack = gpsTrackService.getById(id);
	    return R.data(GpsTrackWrapper.build().entityVO(gpsTrack));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:gpstrack:submit')")
    public R<GpsTrackVO> submit(@Valid @RequestBody GpsTrackVO gpsTrack){
   		if(gpsTrack.getId()==null) {
            gpsTrack.setCreateAt(new Date());
            gpsTrack.setIsDelete(0);
        } 
    	if(gpsTrackService.saveOrUpdate(gpsTrack)) {
    		 return R.ok(gpsTrack);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:gpstrack:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsTrackService.removeByIds(Arrays.asList(ids)));
    }

}
