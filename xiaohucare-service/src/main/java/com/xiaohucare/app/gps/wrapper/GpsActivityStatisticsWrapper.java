
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsActivityStatisticsEntity;
import com.xiaohucare.app.gps.vo.GpsActivityStatisticsVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 宠物活动统计表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsActivityStatisticsWrapper extends BaseEntityWrapper<GpsActivityStatisticsEntity, GpsActivityStatisticsVO>  {
    public static GpsActivityStatisticsWrapper build() {
        return new GpsActivityStatisticsWrapper();
    }
	@Override
	public GpsActivityStatisticsVO entityVO(GpsActivityStatisticsEntity gpsActivityStatistics) {
		if(gpsActivityStatistics==null) {
			return null;
		}
		GpsActivityStatisticsVO gpsActivityStatisticsVO = BeanUtil.copy(gpsActivityStatistics, GpsActivityStatisticsVO.class);
		return gpsActivityStatisticsVO;
	}

}
