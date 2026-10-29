package com.xiaohucare.app.ledger.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工单流转日志(分派/转派/签到/驳回/延期/结案 留痕)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@TableName("e_ledger_order_log")
public class LedgerOrderLogEntity {

	@TableId
	@Schema(description = "主键")
	private Long id;
	@Schema(description = "工单类别：1探访工单 2巡访工单")
	private Integer orderKind;
	@Schema(description = "工单ID")
	private Long orderId;
	@Schema(description = "工单编号(冗余)")
	private String orderNo;
	@Schema(description = "动作：assign/transfer/revoke/sign/start/finish/delay/cancel/reject/close/revisit")
	private String action;
	@Schema(description = "流转前状态")
	private Integer fromStatus;
	@Schema(description = "流转后状态")
	private Integer toStatus;
	@Schema(description = "原办理人(e_care_employee.id)")
	private Long fromEmployeeId;
	@Schema(description = "新办理人(e_care_employee.id)")
	private Long toEmployeeId;
	@Schema(description = "操作说明/驳回原因等")
	private String content;
	@Schema(description = "操作人")
	private String createBy;
	@Schema(description = "操作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
}
