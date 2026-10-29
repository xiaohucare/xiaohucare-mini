
package com.xiaohucare.app.gps.dto;

import com.xiaohucare.app.gps.entity.GpsDeviceLocationCacheEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 设备位置缓存表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class GpsDeviceLocationCacheDTO extends GpsDeviceLocationCacheEntity {
	private static final long serialVersionUID = 1L;

}
