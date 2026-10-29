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
import com.xiaohucare.app.gps.entity.GpsDeviceEntity;
import com.xiaohucare.app.gps.service.GpsDeviceService;
import com.xiaohucare.app.gps.vo.GpsDeviceVO;
import com.xiaohucare.app.gps.wrapper.GpsDeviceWrapper;
import com.xiaohucare.common.core.redis.RedisUtil;
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
 * 宠物GPS设备表
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
@RestController
@RequestMapping("intra/gps/gpsdevice")
public class GpsDeviceController extends BaseController{
    @Autowired
    private GpsDeviceService gpsDeviceService;
    
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('gps:device:list')")
    public R<VPage<GpsDeviceVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<GpsDeviceEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsDeviceEntity.class);
        queryWrapper.orderByDesc("id");
          
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<GpsDeviceEntity> pages = gpsDeviceService.page(VQueryKit.getPage(params), queryWrapper);
        VPage<GpsDeviceVO> vPage = GpsDeviceWrapper.build().toVPageVO(pages);
        vPage.getList().forEach(item->{
            item.setLastAddress((String) redisUtil.hget("device:location:" + item.getId(), "address"));
            item.setLastUpdateAt((String) redisUtil.hget("device:location:" + item.getId(), "location_time"));
        });
        return R.data(vPage);
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('gps:device:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<GpsDeviceEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), GpsDeviceEntity.class);
         queryWrapper.select("count(id) as totalCount");
           
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=gpsDeviceService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('gps:device:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<GpsDeviceEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				GpsDeviceEntity.class);
		queryWrapper.orderByDesc("id");
          
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<GpsDeviceEntity> gpsDeviceList = gpsDeviceService.list(queryWrapper);
		ExcelUtil<GpsDeviceEntity> util = new ExcelUtil<>(GpsDeviceEntity.class);
		util.exportExcel(response, gpsDeviceList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('gps:device:detail')")
    public R<GpsDeviceVO> detail(@PathVariable("id") Long id){
		GpsDeviceEntity gpsDevice = gpsDeviceService.getById(id);
	    return R.data(GpsDeviceWrapper.build().entityVO(gpsDevice));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('gps:device:submit')")
    public R<GpsDeviceVO> submit(@Valid @RequestBody GpsDeviceVO gpsDevice){
   		if(gpsDevice.getId()==null) {
            gpsDevice.setCreateAt(new Date());
            gpsDevice.setIsDelete(0);
        } 
        gpsDevice.setUpdateAt(new Date());
    	if(gpsDeviceService.saveOrUpdate(gpsDevice)) {
    		 return R.ok(gpsDevice);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('gps:device:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(gpsDeviceService.removeByIds(Arrays.asList(ids)));
    }

}
