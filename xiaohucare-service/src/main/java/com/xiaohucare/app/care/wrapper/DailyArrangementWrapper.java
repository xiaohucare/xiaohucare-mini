
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.DailyArrangementEntity;
import com.xiaohucare.app.care.vo.DailyArrangementVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 宠物酒店日常安排表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-07 08:33:19
 */
public class DailyArrangementWrapper extends BaseEntityWrapper<DailyArrangementEntity, DailyArrangementVO>  {
    public static DailyArrangementWrapper build() {
        return new DailyArrangementWrapper();
    }
	@Override
	public DailyArrangementVO entityVO(DailyArrangementEntity dailyArrangement) {
		if(dailyArrangement==null) {
			return null;
		}
		DailyArrangementVO dailyArrangementVO = BeanUtil.copy(dailyArrangement, DailyArrangementVO.class);
		return dailyArrangementVO;
	}

}
