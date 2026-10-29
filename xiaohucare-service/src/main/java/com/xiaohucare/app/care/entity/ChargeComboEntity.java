package com.xiaohucare.app.care.entity;

import java.math.BigDecimal;
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
 * 收费套餐表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_charge_combo")
public class ChargeComboEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	@Schema(description = "收费类型：0 住养收费 1 服务收费")
	@Excel(name = "收费类型：0 住养收费 1 服务收费")
	private Integer chargeKind;
	/**
	 * 套餐名称
	 */
	@Schema(description = "套餐名称")
	@Excel(name = "套餐名称")
	private String comboName;
	/**
	 * 房型性别要求：0-不限 1-男 2-女
	 */
	@Schema(description = "房型性别要求：0-不限 1-男 2-女")
	@Excel(name = "房型性别要求", readConverterExp = "0=不限,1=男,2=女")
	private Integer genderRequired;

	private String coverImage;
	/**
	 * 套餐说明
	 */
	@Schema(description = "套餐说明")
	@Excel(name = "套餐说明")
	private String description;
	/**
	 * 是否启用（0-禁用 1-启用）
	 */
	@Schema(description = "是否启用（0-禁用 1-启用）")
	@Excel(name = "是否启用（0-禁用 1-启用）")
	private Integer status;
	/**
	 * 是否推荐（0-否 1-是）
	 */
	@Schema(description = "是否推荐（0-否 1-是）")
	@Excel(name = "是否推荐（0-否 1-是）")
	private Integer recommended;
	/**
	 * 计费周期：0-按年 1-按月 2-按天
	 */
	@Schema(description = "计费周期：0-按年 1-按月 2-按天")
	@Excel(name = "计费周期", readConverterExp = "0=按年,1=按月,2=按天")
	private Integer chargeCycle;
	/**
	 * 年费价格（元/年）：住满一年按此价，不满一年回退月价
	 */
	@Schema(description = "年费价格（元/年）")
	@Excel(name = "年费价格（元/年）")
	private BigDecimal yearPrice;
	/**
	 * 月费价格（元/月）：不满一年按此价，不满一月回退天价
	 */
	@Schema(description = "月费价格（元/月）")
	@Excel(name = "月费价格（元/月）")
	private BigDecimal monthPrice;
	/**
	 * 日费价格（元/天）：不满一月按此价
	 */
	@Schema(description = "日费价格（元/天）")
	@Excel(name = "日费价格（元/天）")
	private BigDecimal dayPrice;
	
	/**
	 * 房间类型（单人间/双人间/多人间/夫妻房，自由文本，与房型 room_type 取值一致）
	 */
	@Schema(description = "房间类型（单人间/双人间/多人间/夫妻房）")
	@Excel(name = "房间类型")
	private String roomType;
	/**
	 * 押金金额（元）
	 */
	@Schema(description = "押金金额（元）")
	@Excel(name = "押金金额（元）")
	private BigDecimal depositAmount;
	/**
	 * 优惠总价格（元）
	 */
	@Schema(description = "优惠总价格（元）")
	@Excel(name = "优惠总价格（元）")
	private BigDecimal totalPrice;
	/**
	 * 总原价（元）
	 */
	@Schema(description = "总原价（元）")
	@Excel(name = "总原价（元）")
	private BigDecimal totalOrgPrice;
	/**
	 * 是否有摄像头：0-无 1-有
	 */
	@Schema(description = "是否有摄像头：0-无 1-有")
	@Excel(name = "是否有摄像头：0-无 1-有")
	private Integer hasCamera;
	/**
	 * 是否有空调：0-无 1-有
	 */
	@Schema(description = "是否有空调：0-无 1-有")
	@Excel(name = "是否有空调：0-无 1-有")
	private Integer hasAirConditioner;
	/**
	 * 饮水服务：0-无 1-人工送水 2-直饮水
	 */
	@Schema(description = "饮水服务：0-无 1-人工送水 2-直饮水")
	@Excel(name = "饮水服务：0-无 1-人工送水 2-直饮水")
	private Integer waterType;
	/**
	 * 用餐服务：0-自助餐 1-送餐到房 2-食堂就餐
	 */
	@Schema(description = "用餐服务：0-自助餐 1-送餐到房 2-食堂就餐")
	@Excel(name = "用餐服务：0-自助餐 1-送餐到房 2-食堂就餐")
	private Integer feedingType;
	/**
	 * 是否有床头呼叫器：0-无 1-有
	 */
	@Schema(description = "是否有床头呼叫器：0-无 1-有")
	@Excel(name = "是否有床头呼叫器：0-无 1-有")
	private Integer hasNurseCall;
	/**
	 * 是否有紧急按钮：0-无 1-有
	 */
	@Schema(description = "是否有紧急按钮：0-无 1-有")
	@Excel(name = "是否有紧急按钮：0-无 1-有")
	private Integer hasEmergencyBell;
	/**
	 * 房型模式：0-多人间 1-单人间
	 */
	@Schema(description = "房型模式：0-多人间 1-单人间")
	@Excel(name = "房型模式：0-多人间 1-单人间")
	private Integer roomMode;
	/**
	 * 环境图片（JSON数组）
	 */
	@Schema(description = "环境图片（JSON数组）")
	@Excel(name = "环境图片（JSON数组）")
	private String environmentImages;
	/**
	 * 预约次数
	 */
	@Schema(description = "预约次数")
	@Excel(name = "预约次数")
	private Integer bookNum;
	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@Excel(name = "排序")
	private Integer sortOrder;
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
	/**
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
	/**
	 * 更新者
	 */
	@Schema(description = "更新者")
	@Excel(name = "更新者")
	private String updateBy;

	/**
	 * 适用护理等级列表（JSON数组）
	 */
	@Schema(description = "适用护理等级列表（JSON数组）")
	@Excel(name = "适用护理等级列表（JSON数组）")
	private String suitableFor;
	/**
	 * 人气值（选择次数）
	 */
	@Schema(description = "人气值（选择次数）")
	@Excel(name = "人气值（选择次数）")
	private Integer popularity;
	@Schema(description = "预计时长")
	@Excel(name = "预计时长（分钟）")
	private Integer duration;
	/**
	 * 可住长者数
	 */
	@Schema(description = "可住长者数")
	@Excel(name = "可住长者数")
	private Integer maxResidents;
	/**
	 * 房间面积（如：25㎡）
	 */
	@Schema(description = "房间面积（如：25㎡）")
	@Excel(name = "房间面积")
	private String roomArea;
}