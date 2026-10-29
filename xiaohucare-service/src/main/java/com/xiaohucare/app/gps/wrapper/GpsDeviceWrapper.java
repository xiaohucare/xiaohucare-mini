
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsDeviceEntity;
import com.xiaohucare.app.gps.vo.GpsDeviceVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 宠物GPS设备表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsDeviceWrapper extends BaseEntityWrapper<GpsDeviceEntity, GpsDeviceVO>  {
    public static GpsDeviceWrapper build() {
        return new GpsDeviceWrapper();
    }
	@Override
	public GpsDeviceVO entityVO(GpsDeviceEntity gpsDevice) {
		if(gpsDevice==null) {
			return null;
		}
		GpsDeviceVO gpsDeviceVO = BeanUtil.copy(gpsDevice, GpsDeviceVO.class);
		return gpsDeviceVO;
	}

}
