package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.util.Arrays;
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
import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.app.care.vo.EmployeeHireVO;
import com.xiaohucare.app.care.vo.EmployeeLeaveVO;
import com.xiaohucare.app.care.vo.EmployeeVO;
import com.xiaohucare.app.care.wrapper.EmployeeWrapper;
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
 * 员工信息表
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
@RestController
@RequestMapping("intra/care/employee")
public class EmployeeController extends BaseController{
    @Autowired
    private EmployeeService employeeService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "员工姓名"),
		        @Parameter(name = "level", description = "员工等级"),
		        @Parameter(name = "employeeStatus", description = "员工状态"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:employee:list')")
    public R<VPage<EmployeeVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<EmployeeEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name","level","employeeStatus"), EmployeeEntity.class);
        queryWrapper.orderByDesc("id");
        
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
         queryWrapper.eq(StringUtils.isNotNull(params.get("level")),"level", params.get("level"));
         queryWrapper.eq(StringUtils.isNotNull(params.get("employeeStatus")),"employee_status", params.get("employeeStatus"));
        IPage<EmployeeEntity> pages = employeeService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(EmployeeWrapper.build().toVPageVO(pages));
    }
     @GetMapping("/listAll")
    @PreAuthorize("@ss.hasPermi('care:employee:list')")
    public R<List<EmployeeVO>> listAll(@RequestParam Map<String, Object> params){
        QueryWrapper<EmployeeEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name","level","employeeStatus"), EmployeeEntity.class);
        queryWrapper.orderByDesc("id");
        
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
         queryWrapper.eq(StringUtils.isNotNull(params.get("level")),"level", params.get("level"));
         queryWrapper.eq(StringUtils.isNotNull(params.get("employeeStatus")),"employee_status", params.get("employeeStatus"));
        List<EmployeeEntity> list = employeeService.list(queryWrapper);
        return R.data(EmployeeWrapper.build().listVO(list));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:employee:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<EmployeeEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), EmployeeEntity.class);
         queryWrapper.select("count(id) as totalCount");
         
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=employeeService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:employee:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<EmployeeEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				EmployeeEntity.class);
		queryWrapper.orderByDesc("id");
		 
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<EmployeeEntity> employeeList = employeeService.list(queryWrapper);
		ExcelUtil<EmployeeEntity> util = new ExcelUtil<>(EmployeeEntity.class);
		util.exportExcel(response, employeeList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:employee:detail')")
    public R<EmployeeVO> detail(@PathVariable("id") Long id){
		EmployeeEntity employee = employeeService.getById(id);
	    return R.data(EmployeeWrapper.build().entityVO(employee));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:employee:submit')")
    public R<EmployeeVO> submit(@Valid @RequestBody EmployeeVO employee){
   		if(employee.getId()==null) {
            employee.setCreateAt(new Date());
            employee.setIsDelete(0);
        } 
   	    employee.setEmployeeStatus(0); 
        employee.setUpdateAt(new Date());
    	if(employeeService.saveOrUpdate(employee)) {
    		 return R.ok(employee);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:employee:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(employeeService.removeByIds(Arrays.asList(ids)));
    }
    /**
     * 设置预约数量
     */
    @Operation(summary = "设置预约数量")
    @PostMapping("/modWash")
    @PreAuthorize("@ss.hasPermi('care:employee:hire')")
    public R<Boolean> modWash(@Valid @RequestBody EmployeeVO employeeVO){
        EmployeeEntity employee = employeeService.getById(employeeVO.getId());
        if (employee == null) {
            return R.error("员工不存在");
        }
        employee.setMaxDaily(employeeVO.getMaxDaily()); 
        employee.setWashState(employeeVO.getWashState());
        employee.setUpdateAt(new Date());
        if (employeeService.updateById(employee)) {
            return R.ok(true);
        }
        return R.error("入职办理失败");
    }
    /**
     * 办理入职
     */
    @Operation(summary = "办理入职")
    @PostMapping("/hire")
    @PreAuthorize("@ss.hasPermi('care:employee:hire')")
    public R<Boolean> hire(@Valid @RequestBody EmployeeHireVO hireVO){
        EmployeeEntity employee = employeeService.getById(hireVO.getId());
        if (employee == null) {
            return R.error("员工不存在");
        }
        
        // 更新入职信息
        employee.setEmployeeStatus(1); // 设置为在职
        employee.setHireDate(hireVO.getHireDate()); // 使用前端传入的日期
        
        // 可选字段：入职文件
        if (StringUtils.isNotBlank(hireVO.getHireFile())) {
            employee.setHireFile(hireVO.getHireFile());
        }
        
        // 可选字段：备注
        if (StringUtils.isNotBlank(hireVO.getRemark())) {
            String existingRemark = employee.getRemark() != null ? employee.getRemark() : "";
            employee.setRemark(existingRemark + " [入职备注：" + hireVO.getRemark() + "]");
        }
        employee.setBaseSalary(hireVO.getBaseSalary());
        employee.setBankAccount(hireVO.getBankAccount());
        employee.setBankName(hireVO.getBankName());
        employee.setHireReferee(hireVO.getHireReferee());
        employee.setHireRefereeReason(hireVO.getHireRefereeReason());
        employee.setLevel(hireVO.getLevel());
        employee.setPosition(hireVO.getPosition());
        employee.setUpdateAt(new Date());
        
        if (employeeService.updateById(employee)) {
            return R.ok(true);
        }
        return R.error("入职办理失败");
    }

    /**
     * 办理离职
     */
    @Operation(summary = "办理离职")
    @PostMapping("/leave")
    @PreAuthorize("@ss.hasPermi('care:employee:leave')")
    public R<Boolean> leave(@Valid @RequestBody EmployeeLeaveVO leaveVO){
        EmployeeEntity employee = employeeService.getById(leaveVO.getId());
        if (employee == null) {
            return R.error("员工不存在");
        }
        
        // 更新离职信息
        employee.setEmployeeStatus(10); // 设置为离职
        employee.setLeaveDate(leaveVO.getLeaveDate()); // 使用前端传入的日期
        
        // 可选字段：离职文件
        if (StringUtils.isNotBlank(leaveVO.getLeaveFile())) {
            employee.setLeaveFile(leaveVO.getLeaveFile());
        }
        
        // 必填字段：离职原因（追加到remark中）
        if (StringUtils.isNotBlank(leaveVO.getLeaveReason())) {
            String reason = leaveVO.getLeaveReason();
            String existingRemark = employee.getRemark() != null ? employee.getRemark() : "";
            employee.setRemark(existingRemark + " [离职原因：" + reason + "]");
            employee.setLeaveReason(reason);
        }
        employee.setUpdateAt(new Date());
        if (employeeService.updateById(employee)) {
            return R.ok(true);
        }
        return R.error("离职办理失败");
    }

}
