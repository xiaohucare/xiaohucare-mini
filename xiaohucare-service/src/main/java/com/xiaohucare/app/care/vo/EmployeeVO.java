
package com.xiaohucare.app.care.vo;
import java.util.List;

import com.xiaohucare.app.care.entity.EmployeeEntity;

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
@Schema(description = "EmployeeVO对象")
public class EmployeeVO extends EmployeeEntity {
	private static final long serialVersionUID = 1L;
	private List<String>  hireFileList;
	private List<String>  leaveFileList;
}
