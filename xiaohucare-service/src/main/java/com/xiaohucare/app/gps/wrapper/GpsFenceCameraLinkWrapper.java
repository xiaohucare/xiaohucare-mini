
package com.xiaohucare.app.gps.wrapper;
import com.xiaohucare.app.gps.entity.GpsFenceCameraLinkEntity;
import com.xiaohucare.app.gps.vo.GpsFenceCameraLinkVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 围栏-摄像头联动配置表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsFenceCameraLinkWrapper extends BaseEntityWrapper<GpsFenceCameraLinkEntity, GpsFenceCameraLinkVO>  {
    public static GpsFenceCameraLinkWrapper build() {
        return new GpsFenceCameraLinkWrapper();
    }
	@Override
	public GpsFenceCameraLinkVO entityVO(GpsFenceCameraLinkEntity gpsFenceCameraLink) {
		if(gpsFenceCameraLink==null) {
			return null;
		}
		GpsFenceCameraLinkVO gpsFenceCameraLinkVO = BeanUtil.copy(gpsFenceCameraLink, GpsFenceCameraLinkVO.class);
		return gpsFenceCameraLinkVO;
	}

}
