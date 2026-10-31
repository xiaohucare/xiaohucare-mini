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
 * 智慧台账-老人档案(居家上门服务主档,可选关联 e_resident)
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_ledger_elder")
public class LedgerElderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@TableId
	@Schema(description = "老人ID")
	@Excel(name = "老人ID")
	private Long id;
	@Schema(description = "关联在住长者ID(e_resident.id),居家老人为空")
	private Long residentId;
	@Schema(description = "档案来源：1社区居家登记 2机构在住长者同步")
	private Integer elderSource;
	@Schema(description = "台账编号(LT+区域+流水)")
	@Excel(name = "台账编号")
	private String elderNo;
	@Schema(description = "老人姓名")
	@Excel(name = "姓名")
	private String name;
	@Schema(description = "身份证号")
	@Excel(name = "身份证号")
	private String idCard;
	@Schema(description = "性别：0未知 1男 2女")
	@Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
	private Integer gender;
	@Schema(description = "出生日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date birthday;
	@Schema(description = "民族")
	private String nation;
	@Schema(description = "老人联系电话")
	@Excel(name = "联系电话")
	private String phone;
	@Schema(description = "现居住地址")
	@Excel(name = "现居住地址")
	private String address;
	@Schema(description = "户籍地址")
	private String householdAddress;
	@Schema(description = "老人类型编码集(逗号分隔,字典ledger_elder_type)")
	@Excel(name = "老人类型")
	private String elderTypes;
	@Schema(description = "照料等级：1自理 2半自理 3失能")
	@Excel(name = "照料等级", readConverterExp = "1=自理,2=半自理,3=失能")
	private Integer careLevel;
	@Schema(description = "自理能力：1完全自理 2部分自理 3不能自理")
	private Integer selfCareAbility;
	@Schema(description = "认知状况：1正常 2疑似障碍 3确诊障碍")
	private Integer cognitiveStatus;
	@Schema(description = "ADL日常生活能力评分(0-100)")
	private Integer adlScore;
	@Schema(description = "跌倒风险：1低 2中 3高")
	private Integer fallRisk;
	@Schema(description = "基础慢病")
	private String chronicDisease;
	@Schema(description = "过敏史")
	private String allergy;
	@Schema(description = "长期用药情况")
	private String medicineInfo;
	@Schema(description = "总体健康状况")
	private String healthStatus;
	@Schema(description = "帮扶责任人ID(e_care_employee.id)")
	private Long helperEmployeeId;
	@Schema(description = "帮扶责任人姓名(冗余)")
	@Excel(name = "帮扶责任人")
	private String helperName;
	@Schema(description = "归属片区")
	private String areaName;
	@Schema(description = "风险等级：1低/一般 2关注 3高")
	@Excel(name = "风险等级", readConverterExp = "1=低,2=关注,3=高")
	private Integer riskLevel;
	@Schema(description = "档案附件URL集(逗号分隔)")
	private String archiveImages;
	@Schema(description = "档案状态：0注销 1正常 2停用")
	@Excel(name = "档案状态", readConverterExp = "0=注销,1=正常,2=停用")
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
