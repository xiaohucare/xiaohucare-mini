package com.xiaohucare.app.care.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.ResidentEntity;
import com.xiaohucare.app.care.service.ResidentService;
import com.xiaohucare.app.care.vo.ResidentVO;
import com.xiaohucare.app.care.wrapper.ResidentWrapper;
import com.xiaohucare.common.enums.BusinessType;
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
 * 长者档案表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/resident/resident")
public class ResidentController extends BaseController{
    /** 年龄表达式（按出生日期算周岁） */
    private static final String AGE_EXPR = "TIMESTAMPDIFF(YEAR, birth_date, CURDATE())";

    @Autowired
    private ResidentService residentService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "长者姓名"),
		        @Parameter(name = "residentNo", description = "长者编号"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('resident:resident:list')")
    public R<VPage<ResidentVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ResidentEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name","residentNo"), ResidentEntity.class);
        
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        queryWrapper.like(StringUtils.isNotNull(params.get("residentNo")),"resident_no", params.get("residentNo"));
        queryWrapper.like(StringUtils.isNotNull(params.get("phone")),"phone", params.get("phone"));
        if(StringUtils.isNotNull(params.get("careLevel"))) { queryWrapper.eq("care_level", params.get("careLevel")); }
        if(StringUtils.isNotNull(params.get("gender"))) { queryWrapper.eq("gender", params.get("gender")); }
        if(StringUtils.isNotNull(params.get("status"))) { queryWrapper.eq("status", params.get("status")); }
        if(StringUtils.isNotNull(params.get("roomId"))) { queryWrapper.eq("room_id", params.get("roomId")); }
        if(StringUtils.isNotNull(params.get("bedId"))) { queryWrapper.eq("bed_id", params.get("bedId")); }
        IPage<ResidentEntity> pages = residentService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ResidentWrapper.build().toVPageVO(pages));
    }
    /**
     * 统计分析：概况计数 + 各维度分布 + 近 N 个月趋势（按当前机构全量口径，不受列表筛选影响）
     */
    @Operation(summary = "统计分析(概况+分布+趋势)")
    @Parameters({
                @Parameter(name = "trendMonths", description = "趋势月数，1~36，默认12"),
        })
    @GetMapping("/overview")
    @PreAuthorize("@ss.hasPermi('resident:resident:list')")
    public R<Map<String, Object>> overview(@RequestParam(required = false, defaultValue = "12") int trendMonths){
        if (trendMonths < 1) trendMonths = 1;
        if (trendMonths > 36) trendMonths = 36;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", buildSummary());
        result.put("genderDist", groupCol("gender"));
        result.put("ageDist", groupAge());
        result.put("careLevelDist", groupCol("care_level"));
        result.put("selfCareDist", groupCol("self_care_ability"));
        result.put("cognitiveDist", groupCol("cognitive_status"));
        result.put("fallRiskDist", groupCol("fall_risk"));
        result.put("maritalDist", groupCol("marital_status"));
        result.put("educationDist", groupCol("education"));
        result.put("medicalInsuranceDist", groupCol("medical_insurance_type"));
        result.put("checkInStatusDist", groupCol("check_in_status"));
        result.put("monthTrend", buildMonthTrend(trendMonths));
        return R.data(result);
    }
    /**
     * 全量列表（下拉/选择器用）
     */
    @Operation(summary = "全量列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('resident:resident:list')")
    public R<List<ResidentVO>> list(@RequestParam(required = false) String name){
        QueryWrapper<ResidentEntity> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.eq("status", 1);
        queryWrapper.like(StringUtils.isNotNull(name),"name", name);
        queryWrapper.orderByDesc("id");
        List<ResidentEntity> list = residentService.list(queryWrapper);
        return R.data(ResidentWrapper.build().listVO(list));
    }
    @Operation(summary = "导出")
	@Log(title = "长者档案", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('resident:resident:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ResidentEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"), ResidentEntity.class);
		
		queryWrapper.orderByDesc("id");
		List<ResidentEntity> list = residentService.list(queryWrapper);
		ExcelUtil<ResidentEntity> util = new ExcelUtil<>(ResidentEntity.class);
		util.exportExcel(response, list, "长者档案数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('resident:resident:detail')")
    public R<ResidentVO> detail(@PathVariable("id") Long id){
		ResidentEntity resident = residentService.getById(id);
	    return R.data(ResidentWrapper.build().entityVO(resident));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('resident:resident:submit')")
    public R<ResidentVO> submit(@Valid @RequestBody ResidentVO resident){
   		if(resident.getId()==null) {
            resident.setCreateAt(new Date());
            resident.setIsDelete(0);
        }
        resident.setUpdateAt(new Date());
    	if(residentService.saveOrUpdate(resident)) {
    		 return R.ok(resident);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.DELETE})
    @PreAuthorize("@ss.hasPermi('resident:resident:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(residentService.removeByIds(Arrays.asList(ids)));
    }

    /** 概况计数：档案总量 / 在用停用 / 入住状态 / 本月新增 / 年龄 / 重点关注 */
    private Map<String, Object> buildSummary() {
        Map<String, Object> s = new LinkedHashMap<>();
        s.put("total", residentService.count(qwResident()));
        s.put("active", residentService.count(qwResident().eq("status", 1)));
        s.put("stopped", residentService.count(qwResident().eq("status", 0)));
        s.put("checkIn", residentService.count(qwResident().eq("check_in_status", 1)));
        s.put("checkedOut", residentService.count(qwResident().eq("check_in_status", 2)));
        // 未入住含历史未填值，避免三档相加小于总数
        QueryWrapper<ResidentEntity> waitQw = qwResident();
        waitQw.and(w -> w.eq("check_in_status", 0).or().isNull("check_in_status"));
        s.put("waitIn", residentService.count(waitQw));
        s.put("monthNew", residentService.count(qwResident().ge("create_at", monthStart())));
        s.put("aged80", residentService.count(qwResident().isNotNull("birth_date")
                .apply(AGE_EXPR + " >= 80")));
        s.put("highCare", residentService.count(qwResident().eq("care_level", 3)));
        s.put("highFallRisk", residentService.count(qwResident().eq("fall_risk", 3)));
        s.put("highCognitive", residentService.count(qwResident().eq("cognitive_status", 3)));
        s.put("noBirthDate", residentService.count(qwResident().isNull("birth_date")));
        s.put("avgAge", avgAge());
        return s;
    }

    /** 平均年龄（岁，保留 1 位小数，无出生日期数据计 0） */
    private double avgAge() {
        QueryWrapper<ResidentEntity> qw = qwResident().isNotNull("birth_date");
        qw.select("round(avg(" + AGE_EXPR + "), 1) as v");
        List<Map<String, Object>> rows = residentService.listMaps(qw);
        if (!rows.isEmpty() && rows.get(0).get("v") instanceof Number) {
            return ((Number) rows.get(0).get("v")).doubleValue();
        }
        return 0d;
    }

    /** 单列分布：按 code 列 groupBy，返回 [{k,c}]（忽略未填写的 null 分组） */
    private List<Map<String, Object>> groupCol(String col) {
        QueryWrapper<ResidentEntity> qw = qwResident();
        qw.select(col + " as k", "count(*) as c").isNotNull(col).groupBy(col).orderByAsc(col);
        return normalizeKV(residentService.listMaps(qw));
    }

    /** 年龄段分布：0未知 1(<70) 2(70-79) 3(80-89) 4(>=90) */
    private List<Map<String, Object>> groupAge() {
        String caseExpr = "case when birth_date is null then 0"
                + " when " + AGE_EXPR + " < 70 then 1"
                + " when " + AGE_EXPR + " < 80 then 2"
                + " when " + AGE_EXPR + " < 90 then 3 else 4 end";
        QueryWrapper<ResidentEntity> qw = qwResident();
        qw.select(caseExpr + " as k", "count(*) as c").groupBy("k").orderByAsc("k");
        return normalizeKV(residentService.listMaps(qw));
    }

    /** 近 N 个自然月趋势：每月新增建档数 + 每月办理入住数（缺失月份补 0） */
    private List<Map<String, Object>> buildMonthTrend(int months) {
        Date begin = monthBegin(months - 1);
        QueryWrapper<ResidentEntity> createdQw = qwResident().ge("create_at", begin);
        createdQw.select("DATE_FORMAT(create_at, '%Y-%m') as d", "count(*) as c").groupBy("d");
        QueryWrapper<ResidentEntity> admittedQw = qwResident().isNotNull("admission_date").ge("admission_date", begin);
        admittedQw.select("DATE_FORMAT(admission_date, '%Y-%m') as d", "count(*) as c").groupBy("d");
        Map<String, Long> created = toMonthMap(residentService.listMaps(createdQw));
        Map<String, Long> admitted = toMonthMap(residentService.listMaps(admittedQw));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<Map<String, Object>> trend = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        for (int i = 0; i < months; i++) {
            String key = sdf.format(cal.getTime());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", key);
            item.put("created", created.getOrDefault(key, 0L));
            item.put("admitted", admitted.getOrDefault(key, 0L));
            trend.add(item);
            cal.add(Calendar.MONTH, 1);
        }
        return trend;
    }

    /** 将 [{d,c}] 月度分组结果转为 月份->数量 */
    private Map<String, Long> toMonthMap(List<Map<String, Object>> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Map<String, Object> m : rows) {
            Object d = m.get("d");
            Object c = m.get("c");
            if (d == null) continue;
            map.put(String.valueOf(d), c instanceof Number ? ((Number) c).longValue() : 0L);
        }
        return map;
    }

    /** 将 [{k,c}] 分组结果洗掉 null 分组并规整数字类型 */
    private List<Map<String, Object>> normalizeKV(List<Map<String, Object>> raw) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> m : raw) {
            Object k = m.get("k");
            Object c = m.get("c");
            if (k == null) continue;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("k", k instanceof Number ? ((Number) k).intValue() : String.valueOf(k));
            item.put("c", c instanceof Number ? ((Number) c).longValue() : 0L);
            list.add(item);
        }
        return list;
    }

    /** 租户隔离：仅统计当前机构的长者档案（is_delete 由 MyBatis-Plus 逻辑删除自动附加） */
    private QueryWrapper<ResidentEntity> qwResident() {
        return new QueryWrapper<ResidentEntity>();
    }

    private Date monthStart() {
        return monthBegin(0);
    }

    /** 往前 minusMonths 个月的 1 号 00:00:00 */
    private Date monthBegin(int minusMonths) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MONTH, -minusMonths);
        return c.getTime();
    }

}
