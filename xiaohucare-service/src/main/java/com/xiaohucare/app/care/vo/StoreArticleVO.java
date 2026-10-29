
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.StoreArticleEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 门店文章报道表 视图实体类
 * @author xiaohucare
 * @date 2026-04-17 22:48:48
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "StoreArticleVO对象")
public class StoreArticleVO extends StoreArticleEntity {
	private static final long serialVersionUID = 1L;
}
