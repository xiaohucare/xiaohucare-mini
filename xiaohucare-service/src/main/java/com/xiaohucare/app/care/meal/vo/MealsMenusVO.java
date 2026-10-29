
package com.xiaohucare.app.care.meal.vo;
import com.xiaohucare.app.care.meal.entity.MealsMenusEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮菜单表 视图实体类
 * @author xiaohucare
 * @date 2025-11-11 22:48:39
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "餐饮菜单表")
public class MealsMenusVO extends MealsMenusEntity {
	private static final long serialVersionUID = 1L;
}
