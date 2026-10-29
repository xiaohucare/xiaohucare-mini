
package com.xiaohucare.app.care.activity.vo;
import com.xiaohucare.app.care.activity.entity.ActivityTypeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 活动类型表 视图实体类
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "活动类型表")
public class ActivityTypeVO extends ActivityTypeEntity {
	private static final long serialVersionUID = 1L;
}
