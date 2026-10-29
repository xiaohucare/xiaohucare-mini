
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.StorePaperEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 企业公报 视图实体类
 * @author xiaohucare
 * @date 2026-04-15 19:01:57
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "StorePaperVO对象")
public class StorePaperVO extends StorePaperEntity {
	private static final long serialVersionUID = 1L;
}
