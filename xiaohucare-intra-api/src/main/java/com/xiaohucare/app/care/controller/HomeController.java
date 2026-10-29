package com.xiaohucare.app.care.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.ResidentContractEntity;
import com.xiaohucare.app.care.service.ResidentContractService;
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.service.AppointmentService;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.service.RoomService;
import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.entity.EmployeeScheduleEntity;
import com.xiaohucare.app.care.service.EmployeeScheduleService;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.system.domain.SysNotice;
import com.xiaohucare.framework.system.service.ISysNoticeService;
import com.xiaohucare.framework.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("intra/care/home")
public class HomeController extends BaseController{

    /** 合同状态：0待签署 */
    private static final int CONTRACT_STATUS_PENDING = 0;
    /** 合同状态：1已签署 */
    private static final int CONTRACT_STATUS_SIGNED = 1;
    /** 合同状态：2已完成 */
    private static final int CONTRACT_STATUS_COMPLETED = 2;
    /** 合同状态：3已作废 */
    private static final int CONTRACT_STATUS_VOIDED = 3;
    /** 到期提醒提前天数 */
    private static final int EXPIRING_SOON_DAYS = 30;

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private CheckinService checkinService;
    
    @Autowired
    private RoomService spaceService;

    @Autowired
    private ISysNoticeService noticeService;
    
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeScheduleService employeeScheduleService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ResidentContractService residentContractService;
    
