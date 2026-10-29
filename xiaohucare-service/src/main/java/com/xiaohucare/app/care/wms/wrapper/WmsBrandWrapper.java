
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.WmsBrandEntity;
import com.xiaohucare.app.care.wms.vo.WmsBrandVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 商品品牌表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class WmsBrandWrapper extends BaseEntityWrapper<WmsBrandEntity, WmsBrandVO>  {
    public static WmsBrandWrapper build() {
        return new WmsBrandWrapper();
    }
	@Override
	public WmsBrandVO entityVO(WmsBrandEntity brand) {
		if(brand==null) {
			return null;
		}
		WmsBrandVO wmsBrandVO = BeanUtil.copy(brand, WmsBrandVO.class);
		return wmsBrandVO;
	}

}
