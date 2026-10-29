
package com.xiaohucare.app.care.activity.vo;
import com.xiaohucare.app.care.activity.entity.ActivityParticipantEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 活动参与记录表 视图实体类
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "活动参与记录表")
public class ActivityParticipantVO extends ActivityParticipantEntity {
	private static final long serialVersionUID = 1L;
}
