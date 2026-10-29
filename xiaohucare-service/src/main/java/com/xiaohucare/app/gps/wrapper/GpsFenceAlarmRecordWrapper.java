
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsFenceAlarmRecordEntity;
import com.xiaohucare.app.gps.vo.GpsFenceAlarmRecordVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 围栏报警记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsFenceAlarmRecordWrapper extends BaseEntityWrapper<GpsFenceAlarmRecordEntity, GpsFenceAlarmRecordVO>  {
    public static GpsFenceAlarmRecordWrapper build() {
        return new GpsFenceAlarmRecordWrapper();
    }
	@Override
	public GpsFenceAlarmRecordVO entityVO(GpsFenceAlarmRecordEntity gpsFenceAlarmRecord) {
		if(gpsFenceAlarmRecord==null) {
			return null;
		}
		GpsFenceAlarmRecordVO gpsFenceAlarmRecordVO = BeanUtil.copy(gpsFenceAlarmRecord, GpsFenceAlarmRecordVO.class);
		return gpsFenceAlarmRecordVO;
	}

}
