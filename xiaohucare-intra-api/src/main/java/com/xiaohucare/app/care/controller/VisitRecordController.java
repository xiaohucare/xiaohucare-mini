package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.xiaohucare.app.care.entity.VisitRecordEntity;
import com.xiaohucare.app.care.service.VisitRecordService;
import com.xiaohucare.app.care.vo.VisitRecordVO;
import com.xiaohucare.app.care.wrapper.VisitRecordWrapper;
import com.xiaohucare.app.care.entity.ResidentEntity;
import com.xiaohucare.app.care.service.ResidentService;
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
 * 来访/外出记录表
 * @author xiaohucare
 * @date 2025-11-23 22:33:31
 */
@Tag(name = "来访/外出记录表")
@RestController
@RequestMapping("intra/ginkgo/visitrecord")
public class VisitRecordController extends BaseController{
    @Autowired
    private VisitRecordService visitRecordService;
    @Autowired
    private ResidentService residentService;
    /**
     * 列表
     * visitType：0 来访 1 外出；visitorName/residentName 模糊；visitDate/leaveDate 精确；purpose/reason 模糊；status：1 外出中 0 已返回
     */
    @Operation(summary = "列表")
    @Parameters({
                @Parameter(name = "visitType", description = "类型：0来访 1外出"),
                @Parameter(name = "visitorName", description = "来访者姓名"),
                @Parameter(name = "residentName", description = "长者姓名"),
        })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:list')")
    public R<VPage<VisitRecordVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<VisitRecordEntity> queryWrapper = new QueryWrapper<>();
        
