
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.EmployeeScheduleEntity;
import com.xiaohucare.app.care.vo.EmployeeScheduleVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 员工信息表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
public class EmployeeScheduleWrapper extends BaseEntityWrapper<EmployeeScheduleEntity, EmployeeScheduleVO>  {
    public static EmployeeScheduleWrapper build() {
        return new EmployeeScheduleWrapper();
    }
	@Override
	public EmployeeScheduleVO entityVO(EmployeeScheduleEntity employee) {
		if(employee==null) {
			return null;
		}
		EmployeeScheduleVO employeeVO = BeanUtil.copy(employee, EmployeeScheduleVO.class);
		return employeeVO;
	}

}
