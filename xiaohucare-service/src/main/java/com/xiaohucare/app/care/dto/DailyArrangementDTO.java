
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.DailyArrangementEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物酒店日常安排表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-07 08:33:19
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class DailyArrangementDTO extends DailyArrangementEntity {
	private static final long serialVersionUID = 1L;

}
