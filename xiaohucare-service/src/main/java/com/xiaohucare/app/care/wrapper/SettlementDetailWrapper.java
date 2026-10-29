
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.SettlementDetailEntity;
import com.xiaohucare.app.care.vo.SettlementDetailVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 结算明细表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class SettlementDetailWrapper extends BaseEntityWrapper<SettlementDetailEntity, SettlementDetailVO>  {
    public static SettlementDetailWrapper build() {
        return new SettlementDetailWrapper();
    }
	@Override
	public SettlementDetailVO entityVO(SettlementDetailEntity settlementDetail) {
		if(settlementDetail==null) {
			return null;
		}
		SettlementDetailVO settlementDetailVO = BeanUtil.copy(settlementDetail, SettlementDetailVO.class);
		return settlementDetailVO;
	}

}
