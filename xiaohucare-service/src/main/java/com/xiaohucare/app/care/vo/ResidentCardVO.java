
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.ResidentCardEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 会员卡表 视图实体类
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ResidentCardVO对象")
public class ResidentCardVO extends ResidentCardEntity {
	private static final long serialVersionUID = 1L;
}
