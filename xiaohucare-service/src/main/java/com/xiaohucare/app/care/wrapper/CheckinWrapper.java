
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.vo.CheckinVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 到店登记表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class CheckinWrapper extends BaseEntityWrapper<CheckinEntity, CheckinVO>  {
    public static CheckinWrapper build() {
        return new CheckinWrapper();
    }
	@Override
	public CheckinVO entityVO(CheckinEntity checkin) {
		if(checkin==null) {
			return null;
		}
		CheckinVO checkinVO = BeanUtil.copy(checkin, CheckinVO.class);
		// 照片以逗号分隔存储，拆成列表方便页面与小程序直接回显
		checkinVO.setCheckinPhotoList(StringUtils.splitToList(checkin.getCheckinPhotos(), ","));
		checkinVO.setBroughtPhotoList(StringUtils.splitToList(checkin.getBroughtPhotos(), ","));
		checkinVO.setCheckoutPhotoList(StringUtils.splitToList(checkin.getCheckoutPhotos(), ","));
		return checkinVO;
	}

}
