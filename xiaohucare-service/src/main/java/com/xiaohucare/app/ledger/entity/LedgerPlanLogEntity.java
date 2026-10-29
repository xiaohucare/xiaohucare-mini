package com.xiaohucare.app.ledger.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 计划生成工单执行日志
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@TableName("e_ledger_plan_log")
public class LedgerPlanLogEntity {

	@TableId
	@Schema(description = "主键")
	private Long id;
	@Schema(description = "计划ID")
	private Long planId;
	@Schema(description = "本次生成工单数")
	private Integer genCount;
	@Schema(description = "本次跳过数")
	private Integer skipCount;
	@Schema(description = "跳过原因")
	private String skipReason;
	@Schema(description = "执行结果：1成功 2部分跳过 3失败")
	private Integer execStatus;
	@Schema(description = "失败信息")
	private String errorMsg;
	@Schema(description = "执行时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
}
