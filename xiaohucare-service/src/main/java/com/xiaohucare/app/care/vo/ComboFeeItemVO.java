
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.ComboFeeItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 套餐费用项关联表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 13:47:57
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ComboFeeItemVO对象")
public class ComboFeeItemVO extends ComboFeeItemEntity {
	private static final long serialVersionUID = 1L;
}
