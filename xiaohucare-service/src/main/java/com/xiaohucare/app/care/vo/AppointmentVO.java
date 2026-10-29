
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.vo.ChargeComboVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 预约表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "AppointmentVO对象")
public class AppointmentVO extends AppointmentEntity {
	private static final long serialVersionUID = 1L;
	
	ChargeComboVO chargeComboVO;
	
	
	CheckinVO  checkinVO;
	
	
	String bookStateName;
}
