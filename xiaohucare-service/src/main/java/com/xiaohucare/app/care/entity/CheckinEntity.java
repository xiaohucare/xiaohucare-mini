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
 * 到店登记表（一人一单：一位长者一条登记，同住场景按床位/房间维度反查）
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_checkin")
public class CheckinEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	@Excel(name = "用户ID")
	private Long memberId;
	/**
	 * 登记单号
	 */
	@Schema(description = "登记单号")
	@Excel(name = "登记单号")
	private String checkinNo;
	/**
	 * 关联预约ID
	 */
	@Schema(description = "关联预约ID")
	@Excel(name = "关联预约ID")
	private Long appointmentId;
	/**
	 * 主入住长者ID（关联 e_resident.id）
	 */
	@Schema(description = "主入住长者ID")
	@Excel(name = "主入住长者ID")
	private Long residentId;
	/**
	 * 长者编号
	 */
	@Schema(description = "长者编号")
	@Excel(name = "长者编号")
	private String residentNo;
	/**
	 * 长者姓名
	 */
	@Schema(description = "长者姓名")
	@Excel(name = "长者姓名")
	private String residentName;
	/**
	 * 紧急联系人/家属姓名
	 */
	@Schema(description = "紧急联系人/家属姓名")
	@Excel(name = "紧急联系人/家属姓名")
	private String contactName;
	/**
	 * 紧急联系人电话
	 */
	@Schema(description = "紧急联系人电话")
	@Excel(name = "紧急联系人电话")
	private String contactPhone;
	/**
	 * 备用电话
	 */
	@Schema(description = "备用电话")
	@Excel(name = "备用电话")
	private String contactPhoneBackup;

	/**
	 * 入住日期
	 */
	@Schema(description = "入住日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "入住日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date checkinDate;
	/**
	 * 特殊需求
	 */
	@Schema(description = "特殊需求")
	@Excel(name = "特殊需求")
	private String specialNeeds;

	/**
	 * 预计退住日期
	 */
	@Schema(description = "预计退住日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "预计退住日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date checkoutDate;
	/**
	 * 实际天数
	 */
	@Schema(description = "实际天数")
	@Excel(name = "实际天数")
	private Integer actualDays;
	/**
	 * 同住长者人数（已废弃，一人一单恒为1）
	 */
	@Schema(description = "已废弃：一人一单恒为1")
	@Excel(name = "已废弃：同住人数")
	private Integer residentCount;
	/**
	 * 楼栋ID（冗余主床位所在楼栋）
	 */
	@Schema(description = "楼栋ID")
	@Excel(name = "楼栋ID")
	private Long buildingId;
	/**
	 * 房间ID（冗余主床位所在房间）
	 */
	@Schema(description = "房间ID")
	@Excel(name = "房间ID")
	private Long roomId;
	/**
	 * 主床位ID（关联 e_care_bed.id）
	 */
	@Schema(description = "主床位ID")
	@Excel(name = "主床位ID")
	private Long bedId;
	/**
	 * 实际退住日期
	 */
	@Schema(description = "实际退住日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "实际退住日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date actualCheckoutDate;
	/**
	 * 套餐ID
	 */
	@Schema(description = "套餐ID")
	@Excel(name = "套餐ID")
	private Long comboId;
	/**
	 * 入住状态（0-登记中 1-待入住 2-康养中 3-已退住 9-已拒收）
	 */
	@Schema(description = "入住状态（0-登记中 1-待入住 2-康养中 3-已退住 9-已拒收）")
	@Excel(name = "入住状态", readConverterExp = "0=登记中,1=待入住,2=康养中,3=已退住,9=已拒收")
	private Integer checkinStatus;
	/**
	 * 退住交接状态（0-未开始 1-交接中 2-已完成）
	 */
	@Schema(description = "退住交接状态（0-未开始 1-交接中 2-已完成）")
	@Excel(name = "退住交接状态", readConverterExp = "0=未开始,1=交接中,2=已完成")
	private Integer checkoutStatus;
	/**
	 * 退住交接单号
	 */
	@Schema(description = "退住交接单号")
	@Excel(name = "退住交接单号")
	private String checkoutNo;
	/**
	 * 押金合计
	 */
	@Schema(description = "押金合计")
	@Excel(name = "押金合计")
	private BigDecimal totalDeposit;
	@Schema(description = "押金状态")
	@Excel(name = "押金状态")
	private Integer totalDepositPayState;
	@Schema(description = "已支付押金金额")
	@Excel(name = "已支付押金金额")
	private BigDecimal totalDepositPayMoney;
	@Schema(description = "押金支付方式")
	@Excel(name = "押金支付方式")
	private String totalDepositPaymentMethod;

	@Schema(description = "押金退款时间")
	@Excel(name = "押金退款时间")
	private Date totalDepositRefundTime;
	/**
	 * 是否已结算（0-否 1-待结算 2-已结算）
	 */
	@Schema(description = "是否已结算（0-否 1-待结算 2-已结算）")
	@Excel(name = "是否已结算（0-否 1-待结算 2-已结算）")
	private Integer settled;
	@Schema(description = "结算优惠")
	@Excel(name = "结算优惠")
	private BigDecimal settledDiscount;

	@Schema(description = "应收/应退金额")
	@Excel(name = "应收/应退金额")
	private BigDecimal settledFinalAmount;
	@Schema(description = "实际支付金额")
	@Excel(name = "实际支付金额")
	private BigDecimal settledActualPayment;
	
	@Schema(description = "结算支付方式")
	@Excel(name = "结算支付方式")
	private String settledPaymentMethod;
	
	/**
	 * 负责员工ID
	 */
	@Schema(description = "负责员工ID")
	@Excel(name = "负责员工ID")
	private Long handlerId;

	/**
	 * 结算单ID
	 */
	@Schema(description = "结算单ID")
	@Excel(name = "结算单ID")
	private Long settlementId;
	/**
	 * 登记人员
	 */
	@Schema(description = "登记人员")
	@Excel(name = "登记人员")
	private String operatorName;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remarks;

	// ==================== 以下字段由原入住长者明细表（e_care_checkin_resident）合并而来 ====================

	/**
	 * 性别：0-未知 1-男 2-女
	 */
	@Schema(description = "性别：0-未知 1-男 2-女")
	@Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
	private Integer gender;
	/**
	 * 出生日期
	 */
	@Schema(description = "出生日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date birthDate;
	/**
	 * 身份证号
	 */
	@Schema(description = "身份证号")
	@Excel(name = "身份证号")
	private String idCard;
	/**
	 * 长者联系电话
	 */
	@Schema(description = "长者联系电话")
	@Excel(name = "长者联系电话")
	private String phone;
	/**
	 * 护理等级（自理/半自理/失能）
	 */
	@Schema(description = "护理等级")
	@Excel(name = "护理等级")
	private String careLevel;
	/**
	 * 精神状态（GOOD-良好 NORMAL-一般 POOR-差）
	 */
	@Schema(description = "精神状态")
	@Excel(name = "精神状态")
	private String mentalState;
	/**
	 * 意识状态（CLEAR-清醒 DROWSY-嗜睡 CONFUSED-意识模糊）
	 */
	@Schema(description = "意识状态")
	@Excel(name = "意识状态")
	private String consciousness;
	/**
	 * 体温(℃)
	 */
	@Schema(description = "体温(℃)")
	@Excel(name = "体温")
	private BigDecimal temperature;
	/**
	 * 脉搏(次/分)
	 */
	@Schema(description = "脉搏(次/分)")
	@Excel(name = "脉搏")
	private Integer pulse;
	/**
	 * 收缩压(mmHg)
	 */
	@Schema(description = "收缩压(mmHg)")
	@Excel(name = "收缩压")
	private Integer bloodPressureHigh;
	/**
	 * 舒张压(mmHg)
	 */
	@Schema(description = "舒张压(mmHg)")
	@Excel(name = "舒张压")
	private Integer bloodPressureLow;
	/**
	 * 血氧饱和度(%)
	 */
	@Schema(description = "血氧饱和度(%)")
	@Excel(name = "血氧饱和度")
	private Integer bloodOxygen;
	/**
	 * 视力（NORMAL-正常 DECLINED-下降 IMPAIRED-障碍）
	 */
	@Schema(description = "视力")
	@Excel(name = "视力")
	private String vision;
	/**
	 * 听力（NORMAL-正常 DECLINED-下降 IMPAIRED-障碍）
	 */
	@Schema(description = "听力")
	@Excel(name = "听力")
	private String hearing;
	/**
	 * 认知筛查（NORMAL-正常 SUSPECTED-疑似失智 CONFIRMED-确诊失智）
	 */
	@Schema(description = "认知筛查")
	@Excel(name = "认知筛查")
	private String cognition;
	/**
	 * 跌倒风险（LOW-低 MEDIUM-中 HIGH-高）
	 */
	@Schema(description = "跌倒风险")
	@Excel(name = "跌倒风险")
	private String fallRisk;
	/**
	 * 压疮（NONE-无 I_II-I~II度 III_IV-III~IV度）
	 */
	@Schema(description = "压疮")
	@Excel(name = "压疮")
	private String pressureSore;
	/**
	 * 自理能力（FULL-能 PARTIAL-部分 NONE-完全不能）
	 */
	@Schema(description = "自理能力")
	@Excel(name = "自理能力")
	private String selfCareAbility;
	/**
	 * 慢性疾病
	 */
	@Schema(description = "慢性疾病")
	@Excel(name = "慢性疾病")
	private String chronicDiseases;
	/**
	 * 在服药物
	 */
	@Schema(description = "在服药物")
	@Excel(name = "在服药物")
	private String medications;
	/**
	 * 过敏史（药物/食物）
	 */
	@Schema(description = "过敏史")
	@Excel(name = "过敏史")
	private String allergies;
	/**
	 * 膳食类型（普食/软食/流食/鼻饲/糖尿病膳/低盐低脂）
	 */
	@Schema(description = "膳食类型")
	@Excel(name = "膳食类型")
	private String dietType;
	/**
	 * 大小便情况（失禁/便秘/留置导尿等）
	 */
	@Schema(description = "大小便情况")
	@Excel(name = "大小便情况")
	private String elimination;
	/**
	 * 入住近照（逗号分隔）
	 */
	@Schema(description = "入住近照")
	@Excel(name = "入住近照")
	private String checkinPhotos;
	/**
	 * 自带个人物品（逗号分隔）
	 */
	@Schema(description = "自带个人物品")
	@Excel(name = "自带个人物品")
	private String broughtItems;
	/**
	 * 物品照片（逗号分隔）
	 */
	@Schema(description = "物品照片")
	@Excel(name = "物品照片")
	private String broughtPhotos;
	/**
	 * 房间名称（快照）
	 */
	@Schema(description = "房间名称")
	@Excel(name = "房间名称")
	private String roomName;
	/**
	 * 床位编号（快照）
	 */
	@Schema(description = "床位编号")
	@Excel(name = "床位编号")
	private String bedNo;
	/**
	 * 套餐计费周期快照（0-按年 1-按月 2-按天）
	 */
	@Schema(description = "套餐计费周期（0-按年 1-按月 2-按天）")
	@Excel(name = "计费周期", readConverterExp = "0=按年,1=按月,2=按天")
	private Integer chargeCycle;
	/**
	 * 计划时长数量（配合 chargeCycle 表示「多少年/多少月/多少天」）
	 */
	@Schema(description = "计划时长数量（配合计费周期表示多少年/月/天）")
	@Excel(name = "计划时长数量")
	private Integer chargeNum;

	/**
	 * 周期单价快照（chargeCycle 对应原价）
	 */
	@Schema(description = "周期单价快照")
	@Excel(name = "周期单价")
	private BigDecimal periodPrice;
	/**
	 * 套餐年价快照（元/年，用于按实际时长逐段计费）
	 */
	@Schema(description = "套餐年价快照（元/年）")
	@Excel(name = "套餐年价")
	private BigDecimal comboYearPrice;
	/**
	 * 套餐月价快照（元/月，用于按实际时长逐段计费）
	 */
	@Schema(description = "套餐月价快照（元/月）")
	@Excel(name = "套餐月价")
	private BigDecimal comboMonthPrice;
	/**
	 * 套餐日价快照（元/天，用于按实际时长逐段计费）
	 */
	@Schema(description = "套餐日价快照（元/天）")
	@Excel(name = "套餐日价")
	private BigDecimal comboDayPrice;
	/**
	 * 本单押金
	 */
	@Schema(description = "本单押金")
	@Excel(name = "本单押金")
	private BigDecimal deposit;

	// -------------------- 退住交接字段 --------------------
	/**
	 * 退住时长者近照（逗号分隔）
	 */
	@Schema(description = "退住时长者近照")
	@Excel(name = "退住时长者近照")
	private String checkoutPhotos;
	/**
	 * 身体状况变化说明
	 */
	@Schema(description = "身体状况变化说明")
	@Excel(name = "身体状况变化说明")
	private String appearanceChanges;
	/**
	 * 身体状况变化结果（0-正常 1-有变化）
	 */
	@Schema(description = "身体状况变化结果（0-正常 1-有变化）")
	@Excel(name = "身体状况变化结果")
	private Integer healthChangeDesc;
	/**
	 * 个人卫生状态（0-整洁 1-一般 2-需清洁）
	 */
	@Schema(description = "个人卫生状态（0-整洁 1-一般 2-需清洁）")
	@Excel(name = "个人卫生状态")
	private Integer careSummary;
	/**
	 * 个人物品状态（0-完好 1-有损耗 2-有损坏）
	 */
	@Schema(description = "个人物品状态（0-完好 1-有损耗 2-有损坏）")
	@Excel(name = "个人物品状态")
	private Integer itemsStatus;
	/**
	 * 物品交接确认（0-未确认 1-已确认）
	 */
	@Schema(description = "物品交接确认（0-未确认 1-已确认）")
	@Excel(name = "物品交接确认")
	private Integer itemsConfirmed;
	/**
	 * 物品备注
	 */
	@Schema(description = "物品备注")
	@Excel(name = "物品备注")
	private String itemsRemark;
	/**
	 * 归还/转交家属物品
	 */
	@Schema(description = "归还/转交家属物品")
	@Excel(name = "归还/转交家属物品")
	private String returnedItems;
	/**
	 * 长者/家属反馈
	 */
	@Schema(description = "长者/家属反馈")
	@Excel(name = "长者/家属反馈")
	private String customerFeedback;
	/**
	 * 满意度评分（1-5）
	 */
	@Schema(description = "满意度评分（1-5）")
	@Excel(name = "满意度评分")
	private Integer customerSatisfaction;
	/**
	 * 后续照料建议
	 */
	@Schema(description = "后续照料建议")
	@Excel(name = "后续照料建议")
	private String suggestions;
	/**
	 * 健康提醒事项
	 */
	@Schema(description = "健康提醒事项")
	@Excel(name = "健康提醒事项")
	private String healthReminder;
	/**
	 * 在住适应情况
	 */
	@Schema(description = "在住适应情况")
	@Excel(name = "在住适应情况")
	private String performance;


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