
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.PurchaseOrderEntity;
import com.xiaohucare.app.care.wms.vo.PurchaseOrderVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 进货单表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
public class PurchaseOrderWrapper extends BaseEntityWrapper<PurchaseOrderEntity, PurchaseOrderVO>  {
    public static PurchaseOrderWrapper build() {
        return new PurchaseOrderWrapper();
    }
	@Override
	public PurchaseOrderVO entityVO(PurchaseOrderEntity purchaseOrder) {
		if(purchaseOrder==null) {
			return null;
		}
		PurchaseOrderVO purchaseOrderVO = BeanUtil.copy(purchaseOrder, PurchaseOrderVO.class);
		return purchaseOrderVO;
	}

}
