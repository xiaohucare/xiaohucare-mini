package com.xiaohucare.app.care.wms.vo;
import java.util.List;

import com.xiaohucare.app.care.wms.entity.PurchaseOrderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 进货单表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "PurchaseOrderVO对象")
public class PurchaseOrderVO extends PurchaseOrderEntity {
	private static final long serialVersionUID = 1L;
	@Schema(description = "进货明细（含SKU信息）")
	private List<PurchaseItemVO> purchaseItemVOList;
}
