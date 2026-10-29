
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsDeviceEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物GPS设备表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsDeviceVO对象")
public class GpsDeviceVO extends GpsDeviceEntity {
	private static final long serialVersionUID = 1L;
	private String lastAddress;
	private String lastUpdateAt;
}
