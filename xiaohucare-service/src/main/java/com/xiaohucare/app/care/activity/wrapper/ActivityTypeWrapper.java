
package com.xiaohucare.app.care.activity.wrapper;
import com.xiaohucare.app.care.activity.entity.ActivityTypeEntity;
import com.xiaohucare.app.care.activity.vo.ActivityTypeVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 活动类型表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
public class ActivityTypeWrapper extends BaseEntityWrapper<ActivityTypeEntity, ActivityTypeVO>  {
    public static ActivityTypeWrapper build() {
        return new ActivityTypeWrapper();
    }
	@Override
	public ActivityTypeVO entityVO(ActivityTypeEntity activityType) {
		if(activityType==null) {
			return null;
		}
		ActivityTypeVO activityTypeVO = BeanUtil.copy(activityType, ActivityTypeVO.class);
		return activityTypeVO;
	}

}
