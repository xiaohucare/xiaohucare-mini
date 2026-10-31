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
 * 巡访工单(安全巡查/隐患处置)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_patrol_order")
public class LedgerPatrolOrderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "巡访工单ID")
	@Excel(name = "工单ID")
	private Long id;
	@Schema(description = "关联老人ID,院内巡访可空")
	private Long elderId;
	@Schema(description = "老人姓名(冗余)")
	@Excel(name = "关联老人")
	private String elderName;
	@Schema(description = "工单编号(XF+yyyyMMdd+4位流水)")
	@Excel(name = "工单编号")
	private String orderNo;
	@Schema(description = "巡访类型：1日常安全 2高危专项 3投诉巡访 4隐患复查")
	@Excel(name = "巡访类型", readConverterExp = "1=日常安全,2=高危专项,3=投诉巡访,4=隐患复查")
	private Integer patrolType;
	@Schema(description = "本次巡访风险等级：1一般 2关注 3高风险")
	@Excel(name = "风险等级", readConverterExp = "1=一般,2=关注,3=高风险")
	private Integer riskLevel;
	@Schema(description = "计划巡访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "计划巡访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date planPatrolTime;
	@Schema(description = "指派巡访员(e_care_employee.id)")
	private Long employeeId;
	@Schema(description = "巡访员姓名(冗余)")
	@Excel(name = "巡访员")
	private String employeeName;
	@Schema(description = "状态：1待分派 2待巡访 3处置中 4待复查 5已结案 6取消")
	@Excel(name = "状态", readConverterExp = "1=待分派,2=待巡访,3=处置中,4=待复查,5=已结案,6=取消")
	private Integer orderStatus;
	@Schema(description = "延期原因")
	private String delayReason;
	@Schema(description = "延期登记时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date delayTime;
	@Schema(description = "累计延期次数")
	private Integer delayCount;
	@Schema(description = "巡访地点")
	@Excel(name = "巡访地点")
	private String patrolPlace;
	@Schema(description = "到场签到时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date signTime;
	@Schema(description = "实际巡访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "实际巡访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date realPatrolTime;
	@Schema(description = "现场拍照URL集(逗号分隔)")
	private String patrolImages;
	@Schema(description = "初步处置意见")
	private String disposeOpinion;
	@Schema(description = "整改期限")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date rectifyDeadline;
	@Schema(description = "整改责任人(e_care_employee.id)")
	private Long rectifyEmployeeId;
	@Schema(description = "整改责任人姓名(冗余)")
	private String rectifyEmployeeName;
	@Schema(description = "复查完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reviewTime;
	@Schema(description = "复查结论")
	private String reviewResult;
	@Schema(description = "复查人(登录账号)")
	private String reviewBy;
	@Schema(description = "来源工单ID(隐患复查时指向原巡访单)")
	private Long sourceOrderId;
	@Schema(description = "来源：1计划生成 2人工上报 3风险预警触发")
	@Excel(name = "来源", readConverterExp = "1=计划生成,2=人工上报,3=风险预警")
	private Integer sourceType;
	@Schema(description = "巡访计划ID")
	private Long planId;
	@Schema(description = "取消原因")
	private String cancelReason;
	@Schema(description = "取消时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date cancelTime;
	@Schema(description = "备注")
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
