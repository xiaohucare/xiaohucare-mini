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
 * 长者家属表
 * 
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_resident_family_member")
public class ResidentFamilyMemberEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 长者ID（e_resident.id）
	 */
	@Schema(description = "长者ID")
	@Excel(name = "长者ID")
	private Long residentId;
	/**
	 * 关联会员ID（小程序会员/用户，便于家属端小程序按 memberId 反查关联长者）
	 */
	@Schema(description = "关联会员ID（小程序会员）")
	@Excel(name = "关联会员ID")
	private Long memberId;
	/**
	 * 家属姓名
	 */
	@Schema(description = "家属姓名")
	@Excel(name = "家属姓名")
	private String name;
	/**
	 * 与长者关系（子女/配偶/亲属等）
	 */
	@Schema(description = "与长者关系")
	@Excel(name = "与长者关系")
	private String relationship;
	/**
	 * 联系电话
	 */
	@Schema(description = "联系电话")
	@Excel(name = "联系电话")
	private String phone;
	/**
	 * 住址
	 */
	@Schema(description = "住址")
	@Excel(name = "住址")
	private String address;
	/**
	 * 是否紧急联系人：0-否 1-是
	 */
	@Schema(description = "是否紧急联系人：0-否 1-是")
	@Excel(name = "是否紧急联系人")
	private Integer isEmergencyContact;
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
