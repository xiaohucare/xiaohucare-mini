
package com.xiaohucare.app.care.meal.dto;

import com.xiaohucare.app.care.meal.entity.MealsMenusEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮菜单表 数据传输对象实体类
 * @author xiaohucare
 * @date 2025-11-11 22:48:39
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class MealsMenusDTO extends MealsMenusEntity {
	private static final long serialVersionUID = 1L;

}
