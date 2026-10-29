package com.xiaohucare.app.care.activity.controller;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.activity.entity.ActivityParticipantEntity;
import com.xiaohucare.app.care.activity.service.ActivityParticipantService;
import com.xiaohucare.app.care.activity.vo.ActivityParticipantVO;
import com.xiaohucare.app.care.activity.wrapper.ActivityParticipantWrapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
/**
 * 活动参与记录表
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
@Tag(name = "活动参与记录表")
@RestController
@RequestMapping("intra/activity/activityparticipant")
public class ActivityParticipantController extends BaseController{
    @Autowired
    private ActivityParticipantService activityParticipantService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:list')")
    public R<VPage<ActivityParticipantVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ActivityParticipantEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), ActivityParticipantEntity.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<ActivityParticipantEntity> pages = activityParticipantService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ActivityParticipantWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<ActivityParticipantEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), ActivityParticipantEntity.class);
         queryWrapper.select("count(id) as total,sum(id) as all");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=activityParticipantService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('activity:activityparticipant:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ActivityParticipantEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				ActivityParticipantEntity.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<ActivityParticipantEntity> activityParticipantList = activityParticipantService.list(queryWrapper);
		ExcelUtil<ActivityParticipantEntity> util = new ExcelUtil<>(ActivityParticipantEntity.class);
		util.exportExcel(response, activityParticipantList, "参数数据");
	}
   
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:info')")
    public R<ActivityParticipantVO> detail(@PathVariable("id") Integer id){
		ActivityParticipantEntity activityParticipant = activityParticipantService.getById(id);
	    return R.data(ActivityParticipantWrapper.build().entityVO(activityParticipant));
    }
     /**
     * 保存&更新 
     */
    @Log(title = "活动参与记录表", businessType = BusinessType.ADDANDEDIT)
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:save')")
    public R<ActivityParticipantVO> submit(@Valid @RequestBody ActivityParticipantVO activityParticipant){
    	 if(activityParticipant.getId()==null) {
           return add(activityParticipant);
         }else {
           return edit(activityParticipant);
        }
    }
    
    @Log(title = "活动参与记录表", businessType = BusinessType.INSERT)
    @Operation(summary = "新增")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:save')")
    public R<ActivityParticipantVO> add(@Valid @RequestBody ActivityParticipantVO activityParticipant){
   		if(activityParticipant.getId()==null) {
            activityParticipant.setCreateAt(new Date());
            activityParticipant.setIsDelete(0);
            activityParticipant.setUpdateAt(new Date());
        } 
    	if(activityParticipantService.save(activityParticipant)) {
    		 return R.ok(activityParticipant);
    	 }
    	return R.error("数据错误");
    }
    
    @Log(title = "活动参与记录表", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新")
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:update')")
    public R<ActivityParticipantVO> edit(@Valid @RequestBody ActivityParticipantVO activityParticipant){
   		activityParticipant.setUpdateAt(new Date());
    	if(activityParticipantService.updateById(activityParticipant)) {
    		 return R.ok(activityParticipant);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermi('activity:activityparticipant:delete')")
    @Log(title = "活动参与记录表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(activityParticipantService.removeByIds(Arrays.asList(ids)));
    }

}
