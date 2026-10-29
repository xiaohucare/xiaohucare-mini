
package com.xiaohucare.app.care.activity.wrapper;
import com.xiaohucare.app.care.activity.entity.ActivityParticipantEntity;
import com.xiaohucare.app.care.activity.vo.ActivityParticipantVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 活动参与记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-11 22:46:55
 */
public class ActivityParticipantWrapper extends BaseEntityWrapper<ActivityParticipantEntity, ActivityParticipantVO>  {
    public static ActivityParticipantWrapper build() {
        return new ActivityParticipantWrapper();
    }
	@Override
	public ActivityParticipantVO entityVO(ActivityParticipantEntity activityParticipant) {
		if(activityParticipant==null) {
			return null;
		}
		ActivityParticipantVO activityParticipantVO = BeanUtil.copy(activityParticipant, ActivityParticipantVO.class);
		return activityParticipantVO;
	}

}