        // 类型：0 来访 1 外出
        queryWrapper.eq(params.get("visitType") != null, "visit_type", params.get("visitType"));
        // 来访者姓名 / 长者姓名 模糊搜索
        queryWrapper.like(StringUtils.isNotNull(params.get("visitorName")), "visitor_name", params.get("visitorName"));
        queryWrapper.like(StringUtils.isNotNull(params.get("residentName")), "resident_name", params.get("residentName"));
        // 日期（来访日期与外出日期共用 visit_date 列）
        queryWrapper.eq(StringUtils.isNotNull(params.get("visitDate")), "visit_date", params.get("visitDate"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("leaveDate")), "visit_date", params.get("leaveDate"));
        // 来访目的 / 外出事由
        queryWrapper.like(StringUtils.isNotNull(params.get("purpose")), "purpose", params.get("purpose"));
        queryWrapper.like(StringUtils.isNotNull(params.get("reason")), "reason", params.get("reason"));
        // 外出状态：1 外出中（实际返回为空），0 已返回（实际返回不为空）
        String status = params.get("status") == null ? null : params.get("status").toString();
        queryWrapper.isNull("1".equals(status), "actual_return_time");
        queryWrapper.isNotNull("0".equals(status), "actual_return_time");
        queryWrapper.orderByDesc("id");
        IPage<VisitRecordEntity> pages = visitRecordService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(VisitRecordWrapper.build().toVPageVO(pages));
    }

    /**
     * 总统计：今日来访人数，今日外出人数，总来访人数，总外出次数
     */
    @Operation(summary = "总统计")
    @GetMapping("/totalStat")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:list')")
    public R<Map<String, Object>> totalStat() {
        Map<String, Object> result = new LinkedHashMap<>();

        // 获取今天的开始和结束时间
        Date todayStart = DateUtils.getStartOfDate(new Date());
        Date todayEnd = DateUtils.getEndOfDate(new Date());

        // 1. 今日来访人数 (visitType = 0 且来访日期在今天)
        QueryWrapper<VisitRecordEntity> todayVisitQuery = new QueryWrapper<>();
        todayVisitQuery.eq("visit_type", 0);
        todayVisitQuery.between("visit_date", todayStart, todayEnd);
        long todayVisitCount = visitRecordService.count(todayVisitQuery);
        result.put("todayVisitCount", todayVisitCount);

        // 2. 今日外出人数 (visitType = 1 且来访日期在今天)
        QueryWrapper<VisitRecordEntity> todayOutQuery = new QueryWrapper<>();
        todayOutQuery.eq("visit_type", 1);
        todayOutQuery.between("visit_date", todayStart, todayEnd);
        long todayOutCount = visitRecordService.count(todayOutQuery);
        result.put("todayOutCount", todayOutCount);

        // 3. 总来访人数 (visitType = 0)
        QueryWrapper<VisitRecordEntity> totalVisitQuery = new QueryWrapper<>();
        totalVisitQuery.eq("visit_type", 0);
        long totalVisitCount = visitRecordService.count(totalVisitQuery);
        result.put("totalVisitCount", totalVisitCount);

        // 4. 总外出次数 (visitType = 1)
        QueryWrapper<VisitRecordEntity> totalOutQuery = new QueryWrapper<>();
        totalOutQuery.eq("visit_type", 1);
        long totalOutCount = visitRecordService.count(totalOutQuery);
        result.put("totalOutCount", totalOutCount);

        return R.data(result);
    }

    /**
     * 近期来访趋势：按天统计最近30天，来访和外出数量
     */
    @Operation(summary = "近期来访趋势")
    @GetMapping("/trendStat")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:list')")
    public R<List<Map<String, Object>>> trendStat() {
        // 获取当前日期和30天前的日期
        Date currentDate = new Date();
        Date thirtyDaysAgo = DateUtils.addDays(currentDate, -29); // 包含今天，共30天
        Date startOfPeriod = DateUtils.getStartOfDate(thirtyDaysAgo);
        Date endOfPeriod = DateUtils.getEndOfDate(currentDate);

        // 构造查询条件
        QueryWrapper<VisitRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
            "DATE(visit_date) as stat_date",
            "visit_type",
            "COUNT(*) as count"
        );
        
        queryWrapper.between("visit_date", startOfPeriod, endOfPeriod);
        queryWrapper.groupBy("DATE(visit_date)", "visit_type");
        queryWrapper.orderByAsc("stat_date");

        // 执行分组统计查询
        List<Map<String, Object>> dbResults = visitRecordService.listMaps(queryWrapper);

        // 构造完整的30天数据，确保每天都有记录（即使为0）
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Map<String, Object>> dateMap = new LinkedHashMap<>();

        // 初始化30天的数据结构
        for (int i = 0; i < 30; i++) {
            Date date = DateUtils.addDays(thirtyDaysAgo, i);
            String dateStr = DateUtils.parseDateToStr("yyyy-MM-dd", date);
            Map<String, Object> dayStat = new LinkedHashMap<>();
            dayStat.put("date", dateStr);
            dayStat.put("visitCount", 0L);
            dayStat.put("outCount", 0L);
            dateMap.put(dateStr, dayStat);
        }

        // 填充实际统计数据
        for (Map<String, Object> record : dbResults) {
            String statDate = String.valueOf(record.get("stat_date"));
            Object visitTypeObj = record.get("visit_type");
            Integer visitType = visitTypeObj == null ? null : Integer.valueOf(visitTypeObj.toString());
            Long count = ((Number) record.get("count")).longValue();

            if (dateMap.containsKey(statDate)) {
                Map<String, Object> dayStat = dateMap.get(statDate);
                if (visitType != null && visitType == 0) {
                    dayStat.put("visitCount", count);
                } else if (visitType != null && visitType == 1) {
                    dayStat.put("outCount", count);
                }
            }
        }

        // 转换为列表
        result.addAll(dateMap.values());

        return R.data(result);
    }

    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<VisitRecordEntity> queryWrapper = new QueryWrapper<>();
         queryWrapper.select("count(id) as total");
         
         queryWrapper.eq(params.get("visitType") != null, "visit_type", params.get("visitType"));
         if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );
        }
        Map<String, Object>  stat=visitRecordService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "来访/外出记录表", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<VisitRecordEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("id");
		
		queryWrapper.eq(params.get("visitType") != null, "visit_type", params.get("visitType"));
		 List<VisitRecordEntity> visitRecordList = visitRecordService.list(queryWrapper);
		ExcelUtil<VisitRecordEntity> util = new ExcelUtil<>(VisitRecordEntity.class);
		util.exportExcel(response, visitRecordList, "来访/外出记录");
	}

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:info')")
    public R<VisitRecordVO> detail(@PathVariable("id") Integer id){
		VisitRecordEntity visitRecord = visitRecordService.getById(id);
	    return R.data(VisitRecordWrapper.build().entityVO(visitRecord));
    }
     /**
     * 保存&更新
     */
    @Log(title = "来访/外出记录表", businessType = BusinessType.ADDANDEDIT)
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:save')")
    public R<VisitRecordVO> submit(@Valid @RequestBody VisitRecordVO visitRecord){
        if(visitRecord.getResidentId()!=null) {
            ResidentEntity resident=residentService.getById(visitRecord.getResidentId());
            if(resident!=null) {
                visitRecord.setResidentName(resident.getName());
            }
        }
    	 if(visitRecord.getId()==null) {
           return add(visitRecord);
         }else {
           return edit(visitRecord);
        }
    }

    @Log(title = "来访/外出记录表", businessType = BusinessType.INSERT)
    @Operation(summary = "新增")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:save')")
    public R<VisitRecordVO> add(@Valid @RequestBody VisitRecordVO visitRecord){
   		if(visitRecord.getId()==null) {
            visitRecord.setCreateAt(new Date());
            visitRecord.setIsDelete(0);
            visitRecord.setUpdateAt(new Date());
        }
    	if(visitRecordService.save(visitRecord)) {
    		 return R.ok(visitRecord);
    	 }
    	return R.error("数据错误");
    }

    @Log(title = "来访/外出记录表", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新")
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:update')")
    public R<VisitRecordVO> edit(@Valid @RequestBody VisitRecordVO visitRecord){
   		visitRecord.setUpdateAt(new Date());
    	if(visitRecordService.updateById(visitRecord)) {
    		 return R.ok(visitRecord);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermi('ginkgo:visitrecord:delete')")
    @Log(title = "来访/外出记录表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(visitRecordService.removeByIds(Arrays.asList(ids)));
    }

}
