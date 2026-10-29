
package com.xiaohucare.app.care.meal.wrapper;
import com.xiaohucare.app.care.meal.entity.MealsMenusEntity;
import com.xiaohucare.app.care.meal.vo.MealsMenusVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 餐饮菜单表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-11 22:48:39
 */
public class MealsMenusWrapper extends BaseEntityWrapper<MealsMenusEntity, MealsMenusVO>  {
    public static MealsMenusWrapper build() {
        return new MealsMenusWrapper();
    }
	@Override
	public MealsMenusVO entityVO(MealsMenusEntity mealsMenus) {
		if(mealsMenus==null) {
			return null;
		}
		MealsMenusVO mealsMenusVO = BeanUtil.copy(mealsMenus, MealsMenusVO.class);
		return mealsMenusVO;
	}

}
