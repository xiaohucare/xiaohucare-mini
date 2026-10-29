
package com.xiaohucare.app.care.meal.wrapper;
import java.util.ArrayList;
import java.util.List;

import com.xiaohucare.app.care.meal.entity.MealsRecordEntity;
import com.xiaohucare.app.care.meal.vo.MealsRecordVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 餐饮记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-11 22:49:01
 */
public class MealsRecordWrapper extends BaseEntityWrapper<MealsRecordEntity, MealsRecordVO>  {
    public static MealsRecordWrapper build() {
        return new MealsRecordWrapper();
    }
	@Override
	public MealsRecordVO entityVO(MealsRecordEntity mealsRecord) {
		if(mealsRecord==null) {
			return null;
		}
		MealsRecordVO mealsRecordVO = BeanUtil.copy(mealsRecord, MealsRecordVO.class);
		// 将 residentIdStr 逗号串还原为数组，供前端专人专餐回显
		mealsRecordVO.setResidentIds(parseIds(mealsRecord.getResidentIdStr()));
		return mealsRecordVO;
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
