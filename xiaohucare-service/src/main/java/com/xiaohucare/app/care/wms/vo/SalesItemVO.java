
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 出货明细表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SalesItemVO对象")
public class SalesItemVO extends SalesItemEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * SKU编码
	 */
	@Schema(description = "SKU编码")
	private String skuCode;
	/**
	 * SKU名称（如：护理垫-品牌A-100片）
	 */
	@Schema(description = "SKU名称（如：护理垫-品牌A-100片）")
	private String skuName;
}
