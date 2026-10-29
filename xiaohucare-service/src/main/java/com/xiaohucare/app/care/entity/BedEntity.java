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
 * 康养床位表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_bed")
public class BedEntity extends BaseEntity {
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
	 * 房间ID（e_care_room.id）
	 */
	@Schema(description = "房间ID")
	@Excel(name = "房间ID")
	private Long roomId;
	/**
	 * 床位编号（如：201-1）
	 */
	@Schema(description = "床位编号")
	@Excel(name = "床位编号")
	private String bedNo;
	/**
	 * 床位名称
	 */
	@Schema(description = "床位名称")
	@Excel(name = "床位名称")
	private String bedName;
	/**
	 * 床位类型：1-普通床 2-护理床 3-康复床
	 */
	@Schema(description = "床位类型：1-普通床 2-护理床 3-康复床")
	@Excel(name = "床位类型")
	private Integer bedType;
	/**
	 * 状态 0-禁用 1-空闲 2-占用 3-预订 4-维修 5-清洁
	 */
	@Schema(description = "状态 0-禁用 1-空闲 2-占用 3-预订 4-维修 5-清洁")
	@Excel(name = "状态")
	private Integer status;
	/**
	 * 当前入住长者ID
	 */
	@Schema(description = "当前入住长者ID")
	@Excel(name = "当前入住长者ID")
	private Long curResidentId;
	/**
	 * 当前入住长者姓名
	 */
	@Schema(description = "当前入住长者姓名")
	@Excel(name = "当前入住长者姓名")
	private String curResidentName;
	/**
	 * 当前入住登记ID（e_care_checkin.id）
	 */
	@Schema(description = "当前入住登记ID")
	@Excel(name = "当前入住登记ID")
	private Long checkinId;
	/**
	 * 入住时间
	 */
	@Schema(description = "入住时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "入住时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date checkinTime;
	/**
	 * 床位日单价（元）
	 */
	@Schema(description = "床位日单价（元）")
	@Excel(name = "床位日单价")
	private BigDecimal dailyPrice;
	/**
	 * 使用次数
	 */
	@Schema(description = "使用次数")
	@Excel(name = "使用次数")
	private Integer useNum;
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
