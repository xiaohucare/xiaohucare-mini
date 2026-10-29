
package com.xiaohucare.app.care.activity.dto;

import com.xiaohucare.app.care.activity.entity.ActivityParticipantEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 活动参与记录表 数据传输对象实体类
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class ActivityParticipantDTO extends ActivityParticipantEntity {
	private static final long serialVersionUID = 1L;

}
