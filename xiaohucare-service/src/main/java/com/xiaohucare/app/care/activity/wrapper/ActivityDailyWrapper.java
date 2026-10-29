
package com.xiaohucare.app.care.activity.wrapper;
import com.xiaohucare.app.care.activity.entity.ActivityDailyEntity;
import com.xiaohucare.app.care.activity.vo.ActivityDailyVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 日常活动记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
public class ActivityDailyWrapper extends BaseEntityWrapper<ActivityDailyEntity, ActivityDailyVO>  {
    public static ActivityDailyWrapper build() {
        return new ActivityDailyWrapper();
    }
	@Override
	public ActivityDailyVO entityVO(ActivityDailyEntity activityDaily) {
		if(activityDaily==null) {
			return null;
		}
		ActivityDailyVO activityDailyVO = BeanUtil.copy(activityDaily, ActivityDailyVO.class);
		return activityDailyVO;
	}

}
