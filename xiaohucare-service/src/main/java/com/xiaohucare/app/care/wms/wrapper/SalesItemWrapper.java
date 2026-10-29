
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.vo.SalesItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 出货明细表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class SalesItemWrapper extends BaseEntityWrapper<SalesItemEntity, SalesItemVO>  {
    public static SalesItemWrapper build() {
        return new SalesItemWrapper();
    }
	@Override
	public SalesItemVO entityVO(SalesItemEntity salesItem) {
		if(salesItem==null) {
			return null;
		}
		SalesItemVO salesItemVO = BeanUtil.copy(salesItem, SalesItemVO.class);
		return salesItemVO;
	}

}
