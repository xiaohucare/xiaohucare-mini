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
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.service.RoomService;
import com.xiaohucare.app.care.vo.RoomUsageVO;
import com.xiaohucare.app.care.vo.RoomVO;
import com.xiaohucare.app.care.wrapper.RoomWrapper;
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
 * 康养房间表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/hotel/room")
public class RoomController extends BaseController{
    @Autowired
    private RoomService roomService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "roomName", description = "房间名称"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:room:list')")
    public R<VPage<RoomVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<RoomEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("roomName"), RoomEntity.class);
        
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
       queryWrapper.like(StringUtils.isNotNull(params.get("roomName")),"room_name", params.get("roomName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("buildingId")),"building_id", params.get("buildingId"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("floorId")),"floor_id", params.get("floorId"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")),"status", params.get("status"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("roomType")),"room_type", params.get("roomType"));
        IPage<RoomEntity> pages = roomService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(RoomWrapper.build().toVPageVO(pages));
    }
    /**
     * 按楼层查询房间（供房态视图）
     */
    @Operation(summary = "按楼层查询房间")
    @GetMapping("/listByFloor/{floorId}")
    @PreAuthorize("@ss.hasPermi('care:room:list')")
    public R<List<RoomVO>> listByFloor(@PathVariable("floorId") Long floorId){
        QueryWrapper<RoomEntity> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.eq("floor_id", floorId);
        queryWrapper.orderByAsc("sort_order").orderByAsc("room_no");
        List<RoomEntity> list = roomService.list(queryWrapper);
        return R.data(RoomWrapper.build().listVO(list));
    }
    /**
     * 房态统计（按楼栋/楼层聚合各状态房间数）
     */
    @Operation(summary = "房态统计")
    @GetMapping("/roomUsage")
    @PreAuthorize("@ss.hasPermi('care:room:list')")
    public R<Map<Integer, RoomUsageVO>> roomUsage(@RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long floorId){
        return R.data(roomService.statRoomUsage(buildingId, floorId));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:room:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<RoomEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("roomName"), RoomEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("roomName")),"room_name", params.get("roomName"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );
        }
        Map<String, Object>  stat=roomService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "房间管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:room:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<RoomEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("roomName"),
				RoomEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("roomName")), "room_name", params.get("roomName"));
		 List<RoomEntity> roomList = roomService.list(queryWrapper);
		ExcelUtil<RoomEntity> util = new ExcelUtil<>(RoomEntity.class);
		util.exportExcel(response, roomList, "房间数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:room:detail')")
    public R<RoomVO> detail(@PathVariable("id") Long id){
		RoomEntity room = roomService.getById(id);
	    return R.data(RoomWrapper.build().entityVO(room));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:room:submit')")
    public R<RoomVO> submit(@Valid @RequestBody RoomVO room){
   		if(room.getId()==null) {
            room.setCreateAt(new Date());
            room.setIsDelete(0);
        }
        room.setUpdateAt(new Date());
    	if(roomService.saveOrUpdate(room)) {
    		 return R.ok(room);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:room:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(roomService.removeByIds(Arrays.asList(ids)));
    }

}
