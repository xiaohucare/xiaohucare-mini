package com.xiaohucare.app.care.meal.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 餐饮套餐表
 * 
 * @author xiaohucare
 * @date 2026-10-20 10:00:00
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("e_meals_package")
public class MealsPackageEntity extends BaseEntity{
private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
				    @Excel(name = "")
	private Integer id;
	/**
	 * 套餐名称
	 */
		@Schema(description ="套餐名称")
			    @Excel(name = "套餐名称")
	private String packageName;
	/**
	 * 餐次类型
	 */
		@Schema(description ="餐次类型：1早餐，2午餐，3晚餐，4加餐")
			    @Excel(name = "餐次类型", readConverterExp = "1=早餐,2=午餐,3=晚餐,4=加餐")
	private Integer mealType;
	/**
	 * 套餐图片
	 */
		@Schema(description ="套餐图片")
	private String packageImage;
	/**
	 * 包含的菜单ID集合（逗号分隔）
	 */
		@Schema(description ="包含的菜单ID集合")
			    @TableField("menu_ids")
	private String menuIdStr;
	/**
	 * 套餐价格
	 */
		@Schema(description ="套餐价格")
			    @Excel(name = "套餐价格")
	private BigDecimal price;
	/**
	 * 套餐描述
	 */
		@Schema(description ="套餐描述")
			    @Excel(name = "套餐描述")
	private String description;
	/**
	 * 创建人ID
	 */
		@Schema(description ="创建人ID")
			    @Excel(name = "创建人ID")
	private Integer creatorId;
	/**
	 * 状态：1正常，0停用
	 */
		@Schema(description ="状态：1正常，0停用")
			    @Excel(name = "状态：1正常，0停用")
	private Integer status;
	/**
	 * 创建时间
	 */
		@Schema(description ="创建时间")
			    @JsonFormat(pattern = "yyyy-MM-dd")
	    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新时间
	 */
		@Schema(description ="更新时间")
			    @JsonFormat(pattern = "yyyy-MM-dd")
	    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;

}
