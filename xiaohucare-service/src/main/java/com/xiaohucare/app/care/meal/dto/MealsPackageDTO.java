
package com.xiaohucare.app.care.meal.dto;

import com.xiaohucare.app.care.meal.entity.MealsPackageEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮套餐表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-10-20 10:00:00
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class MealsPackageDTO extends MealsPackageEntity {
	private static final long serialVersionUID = 1L;

}
