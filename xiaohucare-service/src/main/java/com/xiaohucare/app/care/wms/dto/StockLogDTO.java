
package com.xiaohucare.app.care.wms.dto;

import com.xiaohucare.app.care.wms.entity.StockLogEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 库存变动记录表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class StockLogDTO extends StockLogEntity {
	private static final long serialVersionUID = 1L;

}
