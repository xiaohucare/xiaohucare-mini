
package com.xiaohucare.app.care.vo;

import com.xiaohucare.app.care.entity.StoreMessageTipsEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺通知提示表 视图实体类
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "StoreMessageTipsVO对象")
public class StoreMessageTipsVO extends StoreMessageTipsEntity {
	private static final long serialVersionUID = 1L;
}
