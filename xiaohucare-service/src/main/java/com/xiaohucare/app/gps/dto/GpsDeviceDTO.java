
package com.xiaohucare.app.gps.dto;

import com.xiaohucare.app.gps.entity.GpsDeviceEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物GPS设备表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class GpsDeviceDTO extends GpsDeviceEntity {
	private static final long serialVersionUID = 1L;

}
