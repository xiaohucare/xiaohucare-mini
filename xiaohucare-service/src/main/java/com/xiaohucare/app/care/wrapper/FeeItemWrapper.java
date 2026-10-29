
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.FeeItemEntity;
import com.xiaohucare.app.care.vo.FeeItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 收费类目表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
public class FeeItemWrapper extends BaseEntityWrapper<FeeItemEntity, FeeItemVO>  {
    public static FeeItemWrapper build() {
        return new FeeItemWrapper();
    }
	@Override
	public FeeItemVO entityVO(FeeItemEntity feeItem) {
		if(feeItem==null) {
			return null;
		}
		FeeItemVO feeItemVO = BeanUtil.copy(feeItem, FeeItemVO.class);
		return feeItemVO;
	}

}
