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
 * 员工信息表
 * 
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_employee")
public class EmployeeEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 员工ID
	 */
	@TableId
	@Schema(description = "员工ID")
	@Excel(name = "员工ID")
	private Long id;
	/**
	 * 员工编号
	 */
	@Schema(description = "员工编号")
	@Excel(name = "员工编号")
	private String employeeNo;
	/**
	 * 姓名
	 */
	@Schema(description = "姓名")
	@Excel(name = "姓名")
	private String name;
	/**
	 * 性别：0-未知，1-男，2-女
	 */
	@Schema(description = "性别：0-未知，1-男，2-女")
	@Excel(name = "性别：0-未知，1-男，2-女")
	private Integer gender;
	/**
	 * 身份证号
	 */
	@Schema(description = "身份证号")
	@Excel(name = "身份证号")
	private String idCard;
	/**
	 * 出生日期
	 */
	@Schema(description = "出生日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date birthday;
	/**
	 * 联系电话
	 */
	@Schema(description = "联系电话")
	@Excel(name = "联系电话")
	private String phone;
	/**
	 * 紧急联系人
	 */
	@Schema(description = "紧急联系人")
	@Excel(name = "紧急联系人")
	private String emergencyContact;
	/**
	 * 紧急联系电话
	 */
	@Schema(description = "紧急联系电话")
	@Excel(name = "紧急联系电话")
	private String emergencyPhone;
	/**
	 * 头像URL
	 */
	@Schema(description = "头像URL")
	@Excel(name = "头像URL")
	private String avatar;
	/**
	 * 等级：1-初级，2-中级，3-高级，4-特级
	 */
	@Schema(description = "等级：1-初级，2-中级，3-高级，4-特级")
	@Excel(name = "等级：1-初级，2-中级，3-高级，4-特级")
	private Integer level;
	/**
	 * 职位：美容师、康养师、店长等
	 */
	@Schema(description = "职位：美容师、康养师、店长等")
	@Excel(name = "职位：美容师、康养师、店长等")
	private String position;
	/**
	 * 专长技能列表（JSON数组）
	 */
	@Schema(description = "专长技能列表（JSON数组）")
	@Excel(name = "专长技能列表（JSON数组）")
	private String skills;
	/**
	 * 资质证书（JSON数组）
	 */
	@Schema(description = "资质证书（JSON数组）")
	@Excel(name = "资质证书（JSON数组）")
	private String certification;
	/**
	 * 入职日期
	 */
	@Schema(description = "入职日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date hireDate;
	private String hireFile;
	private String hireReferee;
	private String hireRefereeReason;
	/**
	 * 离职日期
	 */
	@Schema(description = "离职日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "离职日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date leaveDate;
	private String leaveFile;
	private String leaveReason;
	/**
	 * 员工状态：0-待入职，1-在职，2-试用期，3-实习，10 已离职
	 */
	@Schema(description = "员工状态：0-待入职，1-在职，2-试用期，3-实习，10 已离职")
	@Excel(name = "员工状态：0-待入职，1-在职，2-试用期，3-实习，10 已离职")
	private Integer employeeStatus;
	/**
	 * 是否康养服务：0-否，1-是
	 */
	@Schema(description = "是否康养服务：0-否，1-是")
	@Excel(name = "是否康养服务：0-否，1-是")
	private Integer isCare;
	/**
	 * 是否康复服务：0-否，1-是
	 */
	@Schema(description = "是否康复服务：0-否，1-是")
	@Excel(name = "是否康复服务：0-否，1-是")
	private Integer isRehab;
	/**
	 * 是否商品销售：0-否，1-是
	 */
	@Schema(description = "是否商品销售：0-否，1-是")
	@Excel(name = "是否商品销售：0-否，1-是")
	private Integer isSales;
	/**
	 * 每日最大预约数
	 */
	@Schema(description = "每日最大预约数")
	@Excel(name = "每日最大预约数")
	private Integer maxDaily;
	/**
	 * 是否暂停洗护：0-否，1-是
	 */
	@Schema(description = "是否暂停洗护：0-否，1-是")
	private Integer washState;
	/**
	 * 启用状态：0-禁用，1-启用
	 */
	@Schema(description = "启用状态：0-禁用，1-启用")
	@Excel(name = "启用状态：0-禁用，1-启用")
	private Integer enabled;
	/**
	 * 关联系统用户ID（用于登录）
	 */
	@Schema(description = "关联系统用户ID（用于登录）")
	@Excel(name = "关联系统用户ID（用于登录）")
	private Long userId;
	/**
	 * 已分配角色ID（逗号分隔，开通账号/分配角色时由 sys_user_role 回写的快照）
	 */
	@Schema(description = "已分配角色ID（逗号分隔）")
	private String roleIds;
	/**
	 * 开户银行
	 */
	@Schema(description = "开户银行")
	@Excel(name = "开户银行")
	private String bankName;
	/**
	 * 银行账号（用于发放工资）
	 */
	@Schema(description = "银行账号（用于发放工资）")
	@Excel(name = "银行账号（用于发放工资）")
	private String bankAccount;
	/**
	 * 家庭住址
	 */
	@Schema(description = "家庭住址")
	@Excel(name = "家庭住址")
	private String address;
	/**
	 * 微信号
	 */
	@Schema(description = "微信号")
	@Excel(name = "微信号")
	private String wechat;
	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	@Excel(name = "邮箱")
	private String email;
	/**
	 * 基本工资
	 */
	@Schema(description = "基本工资")
	@Excel(name = "基本工资")
	private BigDecimal baseSalary;
	/**
	 * 默认提成比例（%）
	 */
	@Schema(description = "默认提成比例（%）")
	@Excel(name = "默认提成比例（%）")
	private BigDecimal commissionRate;
	/**
	 * 累计完成单数
	 */
	@Schema(description = "累计完成单数")
	@Excel(name = "累计完成单数")
	private Integer totalOrders;
	/**
	 * 累计完成金额
	 */
	@Schema(description = "累计完成金额")
	@Excel(name = "累计完成金额")
	private BigDecimal totalAmount;
	/**
	 * 平均评分（5分制）
	 */
	@Schema(description = "平均评分（5分制）")
	@Excel(name = "平均评分（5分制）")
	private BigDecimal rating;
	/**
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新者
	 */
	@Schema(description = "更新者")
	@Excel(name = "更新者")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;

}