
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.DailyArrangementEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物酒店日常安排表 视图实体类
 * @author xiaohucare
 * @date 2026-04-07 08:33:19
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "DailyArrangementVO对象")
public class DailyArrangementVO extends DailyArrangementEntity {
	private static final long serialVersionUID = 1L;
}
