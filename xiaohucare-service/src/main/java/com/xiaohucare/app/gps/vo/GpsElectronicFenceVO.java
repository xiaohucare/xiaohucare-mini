
package com.xiaohucare.app.gps.vo;
import java.util.List;

import com.xiaohucare.app.gps.entity.GpsElectronicFenceEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 电子围栏表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsElectronicFenceVO对象")
public class GpsElectronicFenceVO extends GpsElectronicFenceEntity {
	private static final long serialVersionUID = 1L;
	
	List<GpsCoordinateVo> polygonPointList;
}
