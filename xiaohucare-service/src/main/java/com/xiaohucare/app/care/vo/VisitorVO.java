
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.VisitorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 来访者表 视图实体类
 * @author xiaohucare
 * @date 2026-10-22
 */

@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "VisitorVO对象")
public class VisitorVO extends VisitorEntity {
	private static final long serialVersionUID = 1L;
}
