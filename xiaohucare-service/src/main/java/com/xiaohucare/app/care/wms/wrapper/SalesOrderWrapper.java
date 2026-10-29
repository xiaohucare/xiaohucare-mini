
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;
import com.xiaohucare.app.care.wms.vo.SalesOrderVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 出货单表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class SalesOrderWrapper extends BaseEntityWrapper<SalesOrderEntity, SalesOrderVO>  {
    public static SalesOrderWrapper build() {
        return new SalesOrderWrapper();
    }
	@Override
	public SalesOrderVO entityVO(SalesOrderEntity salesOrder) {
		if(salesOrder==null) {
			return null;
		}
		SalesOrderVO salesOrderVO = BeanUtil.copy(salesOrder, SalesOrderVO.class);
		return salesOrderVO;
	}

}
