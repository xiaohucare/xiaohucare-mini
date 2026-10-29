
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.ProductEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 商品主表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ProductVO对象")
public class ProductVO extends ProductEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 品牌名称（连表查询）
	 */
	@Schema(description = "品牌名称")
	private String brandName;
	
	/**
	 * 分类名称（连表查询）
	 */
	@Schema(description = "分类名称")
	private String categoryName;
	
	/**
	 * SKU数量（子查询）
	 */
	@Schema(description = "SKU数量")
	private Integer skuCount;
}
