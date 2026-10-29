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
 * 来访/外出记录表
 *
 * visit_type=0 来访：visit_date/visit_time/leave_time/purpose + 来访者信息(visitor_*)
 * visit_type=1 外出：visit_date/visit_time 为外出日期/时间，expected_return_time/actual_return_time 为预计/实际返回，reason/companion/approver
 *
 * @author xiaohucare
 * @date 2025-11-23 22:33:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_visit_record")
public class VisitRecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Integer id;
	/**
	 * 来访者ID
	 */
	@Schema(description = "来访者ID")
	@Excel(name = "来访者ID")
	private Integer visitorId;
	/**
	 * 长者ID
	 */
	@Schema(description = "长者ID")
	@Excel(name = "长者ID")
	private Integer residentId;
	/**
	 * 长者姓名
	 */
	@Schema(description = "长者姓名")
	@Excel(name = "长者姓名")
	private String residentName;
	/**
	 * 来访/外出日期
	 */
	@Schema(description = "来访/外出日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date visitDate;
	/**
	 * 来访/外出时间（HH:mm:ss）
	 */
	@Schema(description = "来访/外出时间")
	@Excel(name = "时间")
	private String visitTime;
	/**
	 * 离开时间（HH:mm:ss，来访记录用）
	 */
	@Schema(description = "离开时间")
	@Excel(name = "离开时间")
	private String leaveTime;
	/**
	 * 来访目的
	 */
	@Schema(description = "来访目的")
	@Excel(name = "来访目的")
	private String purpose;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remarks;
	/**
	 * 来访者联系电话
	 */
	@Schema(description = "联系电话")
	@Excel(name = "联系电话")
	private String phone;
	/**
	 * 来访者身份证号
	 */
	@Schema(description = "身份证号")
	@Excel(name = "身份证号")
	private String idCard;
	/**
	 * 与长者关系
	 */
	@Schema(description = "与长者关系")
	@Excel(name = "与长者关系")
	private String relationship;
	/**
	 * 类型：0 来访 1 外出
	 */
	@Schema(description = "类型：0 来访 1 外出")
	@Excel(name = "类型", readConverterExp = "0=来访,1=外出")
	private Integer visitType;
	/**
	 * 预计返回时间（yyyy-MM-dd HH:mm:ss，外出记录用）
	 */
	@Schema(description = "预计返回时间")
	@Excel(name = "预计返回时间")
	private String expectedReturnTime;
	/**
	 * 实际返回时间（yyyy-MM-dd HH:mm:ss，外出记录用）
	 */
	@Schema(description = "实际返回时间")
	@Excel(name = "实际返回时间")
	private String actualReturnTime;
	/**
	 * 外出事由（外出记录用）
	 */
	@Schema(description = "外出事由")
	@Excel(name = "外出事由")
	private String reason;
	/**
	 * 陪同人（外出记录用）
	 */
	@Schema(description = "陪同人")
	@Excel(name = "陪同人")
	private String companion;
	/**
	 * 批准人（外出记录用）
	 */
	@Schema(description = "批准人")
	@Excel(name = "批准人")
	private String approver;
	/**
	 * 陪同人电话（外出记录用）
	 */
	@Schema(description = "陪同人电话")
	@Excel(name = "陪同人电话")
	private String companionPhone;
	/**
	 * 来访者姓名
	 */
	@Schema(description = "来访者姓名")
	@Excel(name = "来访者姓名")
	private String visitorName;
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
