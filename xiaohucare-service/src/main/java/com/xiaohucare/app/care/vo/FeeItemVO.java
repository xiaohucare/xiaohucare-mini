
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.FeeItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 收费类目表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "FeeItemVO对象")
public class FeeItemVO extends FeeItemEntity {
	private static final long serialVersionUID = 1L;
	/** 套餐内是否必选（0-可选 1-必选），来自关联表 */
	@Schema(description = "是否必选（0-可选 1-必选）")
	private Integer mandatory;
	/** 套餐内数量，来自关联表 */
	@Schema(description = "套餐内数量")
	private Integer quantity;
}

