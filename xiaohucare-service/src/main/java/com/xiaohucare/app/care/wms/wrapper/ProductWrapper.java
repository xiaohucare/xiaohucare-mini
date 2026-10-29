
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.ProductEntity;
import com.xiaohucare.app.care.wms.vo.ProductVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 商品主表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class ProductWrapper extends BaseEntityWrapper<ProductEntity, ProductVO>  {
    public static ProductWrapper build() {
        return new ProductWrapper();
    }
	@Override
	public ProductVO entityVO(ProductEntity product) {
		if(product==null) {
			return null;
		}
		ProductVO productVO = BeanUtil.copy(product, ProductVO.class);
		return productVO;
	}

}
