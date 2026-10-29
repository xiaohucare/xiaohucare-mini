
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsDeviceLocationCacheEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 设备位置缓存表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsDeviceLocationCacheVO对象")
public class GpsDeviceLocationCacheVO extends GpsDeviceLocationCacheEntity {
	private static final long serialVersionUID = 1L;
}
