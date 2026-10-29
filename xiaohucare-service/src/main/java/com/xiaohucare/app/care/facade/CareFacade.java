package com.xiaohucare.app.care.facade;

import com.xiaohucare.app.care.vo.AppointmentVO;
import com.xiaohucare.framework.core.domain.R;

public interface CareFacade {

	/**
	 * 取消预约（状态->2）
	 */
	R<Boolean> cancel(Long id);

	/**
	 * 转为到店登记（状态1->3）
	 */
	R<Boolean> convert(Long id);

	/**
	 * 确认预约（状态0->1）
	 */
	R<Boolean> confirm(Long id);

	R<AppointmentVO> book(AppointmentVO appointment);
}
