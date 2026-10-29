
package com.xiaohucare.app.care.wms.dto;

import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 进货明细表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class PurchaseItemDTO extends PurchaseItemEntity {
	private static final long serialVersionUID = 1L;

}
