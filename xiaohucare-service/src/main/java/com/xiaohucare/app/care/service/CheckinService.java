package com.xiaohucare.app.care.service;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.vo.CheckinVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 到店登记表
 *
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public interface CheckinService extends BaseService<CheckinEntity> {

	CheckinVO getByAppointmentId(Long appointmentId);
}

