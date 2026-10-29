
package com.xiaohucare.app.care.wrapper;
import java.util.List;

import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.vo.EmployeeVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 员工信息表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
public class EmployeeWrapper extends BaseEntityWrapper<EmployeeEntity, EmployeeVO>  {
    public static EmployeeWrapper build() {
        return new EmployeeWrapper();
    }
	@Override
	public EmployeeVO entityVO(EmployeeEntity employee) {
		if(employee==null) {
			return null;
		}
		EmployeeVO employeeVO = BeanUtil.copy(employee, EmployeeVO.class);
		List<String> hireFileList=StringUtils.splitToList(employee.getHireFile(), ",");
		employeeVO.setHireFileList(hireFileList);
		List<String> leaveFileList=StringUtils.splitToList(employee.getLeaveFile(), ",");
		employeeVO.setLeaveFileList(leaveFileList);
		return employeeVO;
	}

}
