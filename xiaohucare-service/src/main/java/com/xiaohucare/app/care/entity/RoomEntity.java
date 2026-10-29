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
 * 康养房间表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_room")
public class RoomEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 楼栋ID（e_care_building.id）
	 */
	@Schema(description = "楼栋ID")
	@Excel(name = "楼栋ID")
	private Long buildingId;
	/**
	 * 楼层ID（e_care_floor_plan.id）
	 */
	@Schema(description = "楼层ID")
	@Excel(name = "楼层ID")
	private Long floorId;
	/**
	 * 房间编号（如：201）
	 */
	@Schema(description = "房间编号")
	@Excel(name = "房间编号")
	private String roomNo;
	/**
	 * 房间名称
	 */
	@Schema(description = "房间名称")
	@Excel(name = "房间名称")
	private String roomName;
	/**
	 * 房型：1-单人间 2-双人间 3-多人间 4-夫妻房
	 */
	@Schema(description = "房型：1-单人间 2-双人间 3-多人间 4-夫妻房")
	@Excel(name = "房型", readConverterExp = "1=单人间,2=双人间,3=多人间,4=夫妻房")
	private Integer roomType;
	/**
	 * 房间等级：1-普通 2-高级 3-豪华 4-豪华+
	 */
	@Schema(description = "房间等级：1-普通 2-高级 3-豪华 4-豪华+")
	@Excel(name = "房间等级", readConverterExp = "1=普通,2=高级,3=豪华,4=豪华+")
	private Integer roomLevel;
	
	/**
	 * 房间性别要求：0-不限 1-男 2-女
	 */
	@Schema(description = "房间性别要求：0-不限 1-男 2-女")
	@Excel(name = "房间性别要求")
	private Integer genderType;
	/**
	 * 床位总数
	 */
	@Schema(description = "床位总数")
	@Excel(name = "床位总数")
	private Integer bedCount;
	/**
	 * 当前入住人数
	 */
	@Schema(description = "当前入住人数")
	@Excel(name = "当前入住人数")
	private Integer occupiedCount;
	/**
	 * 朝向
	 */
	@Schema(description = "朝向")
	@Excel(name = "朝向")
	private String orientation;
	/**
	 * 房间面积（㎡）
	 */
	@Schema(description = "房间面积（㎡）")
	@Excel(name = "房间面积")
	private BigDecimal area;
	/**
	 * 是否有独立卫浴：0-无 1-有
	 */
	@Schema(description = "是否有独立卫浴：0-无 1-有")
	@Excel(name = "是否有独立卫浴")
	private Integer hasToilet;
	/**
	 * 是否有阳台：0-无 1-有
	 */
	@Schema(description = "是否有阳台：0-无 1-有")
	@Excel(name = "是否有阳台")
	private Integer hasBalcony;
	/**
	 * 是否有床头呼叫器：0-无 1-有
	 */
	@Schema(description = "是否有床头呼叫器：0-无 1-有")
	@Excel(name = "是否有床头呼叫器")
	private Integer hasNurseCall;
	/**
	 * 适老设施（JSON：扶手/防滑地垫/护理床等）
	 */
	@Schema(description = "适老设施（JSON）")
	@Excel(name = "适老设施")
	private String facilityJson;
	/**
	 * 床位日单价（元，房间基准价）
	 */
	@Schema(description = "床位日单价（元）")
	@Excel(name = "床位日单价")
	private BigDecimal dailyPrice;
	/**
	 * 价格类型：0-普通价格 1-会员价格
	 */
	@Schema(description = "价格类型：0-普通价格 1-会员价格")
	@Excel(name = "价格类型")
	private Integer spacePriceType;
	/**
	 * 状态 0-禁用 1-空闲 2-已预订 3-已满房 4-维修中 5-清洁中
	 */
	@Schema(description = "状态 0-禁用 1-空闲 2-已预订 3-已满房 4-维修中 5-清洁中")
	@Excel(name = "状态")
	private Integer status;
	/**
	 * 环境图片（JSON数组）
	 */
	@Schema(description = "环境图片（JSON数组）")
	@Excel(name = "环境图片")
	private String environmentImages;
	/**
	 * 房间监控设备编号
	 */
	@Schema(description = "房间监控设备编号")
	@Excel(name = "房间监控设备编号")
	private String cameraDeviceId;
	/**
	 * 房间说明
	 */
	@Schema(description = "房间说明")
	@Excel(name = "房间说明")
	private String content;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
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

}
