
package com.xiaohucare.app.care.activity.vo;
import com.xiaohucare.app.care.activity.entity.ActivityDailyEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 日常活动记录表 视图实体类
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "日常活动记录表")
public class ActivityDailyVO extends ActivityDailyEntity {
	private static final long serialVersionUID = 1L;
}
