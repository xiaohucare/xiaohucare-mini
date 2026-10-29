
package com.xiaohucare.app.care.vo;

import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入住登记费用项表 视图实体类
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CheckinFeeItemVO对象")
public class CheckinFeeItemVO extends CheckinFeeItemEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 折算后金额（按天/按年/按月时 = 单价折算日价 × 数量 × 实际住养天数，仅用于接口回显）
	 */
	@Schema(description = "折算后金额")
	private java.math.BigDecimal amount;
}
