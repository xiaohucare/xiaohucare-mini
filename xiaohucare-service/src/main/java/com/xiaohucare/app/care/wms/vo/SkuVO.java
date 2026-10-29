
package com.xiaohucare.app.care.wms.vo;
import java.math.BigDecimal;

import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.framework.annotation.Excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 商品SKU表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SkuVO对象")
public class SkuVO extends SkuEntity {
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "商品名称")
	@Excel(name = "商品名称")
	private String productName;
	
	@Schema(description = "单位")
	@Excel(name = "单位")
	private String unit;
	
	@Schema(description = "规格显示文本")
	@Excel(name = "规格")
	private String specText;
	@Schema(description = "市场价")
			@Excel(name = "市场价")
		private BigDecimal marketPrice;
}
