
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;
import com.xiaohucare.app.care.wms.vo.PurchaseItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 进货明细表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class PurchaseItemWrapper extends BaseEntityWrapper<PurchaseItemEntity, PurchaseItemVO>  {
    public static PurchaseItemWrapper build() {
        return new PurchaseItemWrapper();
    }
	@Override
	public PurchaseItemVO entityVO(PurchaseItemEntity purchaseItem) {
		if(purchaseItem==null) {
			return null;
		}
		PurchaseItemVO purchaseItemVO = BeanUtil.copy(purchaseItem, PurchaseItemVO.class);
		return purchaseItemVO;
	}

}
