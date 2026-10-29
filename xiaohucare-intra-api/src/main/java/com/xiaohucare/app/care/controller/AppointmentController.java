package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.util.Arrays;
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
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.facade.CareFacade;
import com.xiaohucare.app.care.service.AppointmentService;
import com.xiaohucare.app.care.vo.AppointmentVO;
import com.xiaohucare.app.care.wrapper.AppointmentWrapper;
import com.xiaohucare.app.care.service.ChargeComboService;
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
 * 预约表
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@RestController
@RequestMapping("intra/hotel/appointment")
public class AppointmentController extends BaseController{
	@Autowired
	private CareFacade careFacade;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ChargeComboService chargeComboService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
            @Parameter(name = "appointmentNo", description = "预约编号"),
            @Parameter(name = "appointerPhone", description = "报名人电话"),
            @Parameter(name = "status", description = "状态"),
    })
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('care:appointment:list')")
    public R<VPage<AppointmentVO>> list(@RequestParam Map<String, Object> params) {
        QueryWrapper<AppointmentEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
                Arrays.asList("appointmentNo", "appointerPhone"), AppointmentEntity.class);
        
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("appointmentNo")), "appointment_no", params.get("appointmentNo"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("appointerPhone")), "appointer_phone", params.get("appointerPhone"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")), "status", params.get("status"));
        if (params.containsKey("appointmentDate") && StringUtils.isNotNull(params.get("appointmentDate"))) {
            queryWrapper.eq("DATE_FORMAT(appointment_date,'%Y-%m-%d')", params.get("appointmentDate"));
        }
        IPage<AppointmentEntity> pages = appointmentService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(AppointmentWrapper.build().toVPageVO(pages));
    }

    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:appointment:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException {
        QueryWrapper<AppointmentEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"), AppointmentEntity.class);
        
        queryWrapper.select("count(id) as totalCount");
        if (params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString())));
        }
        Map<String, Object> stat = appointmentService.getMap(queryWrapper);
        return R.data(stat);
    }

    @Operation(summary = "导出")
    @Log(title = "预约管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('care:appointment:list')")
    @PostMapping("/toExport")
    public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        QueryWrapper<AppointmentEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
                AppointmentEntity.class);
        
        queryWrapper.orderByDesc("id");
        List<AppointmentEntity> appointmentList = appointmentService.list(queryWrapper);
        ExcelUtil<AppointmentEntity> util = new ExcelUtil<>(AppointmentEntity.class);
        util.exportExcel(response, appointmentList, "预约数据");
    }

    /**
     * 详情
     */
    @Operation(summary = "详情")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:appointment:detail')")
    public R<AppointmentVO> detail(@PathVariable("id") Long id) {
        AppointmentEntity appointment = appointmentService.getById(id);
        return R.data(AppointmentWrapper.build().entityVO(appointment));
    }

    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:appointment:submit')")
    public R<AppointmentVO> submit(@Valid @RequestBody AppointmentVO appointment) {
        return careFacade.book(appointment);
    }

    /**
     * 确认预约（状态0->1）
     */
    @Operation(summary = "确认预约")
    @PostMapping("/confirm/{id}")
    @PreAuthorize("@ss.hasPermi('care:appointment:submit')")
    public R<Boolean> confirm(@PathVariable("id") Long id) {
//        AppointmentEntity appointment = appointmentService.getById(id);
//        if (appointment == null) {
//            return R.error("预约记录不存在");
//        }
//        if (appointment.getStatus() != 0) {
//            return R.error("当前状态不允许确认");
//        }
//        if(appointment.getComboId()!=null&&appointment.getComboId()>0) {
//   			ChargeComboEntity chargeCombo=chargeComboService.getById(appointment.getComboId());
//   			if(chargeCombo!=null) {
//   				appointment.setComboName(chargeCombo.getComboName());
//   			}
//   		}
//        appointment.setStatus(1);
//        appointment.setUpdateAt(new Date());
//    	return R.ok(appointmentService.updateById(appointment));
        return careFacade.confirm(id);
    }

    /**
     * 转为到店登记（状态1->3）
     */
    @Operation(summary = "转为到店登记")
    @PostMapping("/convert/{id}")
    @PreAuthorize("@ss.hasPermi('care:appointment:submit')")
    public R<Boolean> convert(@PathVariable("id") Long id) {
//        AppointmentEntity appointment = appointmentService.getById(id);
//        if (appointment == null) {
//            return R.error("预约记录不存在");
//        }
//        if (appointment.getStatus() != 1) {
//            return R.error("请先确认预约再转为登记");
//        }
//        appointment.setStatus(3);
//        appointment.setUpdateAt(new Date());
//        return R.ok(appointmentService.updateById(appointment));
        return careFacade.convert(id);
    }

    /**
     * 取消预约（状态->2）
     */
    @Operation(summary = "取消预约")
    @PostMapping("/cancel/{id}")
    @PreAuthorize("@ss.hasPermi('care:appointment:submit')")
    public R<Boolean> cancel(@PathVariable("id") Long id) {
//        AppointmentEntity appointment = appointmentService.getById(id);
//        if (appointment == null) {
//            return R.error("预约记录不存在");
//        }
//        appointment.setStatus(2);
//        appointment.setUpdateAt(new Date());
//        return R.ok(appointmentService.updateById(appointment));
        return careFacade.cancel(id);
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:appointment:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids) {
        return R.ok(appointmentService.removeByIds(Arrays.asList(ids)));
    }

}
