
package com.xiaohucare.app.care.meal.vo;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xiaohucare.app.care.meal.entity.MealsPackageEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮套餐表 视图实体类
 * @author xiaohucare
 * @date 2026-10-20 10:00:00
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "餐饮套餐表")
public class MealsPackageVO extends MealsPackageEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 包含的菜单ID数组（视图层数组，对应实体 menuIdStr 逗号串）
	 */
	@Schema(description ="包含的菜单ID数组")
	@TableField(exist = false)
	private List<Integer> menuIds;
}
