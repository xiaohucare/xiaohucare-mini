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
import com.xiaohucare.app.care.entity.BedEntity;
import com.xiaohucare.app.care.service.BedService;
import com.xiaohucare.app.care.vo.BedVO;
import com.xiaohucare.app.care.wrapper.BedWrapper;
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
 * 康养床位表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/hotel/bed")
public class BedController extends BaseController{
    @Autowired
    private BedService bedService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "bedName", description = "床位名称"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:bed:list')")
    public R<VPage<BedVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<BedEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("bedName"), BedEntity.class);
        
        queryWrapper.orderByAsc("sort_order").orderByAsc("bed_no");
        queryWrapper.like(StringUtils.isNotNull(params.get("bedName")),"bed_name", params.get("bedName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("buildingId")),"building_id", params.get("buildingId"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("floorId")),"floor_id", params.get("floorId"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("roomId")),"room_id", params.get("roomId"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("bedType")),"bed_type", params.get("bedType"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")),"status", params.get("status"));
        IPage<BedEntity> pages = bedService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(BedWrapper.build().toVPageVO(pages));
    }
    /**
     * 按房间查询床位（供房态视图床位格）
     */
    @Operation(summary = "按房间查询床位")
    @GetMapping("/listByRoom/{roomId}")
    @PreAuthorize("@ss.hasPermi('care:bed:list')")
    public R<List<BedVO>> listByRoom(@PathVariable("roomId") Long roomId){
        QueryWrapper<BedEntity> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.eq("room_id", roomId);
        queryWrapper.orderByAsc("sort_order").orderByAsc("bed_no");
        List<BedEntity> list = bedService.list(queryWrapper);
        return R.data(BedWrapper.build().listVO(list));
    }
    /**
     * 查询空闲床位（可按楼栋/楼层/房间/床位类型过滤）
     */
    @Operation(summary = "查询空闲床位")
    @GetMapping("/freeBeds")
    @PreAuthorize("@ss.hasPermi('care:bed:list')")
    public R<List<BedVO>> freeBeds(@RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long floorId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Integer bedType){
        List<BedEntity> list = bedService.listFreeBeds(buildingId, floorId, roomId, bedType);
        return R.data(BedWrapper.build().listVO(list));
    }
    /**
     * 换床（释放旧床并占用新床）
     */
    @Operation(summary = "换床")
    @PostMapping("/changeBed")
    @PreAuthorize("@ss.hasPermi('care:bed:submit')")
    public R<Boolean> changeBed(@RequestParam Long oldBedId, @RequestParam Long newBedId,
            @RequestParam Long residentId, @RequestParam(required = false) String residentName,
            @RequestParam(required = false) Long checkinId){
        return R.ok(bedService.changeBed(oldBedId, newBedId, residentId, residentName, checkinId));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:bed:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<BedEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("bedName"), BedEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("bedName")),"bed_name", params.get("bedName"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );
        }
        Map<String, Object>  stat=bedService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "床位管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:bed:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<BedEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("bedName"),
				BedEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("bedName")), "bed_name", params.get("bedName"));
		 List<BedEntity> bedList = bedService.list(queryWrapper);
		ExcelUtil<BedEntity> util = new ExcelUtil<>(BedEntity.class);
		util.exportExcel(response, bedList, "床位数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:bed:detail')")
    public R<BedVO> detail(@PathVariable("id") Long id){
		BedEntity bed = bedService.getById(id);
	    return R.data(BedWrapper.build().entityVO(bed));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:bed:submit')")
    public R<BedVO> submit(@Valid @RequestBody BedVO bed){
   		if(bed.getId()==null) {
            bed.setCreateAt(new Date());
            bed.setIsDelete(0);
        }
        bed.setUpdateAt(new Date());
    	if(bedService.saveOrUpdate(bed)) {
    		 return R.ok(bed);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:bed:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(bedService.removeByIds(Arrays.asList(ids)));
    }

}
