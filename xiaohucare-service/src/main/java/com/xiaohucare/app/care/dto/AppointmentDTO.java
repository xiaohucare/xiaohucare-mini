
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.AppointmentEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 预约表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class AppointmentDTO extends AppointmentEntity {
	private static final long serialVersionUID = 1L;

}
