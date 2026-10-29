package com.xiaohucare.app.care.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.service.CheckinFeeItemService;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.service.RoomService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 康养看板（一人一单：登记主表 e_care_checkin 即长者单，健康评估/床位/费用字段均在本行）
 *
 * @author xiaohucare
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/intra/hotel/boarding")
@Tag(name = "康养看板")
public class BoardingController extends BaseController {

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private CheckinFeeItemService checkinFeeItemService;

    @Autowired
    private RoomService spaceService;

    /**
     * 1. 在住长者列表
     */
    @Operation(summary = "在住长者列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('care:checkin:list')")
    public R<List<Map<String, Object>>> list(
            @RequestParam(required = false) String residentName,
            @RequestParam(required = false) String contactPhone,
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) String status) {

        QueryWrapper<CheckinEntity> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("checkout_status", 0);
        if (StringUtils.isNotBlank(residentName)) {
            queryWrapper.like("resident_name", residentName);
        }
        if (StringUtils.isNotBlank(roomName)) {
            queryWrapper.like("room_name", roomName);
        }
        if (StringUtils.isNotBlank(contactPhone)) {
            queryWrapper.like("contact_phone", contactPhone);
        }
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("mental_state", status);
        }
        queryWrapper.orderByDesc("id");

        List<CheckinEntity> residentList = checkinService.list(queryWrapper);

        List<Map<String, Object>> resultList = new ArrayList<>();
        Date today = new Date();

        // 计算今日时间范围（放在循环外避免重复创建）
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayStart = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date todayEnd = cal.getTime();

        for (CheckinEntity resident : residentList) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<>();
            item.put("id", resident.getId());
            item.put("checkinId", resident.getId());
            item.put("residentId", resident.getResidentId());
            item.put("residentName", resident.getResidentName());
            item.put("gender", resident.getGender());
            item.put("careLevel", resident.getCareLevel());
            item.put("roomName", resident.getRoomName());
            item.put("bedNo", resident.getBedNo());
            item.put("mentalState", resident.getMentalState());
            item.put("status", resident.getMentalState());
            item.put("cognition", resident.getCognition());
            item.put("fallRisk", resident.getFallRisk());
            item.put("selfCareAbility", resident.getSelfCareAbility());
            item.put("dietType", resident.getDietType());
            item.put("checkinPhotos", resident.getCheckinPhotos());
            item.put("estimatedCheckOutDate", resident.getCheckoutDate());
            item.put("estimatedCheckout", resident.getCheckoutDate());
            item.put("remark", resident.getRemarks());

            // 一人一单：家属信息与入住日期就在本行
            item.put("contactName", resident.getContactName());
            item.put("contactPhone", resident.getContactPhone());
            item.put("checkinDate", resident.getCheckinDate());
            if (resident.getCheckinDate() != null) {
                long days = (today.getTime() - resident.getCheckinDate().getTime()) / (24 * 60 * 60 * 1000);
                item.put("days", days);
            } else {
                item.put("days", 0);
            }

            // 统计今日护理记录次数
            QueryWrapper<CheckinFeeItemEntity> careWrapper = new QueryWrapper<>();
            careWrapper.eq("checkin_id", resident.getId());
            careWrapper.eq("is_delete", 0);
            careWrapper.between("create_at", todayStart, todayEnd);
            int todayCare = (int) checkinFeeItemService.count(careWrapper);
            item.put("todayCareRecords", todayCare);

            // 查询房间摄像头信息
            if (resident.getRoomId() != null) {
                QueryWrapper<RoomEntity> spaceWrapper = new QueryWrapper<>();
                spaceWrapper.eq("id", resident.getRoomId());
                spaceWrapper.eq("is_delete", 0);
                RoomEntity space = spaceService.getOne(spaceWrapper);
                if (space != null && StringUtils.isNotBlank(space.getCameraDeviceId())) {
                    item.put("hasCamera", 1);
                    item.put("cameraDeviceId", space.getCameraDeviceId());
                } else {
                    item.put("hasCamera", 0);
                    item.put("cameraDeviceId", null);
                }
            } else {
                item.put("hasCamera", 0);
                item.put("cameraDeviceId", null);
            }

            item.put("allergies", resident.getAllergies());
            item.put("medications", resident.getMedications());
            item.put("chronicDiseases", resident.getChronicDiseases());
            item.put("temperature", resident.getTemperature());
            item.put("pulse", resident.getPulse());
            item.put("bloodOxygen", resident.getBloodOxygen());

            // 计算健康风险等级
            String[] risk = calcHealthRisk(resident);
            item.put("healthRiskLevel", risk[0]);
            item.put("riskReasons", risk[1]);

            resultList.add(item);
        }

        return R.data(resultList);
    }

    /**
     * 2. 长者详情
     */
    @Operation(summary = "长者详情")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:checkin:detail')")
    public R<Map<String, Object>> detail(@PathVariable("id") Long id) {
        CheckinEntity resident = checkinService.getById(id);
        if (resident == null) {
            return R.error("记录不存在");
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("id", resident.getId());
        result.put("checkinId", resident.getId());
        result.put("residentId", resident.getResidentId());
        result.put("residentNo", resident.getResidentNo());
        result.put("residentName", resident.getResidentName());
        result.put("gender", resident.getGender());
        result.put("careLevel", resident.getCareLevel());
        result.put("birthDate", resident.getBirthDate());
        result.put("idCard", resident.getIdCard());
        result.put("phone", resident.getPhone());
        result.put("roomId", resident.getRoomId());
        result.put("roomName", resident.getRoomName());
        result.put("bedId", resident.getBedId());
        result.put("bedNo", resident.getBedNo());

        // 健康评估字段
        result.put("mentalState", resident.getMentalState());
        result.put("status", resident.getMentalState());
        result.put("consciousness", resident.getConsciousness());
        result.put("temperature", resident.getTemperature());
        result.put("pulse", resident.getPulse());
        result.put("bloodPressureHigh", resident.getBloodPressureHigh());
        result.put("bloodPressureLow", resident.getBloodPressureLow());
        result.put("bloodOxygen", resident.getBloodOxygen());
        result.put("vision", resident.getVision());
        result.put("hearing", resident.getHearing());
        result.put("cognition", resident.getCognition());
        result.put("fallRisk", resident.getFallRisk());
        result.put("pressureSore", resident.getPressureSore());
        result.put("selfCareAbility", resident.getSelfCareAbility());
        result.put("chronicDiseases", resident.getChronicDiseases());
        result.put("medications", resident.getMedications());
        result.put("allergies", resident.getAllergies());
        result.put("dietType", resident.getDietType());
        result.put("elimination", resident.getElimination());

        result.put("checkinPhotos", resident.getCheckinPhotos());
        result.put("broughtItems", resident.getBroughtItems());
        result.put("broughtPhotos", resident.getBroughtPhotos());
        result.put("estimatedCheckOutDate", resident.getCheckoutDate());
        result.put("estimatedCheckout", resident.getCheckoutDate());
        result.put("remark", resident.getRemarks());
        result.put("checkinStatus", resident.getCheckinStatus());

        // 物品交接
        result.put("returnedItems", resident.getReturnedItems());
        result.put("itemsStatus", resident.getItemsStatus());
        result.put("itemsConfirmed", resident.getItemsConfirmed());
        result.put("itemsRemark", resident.getItemsRemark());
        result.put("checkoutPhotos", resident.getCheckoutPhotos());

        // 计算健康风险等级
        String[] risk = calcHealthRisk(resident);
        result.put("healthRiskLevel", risk[0]);
        result.put("riskReasons", risk[1]);

        // 登记信息（一人一单：家属/入住日期/单号均在本行）
        result.put("contactName", resident.getContactName());
        result.put("contactPhone", resident.getContactPhone());
        result.put("checkinDate", resident.getCheckinDate());
        result.put("checkinNo", resident.getCheckinNo());
        result.put("chargeNum", resident.getChargeNum());
        result.put("chargeCycle", resident.getChargeCycle());
        result.put("estimatedDays", ChargeCalcKit.plannedDays(resident.getChargeNum(), resident.getChargeCycle()));
        result.put("specialNeeds", resident.getSpecialNeeds());
        result.put("contactPhoneBackup", resident.getContactPhoneBackup());
        Date today = new Date();
        if (resident.getCheckinDate() != null) {
            long days = (today.getTime() - resident.getCheckinDate().getTime()) / (24 * 60 * 60 * 1000);
            result.put("days", days);
        } else {
            result.put("days", 0);
        }

        // 查询房间摄像头信息
        if (resident.getRoomId() != null) {
            QueryWrapper<RoomEntity> spaceWrapper = new QueryWrapper<>();
            spaceWrapper.eq("id", resident.getRoomId());
            spaceWrapper.eq("is_delete", 0);
            RoomEntity space = spaceService.getOne(spaceWrapper);
            if (space != null && StringUtils.isNotBlank(space.getCameraDeviceId())) {
                result.put("hasCamera", 1);
                result.put("cameraDeviceId", space.getCameraDeviceId());
            } else {
                result.put("hasCamera", 0);
                result.put("cameraDeviceId", null);
            }
        } else {
            result.put("hasCamera", 0);
            result.put("cameraDeviceId", null);
        }

        // 查询费用项列表
        QueryWrapper<CheckinFeeItemEntity> feeWrapper = new QueryWrapper<>();
        feeWrapper.eq("checkin_id", id);
        feeWrapper.eq("is_delete", 0);
        feeWrapper.orderByDesc("create_at");
        List<CheckinFeeItemEntity> feeItems = checkinFeeItemService.list(feeWrapper);
        result.put("feeItems", feeItems);

        return R.data(result);
    }

    /**
     * 3. 添加日常记录
     */
    @Operation(summary = "添加日常记录")
    @PostMapping("/record/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> addRecord(@PathVariable("checkinId") Long checkinId,
            @RequestBody CheckinFeeItemEntity feeItem) {
        feeItem.setCheckinId(checkinId);
        feeItem.setIsOriginal(0);
        feeItem.setCreateAt(new Date());
        return R.ok(checkinFeeItemService.save(feeItem));
    }

    /**
     * 4. 获取日常记录列表
     */
    @Operation(summary = "获取日常记录列表")
    @GetMapping("/records/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:list')")
    public R<List<CheckinFeeItemEntity>> records(@PathVariable("checkinId") Long checkinId) {
        QueryWrapper<CheckinFeeItemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkin_id", checkinId);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByDesc("create_at");
        List<CheckinFeeItemEntity> list = checkinFeeItemService.list(queryWrapper);
        return R.data(list);
    }

    /**
     * 5. 标记异常
     */
    @Operation(summary = "标记异常")
    @PostMapping("/abnormal/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> markAbnormal(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> body) {
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("记录不存在");
        }
        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : "";
        resident.setMentalState("POOR");
        String existRemark = resident.getRemarks() != null ? resident.getRemarks() : "";
        resident.setRemarks(existRemark + (existRemark.isEmpty() ? "" : "\n") + "[异常] " + reason);
        resident.setUpdateAt(new Date());
        return R.ok(checkinService.updateById(resident));
    }

    /**
     * 6. 解除异常
     */
    @Operation(summary = "解除异常")
    @PostMapping("/resolve/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> resolveAbnormal(@PathVariable("checkinId") Long checkinId) {
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("记录不存在");
        }
        resident.setMentalState("GOOD");
        resident.setUpdateAt(new Date());
        return R.ok(checkinService.updateById(resident));
    }

    /**
     * 7. 转医养专区
     */
    @Operation(summary = "转医养专区")
    @PostMapping("/medical/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> transferMedical(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> body) {
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("记录不存在");
        }
        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : "";
        resident.setMentalState("POOR");
        String existRemark = resident.getRemarks() != null ? resident.getRemarks() : "";
        resident.setRemarks(existRemark + (existRemark.isEmpty() ? "" : "\n") + "[医养监护] " + reason);
        resident.setUpdateAt(new Date());
        return R.ok(checkinService.updateById(resident));
    }

    /**
     * 8. 统计
     */
    @Operation(summary = "康养看板统计")
    @GetMapping("/statistics")
    @PreAuthorize("@ss.hasPermi('care:checkin:list')")
    public R<Map<String, Object>> statistics() {
        // 查询在住总数（checkoutStatus=0）
        QueryWrapper<CheckinEntity> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("is_delete", 0);
        totalWrapper.eq("checkout_status", 0);
        long total = checkinService.count(totalWrapper);

        // 按 mentalState 分组统计
        QueryWrapper<CheckinEntity> groupWrapper = new QueryWrapper<>();
        groupWrapper.eq("is_delete", 0);
        groupWrapper.eq("checkout_status", 0);
        groupWrapper.select("mental_state", "count(id) as count");
        groupWrapper.groupBy("mental_state");
        List<Map<String, Object>> groupResult = checkinService.listMaps(groupWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("good", 0);
        result.put("normal", 0);
        result.put("poor", 0);

        for (Map<String, Object> item : groupResult) {
            String state = item.get("mental_state") != null ? item.get("mental_state").toString() : "";
            long count = ((Number) item.get("count")).longValue();
            if ("GOOD".equalsIgnoreCase(state)) {
                result.put("good", count);
            } else if ("NORMAL".equalsIgnoreCase(state)) {
                result.put("normal", count);
            } else if ("POOR".equalsIgnoreCase(state)) {
                result.put("poor", count);
            }
        }

        return R.data(result);
    }

    /**
     * 9. 发送消息给家属
     */
    @Operation(summary = "发送消息给家属")
    @PostMapping("/message/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> sendMessage(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> params) {
        // 暂时只做记录，后续可对接消息推送
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("长者信息不存在");
        }
        // TODO: 对接消息推送系统
        return R.ok(true);
    }

    /**
     * 10. 上传媒体文件
     */
    @Operation(summary = "上传媒体文件")
    @PostMapping("/media/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> uploadMedia(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> params) {
        // 暂时只做记录，后续可对接 OSS
        return R.ok(true);
    }

    /**
     * 更新健康监测数据
     */
    @Operation(summary = "更新健康监测数据")
    @PostMapping("/health/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> updateHealth(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> body) {
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("记录不存在");
        }
        if (body.get("temperature") != null) {
            resident.setTemperature(new java.math.BigDecimal(body.get("temperature").toString()));
        }
        if (body.get("pulse") != null) {
            resident.setPulse(Integer.valueOf(body.get("pulse").toString()));
        }
        if (body.get("bloodPressureHigh") != null) {
            resident.setBloodPressureHigh(Integer.valueOf(body.get("bloodPressureHigh").toString()));
        }
        if (body.get("bloodPressureLow") != null) {
            resident.setBloodPressureLow(Integer.valueOf(body.get("bloodPressureLow").toString()));
        }
        if (body.get("bloodOxygen") != null) {
            resident.setBloodOxygen(Integer.valueOf(body.get("bloodOxygen").toString()));
        }
        if (body.get("mentalState") != null) {
            resident.setMentalState(body.get("mentalState").toString());
        }
        if (body.get("consciousness") != null) {
            resident.setConsciousness(body.get("consciousness").toString());
        }
        if (body.get("cognition") != null) {
            resident.setCognition(body.get("cognition").toString());
        }
        if (body.get("fallRisk") != null) {
            resident.setFallRisk(body.get("fallRisk").toString());
        }
        if (body.get("pressureSore") != null) {
            resident.setPressureSore(body.get("pressureSore").toString());
        }
        if (body.get("selfCareAbility") != null) {
            resident.setSelfCareAbility(body.get("selfCareAbility").toString());
        }
        resident.setUpdateAt(new Date());
        return R.ok(checkinService.updateById(resident));
    }

    /**
     * 更新物品交接状态
     */
    @Operation(summary = "更新物品交接状态")
    @PostMapping("/items-check/{checkinId}")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<Boolean> updateItemsCheck(@PathVariable("checkinId") Long checkinId,
            @RequestBody Map<String, Object> body) {
        CheckinEntity resident = checkinService.getById(checkinId);
        if (resident == null) {
            return R.error("记录不存在");
        }
        if (body.get("returnedItems") != null) {
            resident.setReturnedItems(body.get("returnedItems").toString());
        }
        if (body.get("itemsStatus") != null) {
            resident.setItemsStatus(Integer.valueOf(body.get("itemsStatus").toString()));
        }
        if (body.get("itemsConfirmed") != null) {
            resident.setItemsConfirmed(Integer.valueOf(body.get("itemsConfirmed").toString()));
        }
        if (body.get("itemsRemark") != null) {
            resident.setItemsRemark(body.get("itemsRemark").toString());
        }
        resident.setUpdateAt(new Date());
        return R.ok(checkinService.updateById(resident));
    }

    /**
     * 依据健康评估指标计算长者健康风险等级。
     * 返回 [等级, 原因]，等级取值：SAFE / WATCH / ALERT
     */
    private String[] calcHealthRisk(CheckinEntity resident) {
        String level = "SAFE";
        StringBuilder reasons = new StringBuilder();

        // ALERT 级：高跌倒风险 / III~IV度压疮 / 确诊失智 / 意识不清
        if ("HIGH".equals(resident.getFallRisk())) {
            level = "ALERT";
            reasons.append("高跌倒风险;");
        }
        if ("III_IV".equals(resident.getPressureSore())) {
            level = "ALERT";
            reasons.append("III~IV度压疮;");
        }
        if ("CONFIRMED".equals(resident.getCognition())) {
            level = "ALERT";
            reasons.append("确诊失智;");
        }
        if (resident.getConsciousness() != null && !"CLEAR".equals(resident.getConsciousness())) {
            level = "ALERT";
            reasons.append("意识状态异常;");
        }
        // 体温异常（长者正常范围约 36.0~37.3℃）
        if (resident.getTemperature() != null) {
            double temp = resident.getTemperature().doubleValue();
            if (temp >= 38.0 || temp < 35.0) {
                level = "ALERT";
                reasons.append("体温异常(" + temp + "℃);");
            } else if (temp > 37.3) {
                if (!"ALERT".equals(level)) level = "WATCH";
                reasons.append("低热(" + temp + "℃);");
            }
        }

        // WATCH 级：疑似失智 / I~II度压疮 / 完全不能自理 / 精神差 / 中跌倒风险
        if ("SUSPECTED".equals(resident.getCognition())) {
            if (!"ALERT".equals(level)) level = "WATCH";
            reasons.append("疑似失智;");
        }
        if ("I_II".equals(resident.getPressureSore())) {
            if (!"ALERT".equals(level)) level = "WATCH";
            reasons.append("I~II度压疮;");
        }
        if ("NONE".equals(resident.getSelfCareAbility())) {
            if (!"ALERT".equals(level)) level = "WATCH";
            reasons.append("完全不能自理;");
        }
        if ("MEDIUM".equals(resident.getFallRisk())) {
            if (!"ALERT".equals(level)) level = "WATCH";
            reasons.append("中等跌倒风险;");
        }
        if ("POOR".equals(resident.getMentalState())) {
            if (!"ALERT".equals(level)) level = "WATCH";
            reasons.append("精神状态差;");
        }

        return new String[]{level, reasons.toString()};
    }
}
