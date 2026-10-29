package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
import com.xiaohucare.app.care.entity.DailyArrangementEntity;
import com.xiaohucare.app.care.service.DailyArrangementService;
import com.xiaohucare.app.care.vo.DailyArrangementVO;
import com.xiaohucare.app.care.wrapper.DailyArrangementWrapper;
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
 * 宠物酒店日常安排表
 * @author xiaohucare
 * @date 2026-04-07 08:33:19
 */
@RestController
@RequestMapping("intra/care/dailyarrangement")
public class DailyArrangementController extends BaseController{
    @Autowired
    private DailyArrangementService dailyArrangementService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<VPage<DailyArrangementVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<DailyArrangementEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), DailyArrangementEntity.class);
        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<DailyArrangementEntity> pages = dailyArrangementService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(DailyArrangementWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<DailyArrangementEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), DailyArrangementEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=dailyArrangementService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<DailyArrangementEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				DailyArrangementEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<DailyArrangementEntity> dailyArrangementList = dailyArrangementService.list(queryWrapper);
		ExcelUtil<DailyArrangementEntity> util = new ExcelUtil<>(DailyArrangementEntity.class);
		util.exportExcel(response, dailyArrangementList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:detail')")
    public R<DailyArrangementVO> detail(@PathVariable("id") Long id){
		DailyArrangementEntity dailyArrangement = dailyArrangementService.getById(id);
	    return R.data(DailyArrangementWrapper.build().entityVO(dailyArrangement));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:submit')")
    public R<DailyArrangementVO> submit(@Valid @RequestBody DailyArrangementVO dailyArrangement){
   		if(dailyArrangement.getId()==null) {
            dailyArrangement.setCreateAt(new Date());
            dailyArrangement.setIsDelete(0);
        } 
        dailyArrangement.setUpdateAt(new Date());
    	if(dailyArrangementService.saveOrUpdate(dailyArrangement)) {
    		 return R.ok(dailyArrangement);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(dailyArrangementService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 获取一周日程安排
     * 优先获取特定日程(kind=1)，如果不存在则获取通用日程(kind=0)
     */
    @Operation(summary = "获取一周日程安排")
    @GetMapping("/getWeekSchedule")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<List<DailyArrangementVO>> getWeekSchedule(@RequestParam Map<String, Object> params){
        try {
            // 获取当前日期是周几 (1-7, 周一到周日)
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            // Java的DAY_OF_WEEK: 1=周日, 2=周一, ..., 7=周六
            // 转换为: 1=周一, 2=周二, ..., 7=周日
            int currentWeek = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;
            
            // 获取当前日期
            Date today = calendar.getTime();
            
            // 1. 先查询今天是否有特定日程(kind=1, week=当前周几)
            QueryWrapper<DailyArrangementEntity> specificQuery = new QueryWrapper<>();
            specificQuery.eq("kind", 1);
            specificQuery.eq("week", currentWeek);
            specificQuery.eq("status", 0);
            specificQuery.eq("is_delete", 0);
            specificQuery.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> specificList = dailyArrangementService.list(specificQuery);
            
            // 2. 如果存在特定日程，返回特定日程
            if (specificList != null && !specificList.isEmpty()) {
                return R.data(DailyArrangementWrapper.build().listVO(specificList));
            }
            
            // 3. 否则返回通用日程(kind=0)
            QueryWrapper<DailyArrangementEntity> generalQuery = new QueryWrapper<>();
            generalQuery.eq("kind", 0);
            generalQuery.eq("week", currentWeek);
            generalQuery.eq("status", 0);
            generalQuery.eq("is_delete", 0);
            generalQuery.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> generalList = dailyArrangementService.list(generalQuery);
            
            return R.data(DailyArrangementWrapper.build().listVO(generalList));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取日程失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取指定日期的日程安排
     * 优先获取特定日程(kind=1 + arrangementDate)，如果不存在则根据日期计算周几获取通用日程(kind=0 + week)
     */
    @Operation(summary = "获取指定日期日程")
    @Parameters({
        @Parameter(name = "arrangementDate", description = "安排日期(yyyy-MM-dd)", required = true)
    })
    @GetMapping("/getDaySchedule")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<List<DailyArrangementVO>> getDaySchedule(@RequestParam String arrangementDate){
        try {
            if (StringUtils.isEmpty(arrangementDate)) {
                return R.error("日期参数不能为空");
            }
            
            // 解析日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(arrangementDate);
            
            // 计算该日期是周几
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            // Java的DAY_OF_WEEK: 1=周日, 2=周一, ..., 7=周六
            // 转换为: 1=周一, 2=周二, ..., 7=周日
            int week = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;
            
            // 1. 先查询该日期是否有特定日程(kind=1 + arrangementDate)
            QueryWrapper<DailyArrangementEntity> specificQuery = new QueryWrapper<>();
            specificQuery.eq("kind", 1);
            specificQuery.eq("arrangement_date", date);
            specificQuery.eq("status", 0);
            specificQuery.eq("is_delete", 0);
            specificQuery.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> specificList = dailyArrangementService.list(specificQuery);
            
            // 2. 如果存在特定日程，返回特定日程
            if (specificList != null && !specificList.isEmpty()) {
                return R.data(DailyArrangementWrapper.build().listVO(specificList));
            }
            
            // 3. 否则根据周几获取通用日程(kind=0 + week)
            QueryWrapper<DailyArrangementEntity> generalQuery = new QueryWrapper<>();
            generalQuery.eq("kind", 0);
            generalQuery.eq("week", week);
            generalQuery.eq("status", 0);
            generalQuery.eq("is_delete", 0);
            generalQuery.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> generalList = dailyArrangementService.list(generalQuery);
            
            return R.data(DailyArrangementWrapper.build().listVO(generalList));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取日程失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取指定周几的通用日程安排（kind=0）
     */
    @Operation(summary = "获取通用日程")
    @Parameters({
        @Parameter(name = "week", description = "周几(1-7，1=周一，7=周日)", required = true)
    })
    @GetMapping("/getGeneralSchedule")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<List<DailyArrangementVO>> getGeneralSchedule(@RequestParam Integer week){
        try {
            if (week == null || week < 1 || week > 7) {
                return R.error("周几参数无效，请输入1-7");
            }
            
            // 查询指定周几的通用日程(kind=0 + week)
            QueryWrapper<DailyArrangementEntity> query = new QueryWrapper<>();
            query.eq("kind", 0);
            query.eq("week", week);
            query.eq("status", 0);
            query.eq("is_delete", 0);
            query.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> list = dailyArrangementService.list(query);
            
            return R.data(DailyArrangementWrapper.build().listVO(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取通用日程失败：" + e.getMessage());
        }
    }
/**
     * 获取指定日期的日程安排
     * 优先获取特定日程(kind=1 + arrangementDate)，如果不存在则根据日期计算周几获取通用日程(kind=0 + week)
     */
    @Operation(summary = "获取指定日期日程")
    @Parameters({
        @Parameter(name = "arrangementDate", description = "安排日期(yyyy-MM-dd)", required = true)
    })
    @GetMapping("/getDayScheduleByDate")
    @PreAuthorize("@ss.hasPermi('care:dailyarrangement:list')")
    public R<List<DailyArrangementVO>> getDayScheduleByDate(@RequestParam String arrangementDate){
        try {
            if (StringUtils.isEmpty(arrangementDate)) {
                return R.error("日期参数不能为空");
            }
            
            // 解析日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(arrangementDate);
            
            
            // 1. 先查询该日期是否有特定日程(kind=1 + arrangementDate)
            QueryWrapper<DailyArrangementEntity> specificQuery = new QueryWrapper<>();
            specificQuery.eq("kind", 1);
            specificQuery.eq("arrangement_date", date);
            specificQuery.eq("status", 0);
            specificQuery.eq("is_delete", 0);
            specificQuery.orderByAsc("arrangement_time");
            List<DailyArrangementEntity> specificList = dailyArrangementService.list(specificQuery);
            
             return R.data(DailyArrangementWrapper.build().listVO(specificList));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取日程失败：" + e.getMessage());
        }
    }
    
    
    
}
