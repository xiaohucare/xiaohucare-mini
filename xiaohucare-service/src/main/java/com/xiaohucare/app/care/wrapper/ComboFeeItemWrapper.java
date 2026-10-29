
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ComboFeeItemEntity;
import com.xiaohucare.app.care.vo.ComboFeeItemVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 套餐费用项关联表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 13:47:57
 */
public class ComboFeeItemWrapper extends BaseEntityWrapper<ComboFeeItemEntity, ComboFeeItemVO>  {
    public static ComboFeeItemWrapper build() {
        return new ComboFeeItemWrapper();
    }
	@Override
	public ComboFeeItemVO entityVO(ComboFeeItemEntity comboFeeItem) {
		if(comboFeeItem==null) {
			return null;
		}
		ComboFeeItemVO comboFeeItemVO = BeanUtil.copy(comboFeeItem, ComboFeeItemVO.class);
		return comboFeeItemVO;
	}

}
