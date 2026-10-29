
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.vo.SettlementVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 结算表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class SettlementWrapper extends BaseEntityWrapper<SettlementEntity, SettlementVO>  {
    public static SettlementWrapper build() {
        return new SettlementWrapper();
    }
	@Override
	public SettlementVO entityVO(SettlementEntity settlement) {
		if(settlement==null) {
			return null;
		}
		SettlementVO settlementVO = BeanUtil.copy(settlement, SettlementVO.class);
		return settlementVO;
	}

}
