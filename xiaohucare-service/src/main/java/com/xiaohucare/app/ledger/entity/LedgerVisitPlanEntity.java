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
 * 探访/巡访计划配置(工单自动化生成依据)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_visit_plan")
public class LedgerVisitPlanEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "计划ID")
	private Long id;
	@Schema(description = "计划名称")
	@Excel(name = "计划名称")
	private String planName;
	@Schema(description = "计划类型：1探访计划 2巡访计划")
	@Excel(name = "计划类型", readConverterExp = "1=探访计划,2=巡访计划")
	private Integer planType;
	@Schema(description = "针对老人类型集(逗号分隔,空为全部)")
	private String elderTypes;
	@Schema(description = "针对照料等级集(逗号分隔,空为全部)")
	private String careLevels;
	@Schema(description = "针对风险等级(空为全部)：1低 2中 3高")
	private Integer riskLevel;
	@Schema(description = "频次周期：1每天 2每周 3每月")
	@Excel(name = "频次周期", readConverterExp = "1=每天,2=每周,3=每月")
	private Integer frequencyType;
	@Schema(description = "每周期生成次数")
	private Integer frequencyTimes;
	@Schema(description = "生成工单的探访/巡访类型默认值")
	private Integer defaultVisitType;
	@Schema(description = "自动指派巡访员(e_care_employee.id,空为生成待分配工单)")
	private Long autoAssignEmployeeId;
	@Schema(description = "最近一次执行生成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastGenTime;
	@Schema(description = "下次执行时间(定时任务扫描依据)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nextGenTime;
	@Schema(description = "状态：0停用 1启用")
	@Excel(name = "状态", readConverterExp = "0=停用,1=启用")
	private Integer state;
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
