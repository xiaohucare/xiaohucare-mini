
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsTrackEntity;
import com.xiaohucare.app.gps.vo.GpsTrackVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 宠物GPS轨迹表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsTrackWrapper extends BaseEntityWrapper<GpsTrackEntity, GpsTrackVO>  {
    public static GpsTrackWrapper build() {
        return new GpsTrackWrapper();
    }
	@Override
	public GpsTrackVO entityVO(GpsTrackEntity gpsTrack) {
		if(gpsTrack==null) {
			return null;
		}
		GpsTrackVO gpsTrackVO = BeanUtil.copy(gpsTrack, GpsTrackVO.class);
		return gpsTrackVO;
	}

}
