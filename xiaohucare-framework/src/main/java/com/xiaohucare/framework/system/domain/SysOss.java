package com.xiaohucare.framework.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oss管理
 * 
 * @author xiaohucare
 * @date 2026-05-18 12:35:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss")
public class SysOss extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@TableId
	@Excel(name = "主键")
	private Long id;
	/**
	 * 模型名称
	 */
	@Excel(name = "模型名称")
	private String ossId;
	/**
	 * 模型描述
	 */
	@Excel(name = "模型描述")
	private String url;
	/**
	 * 请求地址
	 */
	@Excel(name = "请求地址")
	private String name;
	/**
	 * 文本块大小
	 */
	@Excel(name = "文本块大小")
	private Long fileSize;
	/**
	 * 创建者
	 */
	@Excel(name = "创建者")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;
	/**
	 * 备注
	 */
	@Excel(name = "备注")
	private String remark;
	
	private String prefix;
	private String ossType;

}