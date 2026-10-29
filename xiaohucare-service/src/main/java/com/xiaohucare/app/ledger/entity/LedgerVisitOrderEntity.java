package com.xiaohucare.app.ledger.entity;

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
 * 探访工单(关爱探访)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_visit_order")
public class LedgerVisitOrderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "工单ID")
	@Excel(name = "工单ID")
	private Long id;
	@Schema(description = "机构名称(冗余)")
	@Excel(name = "机构名称")
	private String storeName;
	@Schema(description = "老人ID")
	private Long elderId;
	@Schema(description = "老人姓名(冗余)")
	@Excel(name = "老人姓名")
	private String elderName;
	@Schema(description = "工单编号(TF+yyyyMMdd+4位流水)")
	@Excel(name = "工单编号")
	private String orderNo;
	@Schema(description = "探访类型：1常规关爱 2需求评估 3服务跟进 4回访重访")
	@Excel(name = "探访类型", readConverterExp = "1=常规关爱,2=需求评估,3=服务跟进,4=回访重访")
	private Integer visitType;
	@Schema(description = "计划探访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "计划探访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date planVisitTime;
	@Schema(description = "指派巡访员(e_care_employee.id),空为待分配")
	private Long employeeId;
	@Schema(description = "巡访员姓名(冗余)")
	@Excel(name = "巡访员")
	private String employeeName;
	@Schema(description = "状态：1待分配 2待上门 3已完成 4延期 5取消 6重访")
	@Excel(name = "状态", readConverterExp = "1=待分配,2=待上门,3=已完成,4=延期,5=取消,6=重访")
	private Integer orderStatus;
	@Schema(description = "上门签到时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date signTime;
	@Schema(description = "签到定位地址")
	private String signLocation;
	@Schema(description = "探访开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date visitStartTime;
	@Schema(description = "探访结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date visitEndTime;
	@Schema(description = "来源：1计划生成 2人工新建")
	@Excel(name = "来源", readConverterExp = "1=计划生成,2=人工新建")
	private Integer sourceType;
	@Schema(description = "探访计划ID(计划生成时回填)")
	private Long planId;
	@Schema(description = "延期原因")
	private String delayReason;
	@Schema(description = "取消原因")
	private String cancelReason;
	@Schema(description = "工单备注")
	private String remark;
	@Schema(description = "创建者")
	private String createBy;
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	@Schema(description = "更新者")
	private String updateBy;
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;
}
