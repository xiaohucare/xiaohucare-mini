
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.constants.HOTEL_BOOK_STATUS_ENUM;
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.vo.AppointmentVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 预约表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class AppointmentWrapper extends BaseEntityWrapper<AppointmentEntity, AppointmentVO>  {
    public static AppointmentWrapper build() {
        return new AppointmentWrapper();
    }
	@Override
	public AppointmentVO entityVO(AppointmentEntity appointment) {
		if(appointment==null) {
			return null;
		}
		AppointmentVO appointmentVO = BeanUtil.copy(appointment, AppointmentVO.class);
		appointmentVO.setBookStateName(HOTEL_BOOK_STATUS_ENUM.fromCode(appointment.getStatus()).getMessage());
		return appointmentVO;
	}

}
