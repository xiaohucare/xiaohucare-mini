
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.WmsCategoryEntity;
import com.xiaohucare.app.care.wms.vo.WmsCategoryVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 商品分类表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class WmsCategoryWrapper extends BaseEntityWrapper<WmsCategoryEntity, WmsCategoryVO>  {
    public static WmsCategoryWrapper build() {
        return new WmsCategoryWrapper();
    }
	@Override
	public WmsCategoryVO entityVO(WmsCategoryEntity category) {
		if(category==null) {
			return null;
		}
		WmsCategoryVO wmsCategoryVO = BeanUtil.copy(category, WmsCategoryVO.class);
		return wmsCategoryVO;
	}

}
