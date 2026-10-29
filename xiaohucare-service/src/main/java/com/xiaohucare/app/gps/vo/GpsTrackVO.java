
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsTrackEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物GPS轨迹表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsTrackVO对象")
public class GpsTrackVO extends GpsTrackEntity {
	private static final long serialVersionUID = 1L;
}
