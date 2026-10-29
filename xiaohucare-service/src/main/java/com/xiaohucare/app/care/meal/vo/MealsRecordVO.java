
package com.xiaohucare.app.care.meal.vo;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xiaohucare.app.care.meal.entity.MealsRecordEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 餐饮记录表 视图实体类
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "餐饮记录表")
public class MealsRecordVO extends MealsRecordEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 专人专餐老人ID集合（视图层数组，对应实体 residentIdStr 逗号串）
	 */
	@Schema(description ="专人专餐老人ID集合")
	@TableField(exist = false)
	private List<Integer> residentIds;
}
