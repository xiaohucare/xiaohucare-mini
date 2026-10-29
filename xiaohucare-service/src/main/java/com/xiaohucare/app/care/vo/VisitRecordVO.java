
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.VisitRecordEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.TableField;

 /**
 * 来访/外出记录表 视图实体类
 * @author xiaohucare
 * @date 2025-11-23 22:33:31
 */

@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "来访/外出记录表")
public class VisitRecordVO extends VisitRecordEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 外出状态（视图派生字段，非数据库列）：1 外出中（实际返回为空），0 已返回
	 */
	@Schema(description = "外出状态：1 外出中，0 已返回")
	@TableField(exist = false)
	private Integer status;
}
