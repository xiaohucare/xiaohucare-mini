package com.xiaohucare.framework.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * 
 * @author xiaohucare
 * @date 2026-03-14 21:00:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_region")
public class SysRegion extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer parentId;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer type;
	/**
	 * 
	 */
	private Integer agencyId;

}