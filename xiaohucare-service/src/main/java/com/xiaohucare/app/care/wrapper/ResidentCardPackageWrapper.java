
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ResidentCardPackageEntity;
import com.xiaohucare.app.care.vo.ResidentCardPackageVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 会员卡套餐表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
public class ResidentCardPackageWrapper extends BaseEntityWrapper<ResidentCardPackageEntity, ResidentCardPackageVO>  {
    public static ResidentCardPackageWrapper build() {
        return new ResidentCardPackageWrapper();
    }
	@Override
	public ResidentCardPackageVO entityVO(ResidentCardPackageEntity petOwnerCardPackage) {
		if(petOwnerCardPackage==null) {
			return null;
		}
		ResidentCardPackageVO petOwnerCardPackageVO = BeanUtil.copy(petOwnerCardPackage, ResidentCardPackageVO.class);
		return petOwnerCardPackageVO;
	}

}
