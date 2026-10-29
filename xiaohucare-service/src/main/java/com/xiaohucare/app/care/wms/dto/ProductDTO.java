
package com.xiaohucare.app.care.wms.dto;

import com.xiaohucare.app.care.wms.entity.ProductEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 商品主表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class ProductDTO extends ProductEntity {
	private static final long serialVersionUID = 1L;

}
