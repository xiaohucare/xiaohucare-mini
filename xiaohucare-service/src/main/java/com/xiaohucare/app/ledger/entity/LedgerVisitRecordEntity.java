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
 * 上门探访记录(依附探访工单,一单一记录)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_visit_record")
public class LedgerVisitRecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "记录ID")
	private Long id;
	@Schema(description = "探访工单ID")
	@Excel(name = "工单ID")
	private Long orderId;
	@Schema(description = "老人ID")
	private Long elderId;
	@Schema(description = "老人姓名(冗余)")
	@Excel(name = "老人姓名")
	private String elderName;
	@Schema(description = "实际探访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date realVisitTime;
	@Schema(description = "陪同人员")
	private String accompanyPerson;
	@Schema(description = "精神状态")
	private String mentalStatus;
	@Schema(description = "饮食情况")
	private String dietStatus;
	@Schema(description = "睡眠情况")
	private String sleepStatus;
	@Schema(description = "行动情况")
	private String moveStatus;
	@Schema(description = "血压(如 130/85)")
	private String bloodPressure;
	@Schema(description = "血糖")
	private String bloodSugar;
	@Schema(description = "用药依从性")
	private String medicineCompliance;
	@Schema(description = "服务需求集(字典ledger_service_item编码,逗号分隔)")
	private String demandInfo;
	@Schema(description = "需求详细描述")
	private String demandDetail;
	@Schema(description = "居家环境风险")
	private String homeEnvRisk;
	@Schema(description = "服务建议")
	private String serviceSuggest;
	@Schema(description = "建议下次探访时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nextVisitTime;
	@Schema(description = "现场图片URL集(逗号分隔)")
	private String visitImages;
	@Schema(description = "老人/家属电子签名图URL")
	private String signImgUrl;
	@Schema(description = "其他说明")
	private String remark;
	@Schema(description = "填写人")
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
