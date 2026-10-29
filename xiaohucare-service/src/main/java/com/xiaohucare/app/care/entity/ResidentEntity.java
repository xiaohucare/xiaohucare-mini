package com.xiaohucare.app.care.entity;

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
 * 长者档案表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_resident")
public class ResidentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
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
	private String name;
	/**
	 * 性别：男/女
	 */
	@Schema(description = "性别：男/女")
	@Excel(name = "性别")
	private String gender;
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
	 * 联系电话
	 */
	@Schema(description = "联系电话")
	@Excel(name = "联系电话")
	private String phone;
	/**
	 * 家庭住址
	 */
	@Schema(description = "家庭住址")
	@Excel(name = "家庭住址")
	private String address;
	/**
	 * 紧急联系人
	 */
	@Schema(description = "紧急联系人")
	@Excel(name = "紧急联系人")
	private String emergencyContact;
	/**
	 * 紧急联系人电话
	 */
	@Schema(description = "紧急联系人电话")
	@Excel(name = "紧急联系人电话")
	private String emergencyPhone;
	/**
	 * 入住日期
	 */
	@Schema(description = "入住日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "入住日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date admissionDate;
	/**
	 * 入住时间（精确到时分秒）
	 */
	@Schema(description = "入住时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "入住时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date checkInTime;
	/**
	 * 离店时间（为空表示仍在住）
	 */
	@Schema(description = "离店时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "离店时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date checkOutTime;
	/**
	 * 当前房间ID
	 */
	@Schema(description = "当前房间ID")
	@Excel(name = "当前房间ID")
	private Long roomId;
	/**
	 * 当前床位ID
	 */
	@Schema(description = "当前床位ID")
	@Excel(name = "当前床位ID")
	private Long bedId;
	/**
	 * 入住状态：0未入住 1在住 2已退住
	 */
	@Schema(description = "入住状态：0未入住 1在住 2已退住")
	@Excel(name = "入住状态", readConverterExp = "0=未入住,1=在住,2=已退住")
	private Integer checkInStatus;
	/**
	 * 入住房间号
	 */
	@Schema(description = "入住房间号")
	@Excel(name = "入住房间号")
	private String roomNo;
	/**
	 * 入住房铺号
	 */
	@Schema(description = "入住房铺号")
	@Excel(name = "床铺号")
	private String bedNo;
	/**
	 * 照护等级：1自理，2半自理，3失能
	 */
	@Schema(description = "照护等级：1自理，2半自理，3失能")
	@Excel(name = "照护等级", readConverterExp = "1=自理,2=半自理,3=失能")
	private Integer careLevel;
	/**
	 * 健康状况
	 */
	@Schema(description = "健康状况")
	@Excel(name = "健康状况")
	private String healthStatus;
	/**
	 * 兴趣爱好
	 */
	@Schema(description = "兴趣爱好")
	@Excel(name = "兴趣爱好")
	private String hobbies;

	// ==================== 个人扩展信息 ====================
	/**
	 * 民族
	 */
	@Schema(description = "民族")
	@Excel(name = "民族")
	private String nation;
	/**
	 * 籍贯
	 */
	@Schema(description = "籍贯")
	@Excel(name = "籍贯")
	private String nativePlace;
	/**
	 * 婚姻状况：1未婚 2已婚 3丧偶 4离异 5再婚
	 */
	@Schema(description = "婚姻状况：1未婚 2已婚 3丧偶 4离异 5再婚")
	@Excel(name = "婚姻状况", readConverterExp = "1=未婚,2=已婚,3=丧偶,4=离异,5=再婚")
	private Integer maritalStatus;
	/**
	 * 文化程度：1文盲 2小学 3初中 4高中/中专 5大专 6本科及以上
	 */
	@Schema(description = "文化程度：1文盲 2小学 3初中 4高中/中专 5大专 6本科及以上")
	@Excel(name = "文化程度", readConverterExp = "1=文盲,2=小学,3=初中,4=高中/中专,5=大专,6=本科及以上")
	private Integer education;
	/**
	 * 政治面貌
	 */
	@Schema(description = "政治面貌")
	@Excel(name = "政治面貌")
	private String politicalStatus;
	/**
	 * 宗教信仰
	 */
	@Schema(description = "宗教信仰")
	@Excel(name = "宗教信仰")
	private String religion;
	/**
	 * 血型
	 */
	@Schema(description = "血型")
	@Excel(name = "血型")
	private String bloodType;
	/**
	 * 长者照片URL
	 */
	@Schema(description = "长者照片URL")
	private String avatar;

	// ==================== 健康与能力评估 ====================
	/**
	 * 身高(cm)
	 */
	@Schema(description = "身高(cm)")
	@Excel(name = "身高(cm)")
	private Integer height;
	/**
	 * 体重(kg)
	 */
	@Schema(description = "体重(kg)")
	@Excel(name = "体重(kg)")
	private Integer weight;
	/**
	 * 自理能力：1完全自理 2部分自理 3不能自理
	 */
	@Schema(description = "自理能力：1完全自理 2部分自理 3不能自理")
	@Excel(name = "自理能力", readConverterExp = "1=完全自理,2=部分自理,3=不能自理")
	private Integer selfCareAbility;
	/**
	 * 认知状况：1正常 2疑似障碍 3确诊障碍
	 */
	@Schema(description = "认知状况：1正常 2疑似障碍 3确诊障碍")
	@Excel(name = "认知状况", readConverterExp = "1=正常,2=疑似障碍,3=确诊障碍")
	private Integer cognitiveStatus;
	/**
	 * ADL日常生活能力评分（Barthel指数 0-100）
	 */
	@Schema(description = "ADL日常生活能力评分")
	@Excel(name = "ADL评分")
	private Integer adlScore;
	/**
	 * 跌倒风险：1低 2中 3高
	 */
	@Schema(description = "跌倒风险：1低 2中 3高")
	@Excel(name = "跌倒风险", readConverterExp = "1=低,2=中,3=高")
	private Integer fallRisk;
	/**
	 * 慢性病
	 */
	@Schema(description = "慢性病")
	@Excel(name = "慢性病")
	private String chronicDisease;
	/**
	 * 既往病史
	 */
	@Schema(description = "既往病史")
	@Excel(name = "既往病史")
	private String medicalHistory;
	/**
	 * 家族病史
	 */
	@Schema(description = "家族病史")
	@Excel(name = "家族病史")
	private String familyHistory;
	/**
	 * 过敏史
	 */
	@Schema(description = "过敏史")
	@Excel(name = "过敏史")
	private String allergyHistory;
	/**
	 * 用药情况
	 */
	@Schema(description = "用药情况")
	@Excel(name = "用药情况")
	private String medication;

	// ==================== 入住与护理 ====================
	/**
	 * 入住性质：1长期入住 2短期托养 3日间照料 4试住
	 */
	@Schema(description = "入住性质：1长期入住 2短期托养 3日间照料 4试住")
	@Excel(name = "入住性质", readConverterExp = "1=长期入住,2=短期托养,3=日间照料,4=试住")
	private Integer checkInType;
	/**
	 * 入住原因
	 */
	@Schema(description = "入住原因")
	@Excel(name = "入住原因")
	private String checkInReason;
	/**
	 * 护理等级
	 */
	@Schema(description = "护理等级")
	@Excel(name = "护理等级")
	private String nursingLevel;
	/**
	 * 膳食类型（普食/软食/半流质/流质/鼻饲）
	 */
	@Schema(description = "膳食类型")
	@Excel(name = "膳食类型")
	private String dietType;
	/**
	 * 特殊饮食说明（低盐/低糖/高蛋白等）
	 */
	@Schema(description = "特殊饮食说明")
	@Excel(name = "特殊饮食")
	private String specialDiet;

	// ==================== 医保与经济 ====================
	/**
	 * 医保类型：1城镇职工 2城乡居民 3新农合 4自费
	 */
	@Schema(description = "医保类型：1城镇职工 2城乡居民 3新农合 4自费")
	@Excel(name = "医保类型", readConverterExp = "1=城镇职工,2=城乡居民,3=新农合,4=自费")
	private Integer medicalInsuranceType;
	/**
	 * 医保卡号
	 */
	@Schema(description = "医保卡号")
	@Excel(name = "医保卡号")
	private String medicalInsuranceNo;
	/**
	 * 社保卡号
	 */
	@Schema(description = "社保卡号")
	@Excel(name = "社保卡号")
	private String socialSecurityNo;
	/**
	 * 经济来源
	 */
	@Schema(description = "经济来源")
	@Excel(name = "经济来源")
	private String incomeSource;
	/**
	 * 担保人
	 */
	@Schema(description = "担保人")
	@Excel(name = "担保人")
	private String guarantor;
	/**
	 * 担保人电话
	 */
	@Schema(description = "担保人电话")
	@Excel(name = "担保人电话")
	private String guarantorPhone;

	// ==================== 离店 ====================
	/**
	 * 离店原因
	 */
	@Schema(description = "离店原因")
	@Excel(name = "离店原因")
	private String checkOutReason;
	/**
	 * 结算状态：0未结算 1已结算
	 */
	@Schema(description = "结算状态：0未结算 1已结算")
	@Excel(name = "结算状态", readConverterExp = "0=未结算,1=已结算")
	private Integer settlementStatus;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remarks;
	/**
	 * 状态（0-停用 1-正常）
	 */
	@Schema(description = "状态（0-停用 1-正常）")
	@Excel(name = "状态")
	private Integer status;
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

}
