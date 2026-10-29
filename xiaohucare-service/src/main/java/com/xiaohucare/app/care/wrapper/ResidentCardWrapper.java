
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ResidentCardEntity;
import com.xiaohucare.app.care.vo.ResidentCardVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 会员卡表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
public class ResidentCardWrapper extends BaseEntityWrapper<ResidentCardEntity, ResidentCardVO>  {
    public static ResidentCardWrapper build() {
        return new ResidentCardWrapper();
    }
	@Override
	public ResidentCardVO entityVO(ResidentCardEntity petOwnerCard) {
		if(petOwnerCard==null) {
			return null;
		}
		ResidentCardVO petOwnerCardVO = BeanUtil.copy(petOwnerCard, ResidentCardVO.class);
		return petOwnerCardVO;
	}

}
