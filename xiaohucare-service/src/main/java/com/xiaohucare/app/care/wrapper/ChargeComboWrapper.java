
package com.xiaohucare.app.care.wrapper;
import java.util.List;

import com.xiaohucare.app.care.entity.ChargeComboEntity;
import com.xiaohucare.app.care.vo.ChargeComboVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 收费套餐表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
public class ChargeComboWrapper extends BaseEntityWrapper<ChargeComboEntity, ChargeComboVO>  {
    public static ChargeComboWrapper build() {
        return new ChargeComboWrapper();
    }
	@Override
	public ChargeComboVO entityVO(ChargeComboEntity chargeCombo) {
		if(chargeCombo==null) {
			return null;
		}
		ChargeComboVO chargeComboVO = BeanUtil.copy(chargeCombo, ChargeComboVO.class);
		List<String> suitableForList=StringUtils.splitToList(chargeCombo.getSuitableFor(), ",");
		chargeComboVO.setSuitableForList(suitableForList);
		List<String> environmentImageList=StringUtils.splitToList(chargeCombo.getEnvironmentImages(), ",");
		chargeComboVO.setEnvironmentImageList(environmentImageList);
		return chargeComboVO;
	}

}
