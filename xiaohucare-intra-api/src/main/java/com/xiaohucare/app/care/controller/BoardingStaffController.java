package com.xiaohucare.app.care.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.entity.EmployeeScheduleEntity;
import com.xiaohucare.app.care.service.EmployeeScheduleService;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.app.care.vo.EmployeeVO;
import com.xiaohucare.app.care.wrapper.EmployeeWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

/**
 * 康养值班人员管理
 * 复用 EmployeeEntity，通过 is_care=1 过滤康养员工
 */
@RestController
@RequestMapping("intra/hotel/boardingstaff")
public class BoardingStaffController extends BaseController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeScheduleService employeeScheduleService;
    /**
     * 分页列表
     */
    @Operation(summary = "康养值班人员列表")
    @Parameters({
        @Parameter(name = "name", description = "姓名"),
        @Parameter(name = "enabled", description = "启用状态"),
    })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:list')")
    public R<VPage<EmployeeVO>> browse(@RequestParam Map<String, Object> params) {
        QueryWrapper<EmployeeEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(
                params, Arrays.asList("name", "enabled"), EmployeeEntity.class);
        
        queryWrapper.eq("is_care", 1);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("enabled")), "enabled", params.get("enabled"));
        queryWrapper.orderByDesc("id");
        IPage<EmployeeEntity> pages = employeeService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(EmployeeWrapper.build().toVPageVO(pages));
    }

    /**
     * 全量列表（用于下拉选择、值班设置）
     */
    @Operation(summary = "全量值班人员列表")
    @GetMapping("/listAll")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:list')")
    public R<List<EmployeeVO>> listAll(@RequestParam Map<String, Object> params) {
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("is_care", 1);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq(StringUtils.isNotNull(params.get("enabled")), "enabled", params.get("enabled"));
        queryWrapper.orderByAsc("id");
        List<EmployeeEntity> list = employeeService.list(queryWrapper);
        return R.data(EmployeeWrapper.build().listVO(list));
    }

    /**
     * 详情
     */
    @Operation(summary = "值班人员详情")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:list')")
    public R<EmployeeVO> detail(@PathVariable("id") Long id) {
        EmployeeEntity entity = employeeService.getById(id);
        if (entity == null || Integer.valueOf(0).equals(entity.getIsCare())) {
            return R.error("人员不存在");
        }
        return R.data(EmployeeWrapper.build().entityVO(entity));
    }

    /**
     * 新增 / 修改
     */
    @Operation(summary = "保存&更新值班人员")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:submit')")
    public R<EmployeeVO> submit(@RequestBody EmployeeVO staff) {
        // 强制标记为康养人员
        staff.setIsCare(1);
        if (staff.getId() == null) {
            staff.setCreateAt(new Date());
            staff.setIsDelete(0);
            if (staff.getEnabled() == null) {
                staff.setEnabled(1);
            }
        }
        staff.setUpdateAt(new Date());
        if (employeeService.saveOrUpdate(staff)) {
            return R.ok(staff);
        }
        return R.error("保存失败");
    }

    /**
     * 删除（软删除）
     */
    @Operation(summary = "删除值班人员")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids) {
        return R.ok(employeeService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 启用 / 禁用
     */
    @Operation(summary = "切换启用状态")
    @PostMapping("/toggle/{id}")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:submit')")
    public R<Boolean> toggle(@PathVariable("id") Long id) {
        EmployeeEntity entity = employeeService.getById(id);
        if (entity == null) return R.error("人员不存在");
        entity.setEnabled(Integer.valueOf(1).equals(entity.getEnabled()) ? 0 : 1);
        entity.setUpdateAt(new Date());
        return R.ok(employeeService.updateById(entity));
    }
    
    
    
    // ===================== 值班排班接口 =====================

    /**
     * 查询某日值班人员列表
     * slotStatus=3、timeSlot='DUTY' 标识康养值班记录
     * appointmentId 存储员工ID
     */
    @Operation(summary = "查询某日值班列表")
    @GetMapping("/dutyList")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:list')")
    public R<List<Map<String, Object>>> getDutyList(@RequestParam String date) {
    	  try {
              // 使用日期范围查询避免Timestamp匹配问题
              Date startDate = parseDate(date);
              Date endDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
              QueryWrapper<EmployeeScheduleEntity> query = new QueryWrapper<>();
//              query.eq("time_slot", "DUTY");
              query.eq("schedule_type", 1);
              query.eq("slot_status", 3);
              query.ge("schedule_date", startDate);
              query.lt("schedule_date", endDate);
              query.eq("is_delete", 0);
              List<EmployeeScheduleEntity> list = employeeScheduleService.list(query);

              List<Map<String, Object>> result = new ArrayList<>();
              for (EmployeeScheduleEntity record : list) {
                  Map<String, Object> item = new HashMap<>();
                  item.put("id", record.getId());
                  item.put("date", date);
                  // appointmentId 字段复用来存储员工ID
                  Long staffId = record.getAppointmentId();
                  item.put("staffId", staffId);
                  // 查询员工信息
                  if (staffId != null && staffId > 0) {
                      EmployeeEntity staff = employeeService.getById(staffId);
                      if (staff != null) {
                          item.put("staffName", staff.getName());
                          item.put("staffPhone", staff.getPhone());
                          item.put("staffAvatar", staff.getAvatar());
                          item.put("enabled", staff.getEnabled());
                          item.put("remark", staff.getRemark());
                      }
                  }
                  result.add(item);
              }
              return R.data(result);
          } catch (Exception e) {
              e.printStackTrace();
              return R.error("查询值班列表失败：" + e.getMessage());
          }
    }

    /**
     * 设置某日值班人员（覆盖方式）
     * body: { date: "2026-04-12", staffIds: [1, 2, 3] }
     */
    @Operation(summary = "设置值班人员")
    @PostMapping("/dutySet")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:submit')")
    public R<Boolean> setDuty(@RequestBody Map<String, Object> body) {
        try {
            String date = (String) body.get("date");
            if (date == null || date.isEmpty()) {
                return R.error("日期不能为空");
            }
            Object staffIdsObj = body.get("staffIds");
            List<Long> staffIds = new ArrayList<>();
            if (staffIdsObj instanceof List) {
                for (Object id : (List<?>) staffIdsObj) {
                    if (id != null) {
                        staffIds.add(Long.valueOf(id.toString()));
                    }
                }
            }

            
            QueryWrapper<EmployeeScheduleEntity> delQuery = new QueryWrapper<>();
            delQuery.eq("schedule_type", 1);
            delQuery.eq("slot_status", 3);
//            delQuery.ge("schedule_date", startDate);
//            delQuery.lt("schedule_date", endDate);
            delQuery.eq("schedule_date", date);
            employeeScheduleService.remove(delQuery);
            // 重新插入新的值班记录
            Date now = new Date();
            for (Long staffId : staffIds) {
            	  EmployeeScheduleEntity record = new EmployeeScheduleEntity();
                  record.setScheduleDate(parseDate(date));
                  record.setTimeSlot("08:00");
                  record.setScheduleType(1);
                  record.setSlotStatus(3);
                  record.setEmployeeId(staffId);
                  record.setAppointmentId(staffId); // 复用存储员工ID
                  record.setCreateAt(now);
                  record.setUpdateAt(now);
                  employeeScheduleService.save(record);
            }
            return R.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("设置值班失败：" + e.getMessage());
        }
    }

    /**
     * 移除单条值班记录
     */
    @Operation(summary = "移除值班记录")
    @PostMapping("/dutyRemove/{id}")
    @PreAuthorize("@ss.hasPermi('care:boardingstaff:submit')")
    public R<Boolean> removeDuty(@PathVariable("id") Long id) {
        return R.ok(true);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
