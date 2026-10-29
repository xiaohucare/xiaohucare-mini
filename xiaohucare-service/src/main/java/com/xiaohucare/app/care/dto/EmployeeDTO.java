
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.EmployeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 员工信息表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class EmployeeDTO extends EmployeeEntity {
	private static final long serialVersionUID = 1L;

}
