
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsFenceCameraLinkEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 围栏-摄像头联动配置表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsFenceCameraLinkVO对象")
public class GpsFenceCameraLinkVO extends GpsFenceCameraLinkEntity {
	private static final long serialVersionUID = 1L;
}
