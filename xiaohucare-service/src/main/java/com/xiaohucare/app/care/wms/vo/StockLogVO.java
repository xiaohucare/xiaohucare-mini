
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.StockLogEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 库存变动记录表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "StockLogVO对象")
public class StockLogVO extends StockLogEntity {
	private static final long serialVersionUID = 1L;
}
