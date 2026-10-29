
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.EmployeeScheduleEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 员工信息表 视图实体类
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "EmployeeScheduleVO对象")
public class EmployeeScheduleVO extends EmployeeScheduleEntity {
	private static final long serialVersionUID = 1L;
}
