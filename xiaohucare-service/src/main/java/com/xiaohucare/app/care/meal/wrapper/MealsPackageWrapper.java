
package com.xiaohucare.app.care.meal.wrapper;
import java.util.ArrayList;
import java.util.List;

import com.xiaohucare.app.care.meal.entity.MealsPackageEntity;
import com.xiaohucare.app.care.meal.vo.MealsPackageVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 餐饮套餐表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-20 10:00:00
 */
public class MealsPackageWrapper extends BaseEntityWrapper<MealsPackageEntity, MealsPackageVO>  {
    public static MealsPackageWrapper build() {
        return new MealsPackageWrapper();
    }
	@Override
	public MealsPackageVO entityVO(MealsPackageEntity mealsPackage) {
		if(mealsPackage==null) {
			return null;
		}
		MealsPackageVO mealsPackageVO = BeanUtil.copy(mealsPackage, MealsPackageVO.class);
		// 将 menuIdStr 逗号串还原为数组，供前端套餐组合回显
		mealsPackageVO.setMenuIds(parseIds(mealsPackage.getMenuIdStr()));
		return mealsPackageVO;
	}

	/**
	 * 逗号分隔ID串转 List<Integer>
	 */
	private static List<Integer> parseIds(String str) {
		List<Integer> list = new ArrayList<>();
		if (StringUtils.isBlank(str)) {
			return list;
		}
		for (String s : str.split(",")) {
			String t = s.trim();
			if (!t.isEmpty()) {
				list.add(Integer.valueOf(t));
			}
		}
		return list;
	}

}
