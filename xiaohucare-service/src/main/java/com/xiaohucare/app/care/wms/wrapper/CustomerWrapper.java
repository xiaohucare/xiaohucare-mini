
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.CustomerEntity;
import com.xiaohucare.app.care.wms.vo.CustomerVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 客户表（商品购买客户） 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class CustomerWrapper extends BaseEntityWrapper<CustomerEntity, CustomerVO>  {
    public static CustomerWrapper build() {
        return new CustomerWrapper();
    }
	@Override
	public CustomerVO entityVO(CustomerEntity customer) {
		if(customer==null) {
			return null;
		}
		CustomerVO customerVO = BeanUtil.copy(customer, CustomerVO.class);
		return customerVO;
	}

}
