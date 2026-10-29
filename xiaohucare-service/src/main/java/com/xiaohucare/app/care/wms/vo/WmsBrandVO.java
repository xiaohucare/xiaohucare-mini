
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.WmsBrandEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 商品品牌表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "BrandVO对象")
public class WmsBrandVO extends WmsBrandEntity {
	private static final long serialVersionUID = 1L;
}
