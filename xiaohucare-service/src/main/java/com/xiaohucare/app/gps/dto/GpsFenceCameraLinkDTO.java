
package com.xiaohucare.app.gps.dto;

import com.xiaohucare.app.gps.entity.GpsFenceCameraLinkEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 围栏-摄像头联动配置表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class GpsFenceCameraLinkDTO extends GpsFenceCameraLinkEntity {
	private static final long serialVersionUID = 1L;

}
