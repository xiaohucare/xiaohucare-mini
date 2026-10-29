
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ResidentCardRecordEntity;
import com.xiaohucare.app.care.vo.ResidentCardRecordVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 会员卡交易流水表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
public class ResidentCardRecordWrapper extends BaseEntityWrapper<ResidentCardRecordEntity, ResidentCardRecordVO>  {
    public static ResidentCardRecordWrapper build() {
        return new ResidentCardRecordWrapper();
    }
	@Override
	public ResidentCardRecordVO entityVO(ResidentCardRecordEntity petOwnerCardRecord) {
		if(petOwnerCardRecord==null) {
			return null;
		}
		ResidentCardRecordVO petOwnerCardRecordVO = BeanUtil.copy(petOwnerCardRecord, ResidentCardRecordVO.class);
		return petOwnerCardRecordVO;
	}

}
