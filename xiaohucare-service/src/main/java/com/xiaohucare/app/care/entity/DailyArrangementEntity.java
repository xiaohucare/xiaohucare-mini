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
 * 宠物酒店日常安排表
 * 
 * @author xiaohucare
 * @date 2026-04-07 08:33:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_daily_arrangement")
public class DailyArrangementEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@TableId
	@Schema(description = "主键ID")
	@Excel(name = "主键ID")
	private Long id;
	/**
	 * 安排类型:0 常规安排 1 特殊时期安排
	 */
	@Schema(description = "安排类型:0 常规安排 1 特殊时期安排")
	@Excel(name = "安排类型:0 常规安排 1 特殊时期安排")
	private Integer kind;
	/**
	 * 安排类型:1~7 周一到星期日 0 特殊时期安排
	 */
	@Schema(description = "安排类型:1~7 周一到星期日 0 特殊时期安排")
	@Excel(name = "安排类型:1~7 周一到星期日 0 特殊时期安排")
	private Integer week;
	/**
	 * 安排日期
	 */
	@Schema(description = "安排日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "安排日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date arrangementDate;
	/**
	 * 安排时间
	 */
	@Schema(description = "安排时间")
	@Excel(name = "安排时间")
	private String arrangementTime;
	/**
	 * 安排内容
	 */
	@Schema(description = "安排内容")
	@Excel(name = "安排内容")
	private String content;
	/**
	 * 安排备注
	 */
	@Schema(description = "安排备注")
	@Excel(name = "安排备注")
	private String remark;
	/**
	 * 安排状态:0 未执行 1 已执行
	 */
	@Schema(description = "安排状态:0 未执行 1 已执行")
	@Excel(name = "安排状态:0 未执行 1 已执行")
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