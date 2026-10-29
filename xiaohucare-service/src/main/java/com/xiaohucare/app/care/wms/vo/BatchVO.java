
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.BatchEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 库存批次表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "BatchVO对象")
public class BatchVO extends BatchEntity {
	private static final long serialVersionUID = 1L;
}
