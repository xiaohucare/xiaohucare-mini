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
 * 来访者表
 *
 * @author xiaohucare
 * @date 2026-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_visitor")
public class VisitorEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Integer id;
	/**
	 * 来访者姓名
	 */
	@Schema(description = "来访者姓名")
	@Excel(name = "来访者姓名")
	private String name;
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
	 * 与老人关系
	 */
	@Schema(description = "与老人关系")
	@Excel(name = "与老人关系")
	private String relationship;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date updateAt;

}
