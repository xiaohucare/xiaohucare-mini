
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.SettlementEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 结算表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SettlementVO对象")
public class SettlementVO extends SettlementEntity {
	private static final long serialVersionUID = 1L;
}
