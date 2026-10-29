
package com.xiaohucare.app.gps.vo;
import com.xiaohucare.app.gps.entity.GpsActivityStatisticsEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 宠物活动统计表 视图实体类
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "GpsActivityStatisticsVO对象")
public class GpsActivityStatisticsVO extends GpsActivityStatisticsEntity {
	private static final long serialVersionUID = 1L;
}
