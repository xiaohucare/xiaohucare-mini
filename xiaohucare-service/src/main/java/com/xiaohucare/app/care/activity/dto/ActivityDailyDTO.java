
package com.xiaohucare.app.care.activity.dto;

import com.xiaohucare.app.care.activity.entity.ActivityDailyEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 日常活动记录表 数据传输对象实体类
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class ActivityDailyDTO extends ActivityDailyEntity {
	private static final long serialVersionUID = 1L;

}
