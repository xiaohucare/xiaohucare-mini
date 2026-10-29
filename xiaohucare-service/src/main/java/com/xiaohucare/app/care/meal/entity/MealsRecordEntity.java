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
 * 餐饮记录表
 * 
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_meals_record")
public class MealsRecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	@Excel(name = "")
	private Integer id;
	/**
	 * 老人ID
	 */
	@Schema(description = "老人ID")
	@Excel(name = "老人ID")
	private Integer residentId;
	/**
	 * 用餐对象类型：所有人，专人专餐
	 */
	@Schema(description = "用餐对象类型：所有人，专人专餐")
	private String mealTargetType;
	/**
	 * 专人专餐时的用餐老人ID集合（逗号分隔）
	 */
	@Schema(description = "专人专餐老人ID集合")
	@TableField("resident_ids")
	private String residentIdStr;
	/**
	 * 关联套餐ID
	 */
	@Schema(description = "关联套餐ID")
	private Integer packageId;
	/**
	 * 套餐名称
	 */
	@Schema(description = "套餐名称")
	private String packageName;
	/**
	 * 用餐日期
	 */
	@Schema(description = "用餐日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "用餐日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date mealDate;
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
	 * 份量
	 */
	@Schema(description = "份量")
	@Excel(name = "份量")
	private BigDecimal quantity;
	/**
	 * 满意度
	 */
	@Schema(description = "满意度")
	@Excel(name = "满意度")
	private String satisfactionLevel;
	/**
	 * 反馈意见
	 */
	@Schema(description = "反馈意见")
	@Excel(name = "反馈意见")
	private String feedback;
	/**
	 * 照片记录（逗号分隔）
	 */
	@Schema(description = "照片记录")
	private String mealPhotos;
	/**
	 * 服务人员ID
	 */
	@Schema(description = "服务人员ID")
	@Excel(name = "服务人员ID")
	private Integer servedBy;
	/**
	 * 供餐时间
	 */
	@Schema(description = "供餐时间")
	@Excel(name = "供餐时间")
	private String servedTime;
	/**
	 * 状态：1正常，0取消
	 */
	@Schema(description = "状态：1正常，0取消")
	@Excel(name = "状态：1正常，0取消")
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