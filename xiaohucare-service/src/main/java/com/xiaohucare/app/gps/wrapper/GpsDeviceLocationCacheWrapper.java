
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsDeviceLocationCacheEntity;
import com.xiaohucare.app.gps.vo.GpsDeviceLocationCacheVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 设备位置缓存表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsDeviceLocationCacheWrapper extends BaseEntityWrapper<GpsDeviceLocationCacheEntity, GpsDeviceLocationCacheVO>  {
    public static GpsDeviceLocationCacheWrapper build() {
        return new GpsDeviceLocationCacheWrapper();
    }
	@Override
	public GpsDeviceLocationCacheVO entityVO(GpsDeviceLocationCacheEntity gpsDeviceLocationCache) {
		if(gpsDeviceLocationCache==null) {
			return null;
		}
		GpsDeviceLocationCacheVO gpsDeviceLocationCacheVO = BeanUtil.copy(gpsDeviceLocationCache, GpsDeviceLocationCacheVO.class);
		return gpsDeviceLocationCacheVO;
	}

}
