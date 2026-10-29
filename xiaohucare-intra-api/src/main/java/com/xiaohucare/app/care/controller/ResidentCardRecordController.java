package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.ResidentCardRecordEntity;
import com.xiaohucare.app.care.service.ResidentCardRecordService;
import com.xiaohucare.app.care.vo.ResidentCardRecordVO;
import com.xiaohucare.app.care.wrapper.ResidentCardRecordWrapper;
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
 * 会员卡交易流水表
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@RestController
@RequestMapping("intra/care/residentcardrecord")
public class ResidentCardRecordController extends BaseController{
    @Autowired
    private ResidentCardRecordService petOwnerCardRecordService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "cardId", description = "会员卡 ID"),
		        @Parameter(name = "cardNo", description = "卡号"),
		        @Parameter(name = "residentName", description = "客户姓名"),
		        @Parameter(name = "tradeType", description = "交易类型"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:residentcardrecord:list') or @ss.hasPermi('resident:resident:list')")
    public R<VPage<ResidentCardRecordVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ResidentCardRecordEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("cardNo", "residentName", "tradeType"), ResidentCardRecordEntity.class);
        queryWrapper.orderByDesc("id");
        queryWrapper.eq(StringUtils.isNotNull(params.get("cardId")), "card_id", params.get("cardId"));
        queryWrapper.like(StringUtils.isNotNull(params.get("cardNo")), "card_no", params.get("cardNo"));
        queryWrapper.like(StringUtils.isNotNull(params.get("residentName")), "resident_name", params.get("residentName"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("tradeType")), "trade_type", params.get("tradeType"));
        // 门店隔离：登录账号有门店时只看本店流水
        // 日期范围查询
        if (params.containsKey("dateRange") && params.get("dateRange") != null) {
            Object dr = params.get("dateRange");
            if (dr instanceof List && ((List<?>) dr).size() == 2) {
                queryWrapper.between("trade_time", ((List<?>) dr).get(0).toString() + " 00:00:00",
                        ((List<?>) dr).get(1).toString() + " 23:59:59");
            }
        }
        if (params.containsKey("startDate") && params.get("startDate") != null) {
            queryWrapper.ge("trade_time", params.get("startDate").toString() + " 00:00:00");
        }
        if (params.containsKey("endDate") && params.get("endDate") != null) {
            queryWrapper.le("trade_time", params.get("endDate").toString() + " 23:59:59");
        }
        queryWrapper.eq("is_delete", 0);
        IPage<ResidentCardRecordEntity> pages = petOwnerCardRecordService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ResidentCardRecordWrapper.build().toVPageVO(pages));
    }

    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:residentcardrecord:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) {
        QueryWrapper<ResidentCardRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        // 今日统计
        queryWrapper.apply("DATE(trade_time)=CURDATE()");
        Map<String, Object> stat = petOwnerCardRecordService.getMap(
            new QueryWrapper<ResidentCardRecordEntity>().select(
                "COUNT(1) AS todayCount",
                "IFNULL(SUM(CASE WHEN trade_type='consume' THEN amount ELSE 0 END),0) AS todayAmount"
            ).apply("DATE(trade_time)=CURDATE()").eq("is_delete", 0)
        );
        return R.data(stat);
    }

    @Operation(summary = "导出")
	@Log(title = "流水导出", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:residentcardrecord:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ResidentCardRecordEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("cardNo"), ResidentCardRecordEntity.class);
		queryWrapper.orderByDesc("id");
		 List<ResidentCardRecordEntity> list = petOwnerCardRecordService.list(queryWrapper);
		ExcelUtil<ResidentCardRecordEntity> util = new ExcelUtil<>(ResidentCardRecordEntity.class);
		util.exportExcel(response, list, "流水数据");
	}

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:residentcardrecord:detail')")
    public R<ResidentCardRecordVO> detail(@PathVariable("id") Long id){
		ResidentCardRecordEntity petOwnerCardRecord = petOwnerCardRecordService.getById(id);
	    return R.data(ResidentCardRecordWrapper.build().entityVO(petOwnerCardRecord));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:residentcardrecord:submit')")
    public R<ResidentCardRecordVO> submit(@Valid @RequestBody ResidentCardRecordVO petOwnerCardRecord){
   		if(petOwnerCardRecord.getId()==null) {
            petOwnerCardRecord.setCreateAt(new Date());
            petOwnerCardRecord.setIsDelete(0);
        } 
        petOwnerCardRecord.setUpdateAt(new Date());
    	if(petOwnerCardRecordService.saveOrUpdate(petOwnerCardRecord)) {
    		 return R.ok(petOwnerCardRecord);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:residentcardrecord:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(petOwnerCardRecordService.removeByIds(Arrays.asList(ids)));
    }

     /**
     * 获取消费统计
     */
    @GetMapping("/consumeStat")
    @PreAuthorize("@ss.hasPermi('care:petcardrecord:list')")
    public R<Map<String, Object>> consumeStat(@RequestParam Map<String, Object> params) throws ParseException {
        Map<String, Object> result = new LinkedHashMap<>();
        
        // 今日统计
        Date today = new Date();
        Date startOfDay = DateUtils.getStartOfDate(today);
        Date endOfDay = DateUtils.getEndOfDate(today);
        
        Map<String, Object> todayStat = petOwnerCardRecordService.getMap(
            new QueryWrapper<ResidentCardRecordEntity>()
                .select("COUNT(1) AS todayCount", "IFNULL(SUM(amount),0) AS todayAmount")
                .eq("trade_type", "consume")
                .between("create_at", startOfDay, endOfDay)
        );
        
        result.put("todayCount", todayStat != null ? todayStat.get("todayCount") : 0);
        result.put("todayAmount", todayStat != null && todayStat.get("todayAmount") != null ? 
            new java.math.BigDecimal(todayStat.get("todayAmount").toString()) : java.math.BigDecimal.ZERO);
        
        // 本月统计
        Date startOfMonth = DateUtils.getMonthFirstDayOfDate(new Date());
        Date endOfMonth = DateUtils.getMonthLastDayOfDate(new Date());
        
        Map<String, Object> monthStat = petOwnerCardRecordService.getMap(
            new QueryWrapper<ResidentCardRecordEntity>()
                .select("IFNULL(SUM(amount),0) AS monthAmount")
                .eq("trade_type", "consume")
                .between("create_at", startOfMonth, endOfMonth)
        );
        
        result.put("monthAmount", monthStat != null && monthStat.get("monthAmount") != null ? 
            new java.math.BigDecimal(monthStat.get("monthAmount").toString()) : java.math.BigDecimal.ZERO);
        
        // 消费会员数（去重统计）
        Map<String, Object> memberStat = petOwnerCardRecordService.getMap(
            new QueryWrapper<ResidentCardRecordEntity>()
                .select("COUNT(DISTINCT resident_id) AS memberCount")
                .eq("trade_type", "consume")
                .between("create_at", startOfMonth, endOfMonth)
        );
        
        result.put("memberCount", memberStat != null ? memberStat.get("memberCount") : 0);
        
        return R.data(result);
    }

}

