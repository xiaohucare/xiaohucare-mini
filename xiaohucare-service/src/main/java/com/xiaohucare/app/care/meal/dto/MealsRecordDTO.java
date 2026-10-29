
package com.xiaohucare.app.care.meal.dto;

import com.xiaohucare.app.care.meal.entity.MealsRecordEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮记录表 数据传输对象实体类
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class MealsRecordDTO extends MealsRecordEntity {
	private static final long serialVersionUID = 1L;

}
