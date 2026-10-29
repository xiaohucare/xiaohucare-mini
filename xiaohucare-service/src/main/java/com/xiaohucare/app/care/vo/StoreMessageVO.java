
package com.xiaohucare.app.care.vo;

import com.xiaohucare.app.care.entity.StoreMessageEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺通知表(门店消息) 视图实体类
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "StoreMessageVO对象")
public class StoreMessageVO extends StoreMessageEntity {
	private static final long serialVersionUID = 1L;
}
