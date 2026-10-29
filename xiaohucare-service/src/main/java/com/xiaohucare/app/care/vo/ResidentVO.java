
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.ResidentEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 长者档案表 视图实体类
 * @author xiaohucare
 * @date 2026-10-21
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ResidentVO对象")
public class ResidentVO extends ResidentEntity {
	private static final long serialVersionUID = 1L;
}
