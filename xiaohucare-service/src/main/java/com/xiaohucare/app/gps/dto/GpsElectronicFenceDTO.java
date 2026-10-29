
package com.xiaohucare.app.gps.dto;

import com.xiaohucare.app.gps.entity.GpsElectronicFenceEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 电子围栏表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class GpsElectronicFenceDTO extends GpsElectronicFenceEntity {
	private static final long serialVersionUID = 1L;

}
