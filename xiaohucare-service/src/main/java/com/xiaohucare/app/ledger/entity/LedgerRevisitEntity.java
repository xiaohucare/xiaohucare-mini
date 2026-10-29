package com.xiaohucare.app.ledger.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工单回访记录(探访/巡访完工后的满意度回访,不合格触发重访)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_revisit")
public class LedgerRevisitEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "主键")
	private Long id;
	@Schema(description = "被回访工单类别：1探访工单 2巡访工单")
	private Integer orderKind;
	@Schema(description = "被回访工单ID")
	private Long orderId;
	@Schema(description = "老人ID")
	private Long elderId;
	@Schema(description = "回访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date revisitTime;
	@Schema(description = "回访人(e_care_employee.id)")
	private Long revisitEmployeeId;
	@Schema(description = "满意度：1满意 2一般 3不满意")
	private Integer satisfaction;
	@Schema(description = "回访是否合格：0不合格(触发重访工单) 1合格")
	private Integer isQualified;
	@Schema(description = "不合格时生成的重访工单ID")
	private Long revisitOrderId;
	@Schema(description = "回访记录")
	private String content;
	@Schema(description = "创建者")
	private String createBy;
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
}