    /**
     * 获取康养概况统计数据
     */
    @Operation(summary = "获取康养概况")
    @GetMapping("/boardingStats")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<Map<String, Object>> getBoardingStats(){
        try {
            Map<String, Object> stats = new HashMap<>();
            // 1. 今日预约数（status=0待确认或status=1已确认）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            
            QueryWrapper<AppointmentEntity> todayAppointmentQuery = new QueryWrapper<>();
            todayAppointmentQuery.eq("appointment_date", today);
            todayAppointmentQuery.in("status", 0, 1);
            todayAppointmentQuery.eq("is_delete", 0);
            Long todayAppointments = appointmentService.count(todayAppointmentQuery);
            stats.put("todayAppointments", todayAppointments);
            
            // 2. 当前在住长者数（checkin表checkout_status=0表示未退住）
            QueryWrapper<CheckinEntity> currentBoardingQuery = new QueryWrapper<>();
            currentBoardingQuery.eq("checkout_status", 0);
            currentBoardingQuery.eq("is_delete", 0);
            Long currentBoarding = checkinService.count(currentBoardingQuery);
            stats.put("currentBoarding", currentBoarding);
            
            // 3. 今日退住数（checkin表checkout_status=2且actual_checkout_date是今天）
            QueryWrapper<CheckinEntity> todayCheckoutQuery = new QueryWrapper<>();
            todayCheckoutQuery.eq("checkout_status", 2);
            todayCheckoutQuery.apply("DATE(actual_checkout_date) = CURDATE()");
            todayCheckoutQuery.eq("is_delete", 0);
            Long todayCheckouts = checkinService.count(todayCheckoutQuery);
            stats.put("todayCheckouts", todayCheckouts);
            
            // 4. 异常关注数（通过健康风险指标判断）
            QueryWrapper<CheckinEntity> abnormalQuery = new QueryWrapper<>();
            abnormalQuery.eq("checkout_status", 0); // 只统计在住长者
            abnormalQuery.eq("is_delete", 0);
            // 统计有以下情况的长者：高跌倒风险、III~IV度压疮、疑似/确诊失智、完全不能自理
            abnormalQuery.and(wrapper -> wrapper
                .eq("fall_risk", "HIGH")
                .or().eq("pressure_sore", "III_IV")
                .or().in("cognition", "SUSPECTED", "CONFIRMED")
                .or().eq("self_care_ability", "NONE")
            );
            Long abnormalCount = checkinService.count(abnormalQuery);
            stats.put("abnormalCount", abnormalCount);
            
            return R.data(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取统计数据失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取今日预约列表
     */
    @Operation(summary = "获取今日预约")
    @GetMapping("/todayAppointments")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> getTodayAppointments(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            
            QueryWrapper<AppointmentEntity> query = new QueryWrapper<>();
            query.eq("appointment_date", today);
            query.in("status", 0, 1);
            query.eq("is_delete", 0);
            query.orderByAsc("id"); // 使用id排序（因为没有appointment_time字段）
            query.last("LIMIT 10"); // 只取最近10条
            
            List<AppointmentEntity> appointments = appointmentService.list(query);
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (AppointmentEntity apt : appointments) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", apt.getId());
                // 预约编号
                item.put("appointmentNo", apt.getAppointmentNo());
                // 意向长者（新客可空，显示性别偏好）
                String residentDesc = apt.getResidentName() != null && !apt.getResidentName().isEmpty()
                        ? apt.getResidentName()
                        : (apt.getGenderRequired() != null && apt.getGenderRequired() == 1 ? "男长者" : apt.getGenderRequired() != null && apt.getGenderRequired() == 2 ? "女长者" : "待定");
                item.put("residentName", residentDesc);
                item.put("bedCount", apt.getBedCount() != null ? apt.getBedCount() : 1);
                item.put("appointerName", apt.getAppointerName());
                item.put("appointerPhone", apt.getAppointerPhone());
                item.put("status", apt.getStatus() == 1 ? "CONFIRMED" : "PENDING");
                item.put("appointmentTime", apt.getAppointmentDate());
                result.add(item);
            }
            
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取预约列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取当前在住长者列表
     */
    @Operation(summary = "获取当前在住长者")
    @GetMapping("/currentResidents")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> getCurrentResidents(){
        try {
            QueryWrapper<CheckinEntity> query = new QueryWrapper<>();
            query.eq("checkout_status", 0); // 未离店
            query.eq("is_delete", 0);
            query.orderByDesc("id");
            query.last("LIMIT 10"); // 只取最近10条
            
            List<CheckinEntity> residents = checkinService.list(query);
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (CheckinEntity r : residents) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", r.getId());
                item.put("name", r.getResidentName());
                item.put("roomName", r.getRoomName() != null ? r.getRoomName() : "未分配");
                item.put("avatar", ""); // CheckinEntity没有avatar字段
                // 一人一单：家属信息与入住日期均在本行
                item.put("contactName", r.getContactName());
                if (r.getCheckinDate() != null) {
                    long days = (System.currentTimeMillis() - r.getCheckinDate().getTime()) / (1000 * 60 * 60 * 24);
                    item.put("days", days);
                } else {
                    item.put("days", 0);
                }
                
                // 根据健康评估指标判断状态
                String residentStatus = getResidentStatusFromHealth(r);
                item.put("status", residentStatus);
                
                result.add(item);
            }
            
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取在住长者列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 首页看板：获取在住长者精简列表（含状态判断，最多10条）
     */
    @Operation(summary = "首页在住长者列表")
    @GetMapping("/boardingResidents")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> getBoardingResidents(){
        try {
            QueryWrapper<CheckinEntity> query = new QueryWrapper<>();
            query.eq("checkout_status", 0);
            query.eq("is_delete", 0);
            query.orderByDesc("id");
            query.last("LIMIT 10");

            List<CheckinEntity> residents = checkinService.list(query);
            List<Map<String, Object>> result = new ArrayList<>();

            for (CheckinEntity r : residents) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", r.getId());
                item.put("name", r.getResidentName() != null ? r.getResidentName() : "");
                item.put("roomName", r.getRoomName() != null ? r.getRoomName() : "未分配");
                // 计算已住天数
                long days = 0;
                if (r.getCheckinDate() != null) {
                    days = (System.currentTimeMillis() - r.getCheckinDate().getTime()) / (1000 * 60 * 60 * 24);
                }
                item.put("days", days);
                item.put("status", getResidentStatusFromHealth(r));
                result.add(item);
            }
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取在住长者列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取床位使用情况（按房型统计）
     */
    @Operation(summary = "获取床位使用情况")
    @GetMapping("/spaceUsage")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> getSpaceUsage(){
        try {
            // 查询所有未删除的房间
            QueryWrapper<RoomEntity> query = new QueryWrapper<>();
            query.eq("is_delete", 0);
            
            List<RoomEntity> allRooms = spaceService.list(query);
            
            // 按房型分组统计床位使用情况
            Map<String, Map<String, Object>> typeStats = new HashMap<>();
            
            for (RoomEntity room : allRooms) {
                Integer roomTypeCode = room.getRoomType();
                if (roomTypeCode == null) {
                    continue;
                }
                // 房型编码转名称：1-单人间 2-双人间 3-多人间 4-夫妻房
                String roomType = switch (roomTypeCode) {
                    case 1 -> "单人间";
                    case 2 -> "双人间";
                    case 3 -> "多人间";
                    case 4 -> "夫妻房";
                    default -> "房型" + roomTypeCode;
                };
                
                // 如果该房型还没有统计，初始化
                if (!typeStats.containsKey(roomType)) {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("type", roomType);
                    stats.put("name", roomType);
                    stats.put("occupied", 0);
                    stats.put("total", 0);
                    stats.put("roomCount", 0); // 房间数量
                    typeStats.put(roomType, stats);
                }
                
                // 累加统计
                Map<String, Object> stats = typeStats.get(roomType);
                int occupied = (int) stats.get("occupied");
                int total = (int) stats.get("total");
                int roomCount = (int) stats.get("roomCount");
                
                // 只统计启用状态的房间（status != 0）
                if (room.getStatus() != null && room.getStatus() != 0) {
                    stats.put("total", total + (room.getBedCount() != null ? room.getBedCount() : 0));
                    stats.put("occupied", occupied + (room.getOccupiedCount() != null ? room.getOccupiedCount() : 0));
                    stats.put("roomCount", roomCount + 1);
                }
            }
            
            // 转换为列表并计算百分比
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> stats : typeStats.values()) {
                int occupied = (int) stats.get("occupied");
                int total = (int) stats.get("total");
                
                // 计算使用率
                int percentage = 0;
                if (total > 0) {
                    percentage = (int) ((occupied * 100.0) / total);
                }
                stats.put("percentage", percentage);
                
                // 根据使用率设置颜色
                if (percentage >= 80) {
                    stats.put("color", "#f56c6c"); // 红色
                } else if (percentage >= 60) {
                    stats.put("color", "#e6a23c"); // 橙色
                } else if (percentage >= 40) {
                    stats.put("color", "#409eff"); // 蓝色
                } else {
                    stats.put("color", "#67c23a"); // 绿色
                }
                
                result.add(stats);
            }
            
            // 按房型排序（单人间->双人间->多人间->夫妻房->其他）
            result.sort((a, b) -> {
                String typeA = (String) a.get("type");
                String typeB = (String) b.get("type");
                return getTypeOrder(typeA) - getTypeOrder(typeB);
            });
            
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取床位使用情况失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取房型排序权重
     */
    private int getTypeOrder(String roomType) {
        if (roomType == null) return 999;
        switch (roomType) {
            case "单人间": return 1;
            case "双人间": return 2;
            case "多人间": return 3;
            case "夫妻房": return 4;
            case "照护专区": return 5;
            default: return 999;
        }
    }
    
    /**
     * 根据健康评估指标判断长者状态
     */
    private String getResidentStatusFromHealth(CheckinEntity resident) {
        if (resident == null) return "NORMAL";
        
        boolean hasSevereIssue = false;
        boolean hasMinorIssue = false;
        
        // 高跌倒风险
        if ("HIGH".equals(resident.getFallRisk())) {
            hasSevereIssue = true;
        }
        
        // III~IV度压疮
        if ("III_IV".equals(resident.getPressureSore())) {
            hasSevereIssue = true;
        }
        
        // 确诊失智
        if ("CONFIRMED".equals(resident.getCognition())) {
            hasSevereIssue = true;
        }
        
        // 疑似失智
        if ("SUSPECTED".equals(resident.getCognition())) {
            hasMinorIssue = true;
        }
        
        // 完全不能自理
        if ("NONE".equals(resident.getSelfCareAbility())) {
            hasMinorIssue = true;
        }
        
        // 意识状态不佳
        if (resident.getConsciousness() != null && !"CLEAR".equals(resident.getConsciousness())) {
            hasMinorIssue = true;
        }
        
        // 精神状态差
        if ("POOR".equals(resident.getMentalState())) {
            hasMinorIssue = true;
        }
        
        // 根据问题严重程度返回状态
        if (hasSevereIssue) {
            return "ABNORMAL";  // 异常
        } else if (hasMinorIssue) {
            return "ATTENTION"; // 需关注
        } else {
            return "NORMAL";    // 正常
        }
    }

    /**
     * 查询某日值班人员列表
     * slotStatus=3、scheduleType=1 标识康养值班记录，appointmentId 复用来存放员工ID
     * 与 /intra/care/hotel/boardingstaff/dutyList 用同一套查询条件，
     * 但首页对所有登录角色开放，故这里不挂 @PreAuthorize
     */
    @Operation(summary = "查询某日值班列表")
    @GetMapping("/dutyList")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> getDutyList(@RequestParam String date) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            // 用日期区间比较，避免 schedule_date 带时间部分时等值查询匹配不上
            Date startDate = parseDate(date);
            Date endDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
            QueryWrapper<EmployeeScheduleEntity> query = new QueryWrapper<>();
            query.eq("schedule_type", 1);
            query.eq("slot_status", 3);
            query.ge("schedule_date", startDate);
            query.lt("schedule_date", endDate);
            query.eq("is_delete", 0);
            for (EmployeeScheduleEntity record : employeeScheduleService.list(query)) {
                // appointmentId 字段在本表里被复用来存放员工ID
                Long staffId = record.getAppointmentId();
                if (staffId == null || staffId <= 0) {
                    continue;
                }
                EmployeeEntity staff = employeeService.getById(staffId);
                if (staff == null) {
                    continue;
                }
                Map<String, Object> item = new HashMap<>();
                item.put("id", record.getId());
                item.put("date", date);
                item.put("staffId", staffId);
                item.put("staffName", staff.getName());
                item.put("staffPhone", staff.getPhone());
                item.put("staffAvatar", staff.getAvatar());
                item.put("enabled", staff.getEnabled());
                item.put("remark", staff.getRemark());
                result.add(item);
            }
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询值班列表失败：" + e.getMessage());
        }
    }

    /**
     * 日期参数解析：格式异常时回落到今天零点，首页不应因为一个坏参数报错
     */
    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                return sdf.parse(dateStr);
            }
        } catch (Exception ignored) {
        }
        try {
            return sdf.parse(sdf.format(new Date()));
        } catch (Exception e) {
            return new Date();
        }
    }
   
    /**
     * 首页获取最新通知公告列表（最多5条）
     */
    @Operation(summary = "获取最新通知公告")
    @GetMapping("/listNotice")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<List<Map<String, Object>>> listNotice() {
        try {
            // 使用SysNotice实体查询
            SysNotice query = new SysNotice();
            query.setStatus("0"); // 0=正常 1=关闭
            
            List<SysNotice> notices = noticeService.selectNoticeList(query);
            List<Map<String, Object>> result = new ArrayList<>();
            
            // 只显示最新5条
            int limit = Math.min(notices.size(), 5);
            for (int i = 0; i < limit; i++) {
                SysNotice notice = notices.get(i);
                Map<String, Object> item = new HashMap<>();
                item.put("id", notice.getNoticeId());
                item.put("title", notice.getNoticeTitle());
                // 正文按富文本原样返回，供详情弹窗以 v-html 渲染；列表预览另用去标签后的纯文本
                item.put("content", notice.getNoticeContent() == null ? "" : notice.getNoticeContent());
                String plain = htmlToPlainText(notice.getNoticeContent());
                item.put("type", notice.getNoticeType()); // 1=通知 2=公告
                item.put("tag", "1".equals(notice.getNoticeType()) ? "通知" : "公告");
                item.put("tagType", "1".equals(notice.getNoticeType()) ? "primary" : "warning");
                
                // 格式化日期 MM-DD
                if (notice.getCreateTime() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                    item.put("date", sdf.format(notice.getCreateTime()));
                }
                
                // 截取描述（最多50字，单行预览，去除富文本标签）
                String desc = plain.replaceAll("\\s+", " ").trim();
                item.put("desc", desc.length() > 50 ? desc.substring(0, 50) + "..." : desc);
                
                result.add(item);
            }
            
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取通知列表失败：" + e.getMessage());
        }
    }

    /**
     * 将富文本HTML转换为纯文本：块级标签转换行，去除所有标签并反转义常见实体，
     * 供首页公告列表/详情按纯文本渲染，避免原始标记（如 &lt;p&gt;内容&lt;/p&gt;）直接展示
     */
    private String htmlToPlainText(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        String text = html
                .replaceAll("(?i)<br\\s*/?>", "\n")
                .replaceAll("(?i)</p>", "\n")
                .replaceAll("(?i)</div>", "\n")
                .replaceAll("(?i)</li>", "\n")
                .replaceAll("(?i)<[^>]+>", "");
        // 反转义常见HTML实体
        text = text.replace("&nbsp;", " ").replace("&amp;", "&").replace("&lt;", "<")
                .replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'");
        // 压缩多余空白与空行
        return text.replaceAll("[ \\t]+", " ").replaceAll("\n\\s*\n\\s*\n+", "\n\n").trim();
    }
    
    /**
     * 获取住养态势统计数据
     */
    @Operation(summary = "获取住养态势统计")
    @GetMapping("/livingStats")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<Map<String, Object>> getLivingStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            
            return R.data(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取住养态势统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 首页合同统计：一次分组查询得到各状态合同数，再补算本月新签、近期到期与已逾期
     * 统计口径与 /intra/care/contract/browse 保持一致：按当前机构隔离，并排除模版行
     */
    @Operation(summary = "获取合同统计")
    @GetMapping("/contractStats")
   // @PreAuthorize("@ss.hasPermi('care:settings:home')")
    public R<Map<String, Object>> getContractStats() {
        Map<String, Object> stats = new HashMap<>();
        // 先落 0，未绑定机构或查询异常时首页也不会拿到空字段
        stats.put("pending", 0L);
        stats.put("signed", 0L);
        stats.put("completed", 0L);
        stats.put("voided", 0L);
        stats.put("total", 0L);
        stats.put("monthSigned", 0L);
        stats.put("expiringSoon", 0L);
        stats.put("expired", 0L);
        try {
            // 1. 各状态合同数：按 status 分组一次查完，替代四次 count 往返
            QueryWrapper<ResidentContractEntity> statusQuery = buildContractQuery();
            statusQuery.select("status", "count(1) as contractCount").groupBy("status");
            long total = 0L;
            for (Map<String, Object> row : residentContractService.listMaps(statusQuery)) {
                long count = toLongValue(row.get("contractCount"));
                total += count;
                String statKey = getContractStatKey(toLongValue(row.get("status")));
                if (statKey != null) {
                    stats.put(statKey, count);
                }
            }
            stats.put("total", total);

            // 2. 本月新签：签署日期落在本月，且已签署或已完成（待签署、已作废不计入签约量）
            QueryWrapper<ResidentContractEntity> monthQuery = buildContractQuery();
            monthQuery.between("sign_date", DateUtils.getMonthFirstDayOfDate(new Date()),
                    DateUtils.getMonthLastDayOfDate(new Date()));
            monthQuery.in("status", CONTRACT_STATUS_SIGNED, CONTRACT_STATUS_COMPLETED);
            stats.put("monthSigned", residentContractService.count(monthQuery));

            // 3. 近期到期与已逾期：都只看已签署合同，到期日距今 30 天内为待续约，早于今天为已逾期
            Date today = DateUtils.getStartOfDate(new Date());
            Date expireDeadline = new Date(today.getTime() + EXPIRING_SOON_DAYS * 24L * 60 * 60 * 1000);
            QueryWrapper<ResidentContractEntity> expiringQuery = buildContractQuery();
            expiringQuery.eq("status", CONTRACT_STATUS_SIGNED);
            expiringQuery.ge("expiry_date", today);
            expiringQuery.lt("expiry_date", expireDeadline);
            stats.put("expiringSoon", residentContractService.count(expiringQuery));

            QueryWrapper<ResidentContractEntity> expiredQuery = buildContractQuery();
            expiredQuery.eq("status", CONTRACT_STATUS_SIGNED);
            expiredQuery.lt("expiry_date", today);
            stats.put("expired", residentContractService.count(expiredQuery));

            return R.data(stats);
        } catch (Exception e) {
            log.error("获取合同统计失败", e);
            return R.error("获取合同统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 合同统计的公共过滤条件：机构隔离 + 排除模版行（逻辑删除条件由实体的 @TableLogic 自动拼接）
     */
    private QueryWrapper<ResidentContractEntity> buildContractQuery() {
        QueryWrapper<ResidentContractEntity> query = new QueryWrapper<>();
        query.ne("contract_type", ResidentContractEntity.TYPE_TEMPLATE);
        query.notLikeRight("contract_no", ResidentContractEntity.NO_PREFIX_TEMPLATE);
        return query;
    }

    /**
     * 合同状态转首页统计字段名：0待签署 1已签署 2已完成 3已作废
     */
    private String getContractStatKey(long status) {
        switch ((int) status) {
            case CONTRACT_STATUS_PENDING: return "pending";
            case CONTRACT_STATUS_SIGNED: return "signed";
            case CONTRACT_STATUS_COMPLETED: return "completed";
            case CONTRACT_STATUS_VOIDED: return "voided";
            default: return null;
        }
    }

    /**
     * 聚合列的 JDBC 类型不固定（Integer/Long/BigInteger），统一按 Number 取值避免强转异常
     */
    private long toLongValue(Object value) {
        return value instanceof Number ? ((Number) value).longValue() : 0L;
    }
}
