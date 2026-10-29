
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.StoreArticleEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 门店文章报道表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-17 22:48:48
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class StoreArticleDTO extends StoreArticleEntity {
	private static final long serialVersionUID = 1L;

}
