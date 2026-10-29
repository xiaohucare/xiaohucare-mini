package com.xiaohucare.app.care.controller;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.service.SettlementService;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("intra/care/bill")
public class BillController extends BaseController{
    
    @Autowired
    private CheckinService checkinService;
    
    @Autowired
    private SettlementService settlementService;
    
    /**
     * 获取收费统计汇总数据
     * 包含：KPI指标、收入构成、趋势数据、最近结算记录
     */
    @Operation(summary = "获取收费统计汇总")
    @GetMapping("/statistics")
    @PreAuthorize("@ss.hasPermi('care:bill:list')")
    public R<Map<String, Object>> getStatistics(@RequestParam(required = false) String dateRange) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 计算本月起止时间
            Date now = new Date();
            Date monthStart = getMonthStart(now);
            Date monthEnd = getMonthEnd(now);
            Date todayStart = DateUtils.getStartOfDate(now);
            Date todayEnd = DateUtils.getEndOfDate(now);
            
            // ===== 1. KPI指标 =====
            Map<String, Object> kpi = new HashMap<>();
            
            // 本月总收入（康养结算）
            BigDecimal monthBoardingIncome = getSettlementAmount(monthStart, monthEnd);
            BigDecimal monthIncome = monthBoardingIncome;
            kpi.put("monthIncome", monthIncome.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            
            // 上月总收入（对比用）
            Date lastMonthStart = getLastMonthStart(now);
            Date lastMonthEnd = getLastMonthEnd(now);
            BigDecimal lastMonthBoardingIncome = getSettlementAmount(lastMonthStart, lastMonthEnd);
            BigDecimal lastMonthIncome = lastMonthBoardingIncome;
            kpi.put("lastMonthIncome", lastMonthIncome.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            
            // 今日收入
            BigDecimal todayBoardingIncome = getSettlementAmount(todayStart, todayEnd);
            BigDecimal todayIncome = todayBoardingIncome;
            kpi.put("todayIncome", todayIncome.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            
            // 今日订单数
            long todayBoardingOrders = getSettlementCount(todayStart, todayEnd);
            kpi.put("todayOrders", todayBoardingOrders );
            
            // 康养收入（本月）
            kpi.put("boardingIncome", monthBoardingIncome.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            long boardingOrders = getSettlementCount(monthStart, monthEnd);
            kpi.put("boardingOrders", boardingOrders);
            
            
            // 在押押金（在住未结算，已缴押金合计）
            QueryWrapper<CheckinEntity> depositQuery = new QueryWrapper<>();
            depositQuery.eq("checkin_status", 2); // 康养中（在住）
            depositQuery.ne("total_deposit_pay_money", 0);
            depositQuery.eq("settled", 0); // 未结算
            depositQuery.select("COUNT(*) AS cnt", "IFNULL(SUM(total_deposit_pay_money),0) AS amt");
            Map<String, Object> depositStat = checkinService.getMap(depositQuery);
            kpi.put("depositCount", depositStat != null && depositStat.get("cnt") != null ? ((Number) depositStat.get("cnt")).longValue() : 0);
            kpi.put("holdingDeposit", depositStat != null && depositStat.get("amt") != null ? new BigDecimal(depositStat.get("amt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00");
            
            // 待结算订单（结算单 status=0 待结算）
            QueryWrapper<SettlementEntity> pendingQuery = new QueryWrapper<>();
            pendingQuery.eq("status", 0);
            pendingQuery.select("COUNT(*) AS cnt", "IFNULL(SUM(total_amount),0) AS amt");
            Map<String, Object> pendingStat = settlementService.getMap(pendingQuery);
            kpi.put("pendingOrders", pendingStat != null && pendingStat.get("cnt") != null ? ((Number) pendingStat.get("cnt")).longValue() : 0);
            kpi.put("pendingAmount", pendingStat != null && pendingStat.get("amt") != null ? new BigDecimal(pendingStat.get("amt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00");
            
            // 在住长者数（康养中）
            QueryWrapper<CheckinEntity> residentQuery = new QueryWrapper<>();
            residentQuery.eq("checkin_status", 2);
            kpi.put("residentCount", checkinService.count(residentQuery));
            
            // 本月新增登记数
            QueryWrapper<CheckinEntity> monthCheckinQuery = new QueryWrapper<>();
            monthCheckinQuery.between("create_at", monthStart, monthEnd);
            kpi.put("monthCheckinCount", checkinService.count(monthCheckinQuery));
            
            // 环比增长率（本月 vs 上月，百分比）
            int monthGrowth = 0;
            if (lastMonthIncome.compareTo(BigDecimal.ZERO) > 0) {
                monthGrowth = monthIncome.subtract(lastMonthIncome)
                    .multiply(new BigDecimal("100"))
                    .divide(lastMonthIncome, 0, BigDecimal.ROUND_HALF_UP).intValue();
            } else if (monthIncome.compareTo(BigDecimal.ZERO) > 0) {
                monthGrowth = 100;
            }
            kpi.put("monthGrowth", monthGrowth);
            
            result.put("kpi", kpi);
            
            // ===== 2. 收入构成（本月已结算，按费用类目拆分：床位/护理/餐费/增值/其他） =====
            List<Map<String, Object>> composition = new ArrayList<>();
            buildComposition(composition, monthStart, monthEnd);
            result.put("composition", composition);
            
            // ===== 2.1 支付方式分布（本月已结算，按 payment_method 汇总实收） =====
            result.put("payMethods", buildPayMethods(monthStart, monthEnd));
            
            // ===== 3. 收入趋势（近30天） =====
            List<Map<String, Object>> trendData = new ArrayList<>();
            for (int i = 29; i >= 0; i--) {
                Date dayStart = DateUtils.getStartOfDate(DateUtils.addDays(now, -i));
                Date dayEnd = DateUtils.getEndOfDate(DateUtils.addDays(now, -i));
                
                BigDecimal dayBoarding = getSettlementAmount(dayStart, dayEnd);
                BigDecimal dayTotal = dayBoarding;
                
                Map<String, Object> dayItem = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                dayItem.put("date", sdf.format(dayStart));
                dayItem.put("shortDate", String.valueOf(DateUtils.addDays(now, -i).getDate()));
                dayItem.put("amount", dayTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                trendData.add(dayItem);
            }
            result.put("trendData", trendData);
            
            // ===== 4. 最近结算记录（待结算+已结算，最新10条） =====
            List<Map<String, Object>> recentSettlements = new ArrayList<>();
            
            QueryWrapper<SettlementEntity> boardingQuery = new QueryWrapper<>();
            boardingQuery.in("status", 0, 1);
            boardingQuery.orderByDesc("create_at");
            boardingQuery.last("LIMIT 10");
            List<SettlementEntity> boardingList = settlementService.list(boardingQuery);
            
            // 批量反查登记单，补全长者/家属姓名
            Map<Long, CheckinEntity> checkinMap = new HashMap<>();
            List<Long> checkinIds = new ArrayList<>();
            for (SettlementEntity s : boardingList) {
                if (s.getCheckinId() != null) checkinIds.add(s.getCheckinId());
            }
            if (!checkinIds.isEmpty()) {
                QueryWrapper<CheckinEntity> cq = new QueryWrapper<>();
                cq.in("id", checkinIds);
                for (CheckinEntity c : checkinService.list(cq)) {
                    checkinMap.put(c.getId(), c);
                }
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (SettlementEntity settlement : boardingList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", settlement.getId());
                item.put("settlementNo", settlement.getSettlementNo());
                item.put("serviceType", "boarding");
                CheckinEntity c = settlement.getCheckinId() != null ? checkinMap.get(settlement.getCheckinId()) : null;
                item.put("residentName", c != null && c.getResidentName() != null ? c.getResidentName() : "—");
                item.put("contactName", c != null && c.getContactName() != null ? c.getContactName() : "—");
                item.put("serviceContent", buildServiceContent(settlement));
                item.put("totalAmount", settlement.getActualPayment() != null ? 
                    settlement.getActualPayment().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00");
                item.put("payMethod", settlement.getPaymentMethod());
                item.put("status", settlement.getStatus());
                if (settlement.getCreateAt() != null) {
                    item.put("settledAt", sdf.format(settlement.getCreateAt()));
                }
                recentSettlements.add(item);
            }
            
            result.put("recentSettlements", recentSettlements);
            
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取统计数据失败：" + e.getMessage());
        }
    }
    
    /**
     * 收入构成：按费用类目（床位/护理/餐费/增值服务/其他）拆分本月已结算金额
     */
    private void buildComposition(List<Map<String, Object>> composition, Date start, Date end) {
        QueryWrapper<SettlementEntity> query = new QueryWrapper<>();
        query.eq("status", 1);
        query.between("create_at", start, end);
        query.select(
            "IFNULL(SUM(bed_fee),0) AS bedFee",
            "IFNULL(SUM(care_fee),0) AS careFee",
            "IFNULL(SUM(meal_fee),0) AS mealFee",
            "IFNULL(SUM(service_fee),0) AS serviceFee",
            "IFNULL(SUM(other_fee),0) AS otherFee");
        Map<String, Object> stat = settlementService.getMap(query);
        if (stat == null) {
            return;
        }
        String[][] defs = {
            {"床位费", "#409EFF", "bedFee"},
            {"护理费", "#67C23A", "careFee"},
            {"餐费", "#E6A23C", "mealFee"},
            {"增值服务", "#9747FF", "serviceFee"},
            {"其他费用", "#F56C6C", "otherFee"}
        };
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal[] vals = new BigDecimal[defs.length];
        for (int i = 0; i < defs.length; i++) {
            Object o = stat.get(defs[i][2]);
            vals[i] = o != null ? new BigDecimal(o.toString()) : BigDecimal.ZERO;
            total = total.add(vals[i]);
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        for (int i = 0; i < defs.length; i++) {
            if (vals[i].compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("name", defs[i][0]);
            item.put("color", defs[i][1]);
            item.put("amount", vals[i].setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            item.put("percent", vals[i].multiply(new BigDecimal("100")).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue());
            composition.add(item);
        }
    }
    
    /**
     * 支付方式分布：本月已结算按 payment_method 汇总实收金额
     */
    private List<Map<String, Object>> buildPayMethods(Date start, Date end) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper<SettlementEntity> query = new QueryWrapper<>();
        query.eq("status", 1);
        query.between("create_at", start, end);
        query.select("IFNULL(payment_method,'other') AS pm", "IFNULL(SUM(actual_payment),0) AS amt");
        query.groupBy("payment_method");
        List<Map<String, Object>> rows = settlementService.listMaps(query);
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> r : rows) {
            BigDecimal amt = r.get("amt") != null ? new BigDecimal(r.get("amt").toString()) : BigDecimal.ZERO;
            total = total.add(amt);
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return list;
        }
        Map<String, String[]> meta = new HashMap<>();
        meta.put("cash", new String[]{"现金", "#E6A23C"});
        meta.put("wechat", new String[]{"微信支付", "#07C160"});
        meta.put("alipay", new String[]{"支付宝", "#1677FF"});
        meta.put("card", new String[]{"银行卡", "#909399"});
        meta.put("member_card", new String[]{"会员卡", "#9747FF"});
        for (Map<String, Object> r : rows) {
            BigDecimal amt = r.get("amt") != null ? new BigDecimal(r.get("amt").toString()) : BigDecimal.ZERO;
            if (amt.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            String pm = r.get("pm") != null ? r.get("pm").toString().toLowerCase() : "other";
            String[] m = meta.getOrDefault(pm, new String[]{"其他", "#409EFF"});
            Map<String, Object> item = new HashMap<>();
            item.put("name", m[0]);
            item.put("color", m[1]);
            item.put("amount", amt.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            item.put("percent", amt.multiply(new BigDecimal("100")).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue());
            list.add(item);
        }
        list.sort((a, b) -> Integer.compare((int) b.get("percent"), (int) a.get("percent")));
        return list;
    }
    
    /**
     * 结算单服务内容：拼接各费用类目
     */
    private String buildServiceContent(SettlementEntity s) {
        StringBuilder sb = new StringBuilder();
        appendFee(sb, "床位", s.getBedFee());
        appendFee(sb, "护理", s.getCareFee());
        appendFee(sb, "餐费", s.getMealFee());
        appendFee(sb, "增值", s.getServiceFee());
        appendFee(sb, "其他", s.getOtherFee());
        return sb.length() > 0 ? sb.toString() : "康养服务";
    }
    
    private void appendFee(StringBuilder sb, String label, BigDecimal amt) {
        if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(" · ");
        }
        sb.append(label).append(" ¥").append(amt.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
    
    /**
     * 获取指定时间段内康养结算金额
     */
    private BigDecimal getSettlementAmount(Date startTime, Date endTime) {
        QueryWrapper<SettlementEntity> query = new QueryWrapper<>();
        query.eq("status", 1);
        query.between("create_at", startTime, endTime);
        query.select("SUM(actual_payment) as totalAmount");
        Map<String, Object> stat = settlementService.getMap(query);
        if (stat != null && stat.get("totalAmount") != null) {
            return new BigDecimal(stat.get("totalAmount").toString());
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 获取指定时间段内康养结算单数
     */
    private long getSettlementCount(Date startTime, Date endTime) {
        QueryWrapper<SettlementEntity> query = new QueryWrapper<>();
        query.eq("status", 1);
        query.between("create_at", startTime, endTime);
        return settlementService.count(query);
    }
    
    /**
     * 辅助方法：获取本月开始时间
     */
    private Date getMonthStart(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 辅助方法：获取本月结束时间
     */
    private Date getMonthEnd(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        cal.set(java.util.Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    /**
     * 辅助方法：获取上月开始时间
     */
    private Date getLastMonthStart(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.add(java.util.Calendar.MONTH, -1);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 辅助方法：获取上月结束时间
     */
    private Date getLastMonthEnd(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.add(java.util.Calendar.DATE, -1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        cal.set(java.util.Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
