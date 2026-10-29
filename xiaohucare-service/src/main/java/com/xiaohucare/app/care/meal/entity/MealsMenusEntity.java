package com.xiaohucare.app.care.meal.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 餐饮菜单表
 * 
 * @author xiaohucare
 * @date 2025-11-11 22:48:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_meals_menus")
public class MealsMenusEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	@Excel(name = "")
	private Integer id;
//	/**
//	 * 菜单日期
//	 */
//	@Schema(description = "菜单日期")
//	@JsonFormat(pattern = "yyyy-MM-dd")
//	@Excel(name = "菜单日期", width = 30, dateFormat = "yyyy-MM-dd")
//	private Date menuDate;
	/**
	 * 餐次类型
	 */
	@Schema(description = "餐次类型：1早餐，2午餐，3晚餐，4加餐")
	@Excel(name = "餐次类型", readConverterExp = "1=早餐,2=午餐,3=晚餐,4=加餐")
	private Integer mealType;
	/**
	 * 菜名
	 */
	@Schema(description = "菜名")
	@Excel(name = "菜名")
	private String dishName;
	/**
	 * 菜品图片
	 */
	@Schema(description = "菜品图片")
	private String dishImage;
	/**
	 * 菜品描述
	 */
	@Schema(description = "菜品描述")
	@Excel(name = "菜品描述")
	private String dishDescription;
	/**
	 * 营养信息
	 */
	@Schema(description = "营养信息")
	@Excel(name = "营养信息")
	private String nutritionInfo;
	/**
	 * 适宜人群
	 */
	@Schema(description = "适宜人群")
	@Excel(name = "适宜人群")
	private String suitableFor;
	/**
	 * 禁忌人群
	 */
	@Schema(description = "禁忌人群")
	@Excel(name = "禁忌人群")
	private String contraindicatedFor;
	/**
	 * 创建人ID
	 */
	@Schema(description = "创建人ID")
	@Excel(name = "创建人ID")
	private Integer creatorId;
	/**
	 * 状态：1正常，0停用
	 */
	@Schema(description = "状态：1正常，0停用")
	@Excel(name = "状态：1正常，0停用")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;

}