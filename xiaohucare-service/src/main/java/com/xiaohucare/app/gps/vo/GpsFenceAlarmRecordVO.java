
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsFenceAlarmRecordEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 围栏报警记录表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsFenceAlarmRecordVO对象")
public class GpsFenceAlarmRecordVO extends GpsFenceAlarmRecordEntity {
	private static final long serialVersionUID = 1L;
}
