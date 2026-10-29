
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.SkuEntity;
import com.xiaohucare.app.care.wms.vo.SkuVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 商品SKU表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
public class SkuWrapper extends BaseEntityWrapper<SkuEntity, SkuVO>  {
    public static SkuWrapper build() {
        return new SkuWrapper();
    }
	@Override
	public SkuVO entityVO(SkuEntity sku) {
		if(sku==null) {
			return null;
		}
		SkuVO skuVO = BeanUtil.copy(sku, SkuVO.class);
		return skuVO;
	}

}
