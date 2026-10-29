package com.xiaohucare.app.care.facade.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiaohucare.app.care.entity.ChargeComboEntity;
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.facade.CareFacade;
import com.xiaohucare.app.care.service.AppointmentService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.app.care.vo.AppointmentVO;
import com.xiaohucare.app.care.service.ChargeComboService;
import com.xiaohucare.framework.core.domain.R;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CareFacadeImpl implements CareFacade {
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private ChargeComboService chargeComboService;

	@Override
	public R<AppointmentVO> book(AppointmentVO appointment) {
		if (appointment.getId() == null) {
			appointment.setCreateAt(new Date());
			appointment.setIsDelete(0);
			// 生成预约编号
			appointment.setAppointmentNo("APT" + System.currentTimeMillis());
			// 默认状态：待确认
			if (appointment.getStatus() == null) {
				appointment.setStatus(0);
			}
		}
		if (appointment.getComboId() != null && appointment.getComboId() > 0) {
			ChargeComboEntity chargeCombo = chargeComboService.getById(appointment.getComboId());
			if (chargeCombo != null) {
				appointment.setComboName(chargeCombo.getComboName());
				// 按套餐年/月/天三价 + 预计天数折算日单价（避免把年价当日价）
				ChargeCalcKit.ComboPrice comboPrice = ChargeCalcKit.fromCombo(
						chargeCombo.getChargeCycle(),
						chargeCombo.getYearPrice(), chargeCombo.getMonthPrice(), chargeCombo.getDayPrice(),
						appointment.getEstimatedDays());
				appointment.setDailyPrice(comboPrice.getDailyPrice());
				appointment.setDepositAmount(chargeCombo.getDepositAmount());
				appointment.setCoverImage(chargeCombo.getCoverImage());
				BigDecimal estimatedFee=BigDecimal.ZERO;
				if(appointment.getEstimatedDays()!=null&&appointment.getDailyPrice()!=null) {
					estimatedFee=appointment.getDailyPrice().multiply(new BigDecimal(appointment.getEstimatedDays()));
				}
				if(appointment.getDepositAmount()!=null)estimatedFee=estimatedFee.add(appointment.getDepositAmount());
				appointment.setEstimatedFee(estimatedFee);
			}
		}
		appointment.setUpdateAt(new Date());
		if (appointmentService.saveOrUpdate(appointment)) {
			return R.ok(appointment);
		}
		return R.error("数据错误");
	}

	/**
	 * 确认预约（状态0->1）
	 */
	@Override
	public R<Boolean> confirm(Long id) {
		AppointmentEntity appointment = appointmentService.getById(id);
		if (appointment == null) {
			return R.error("预约记录不存在");
		}
		if (appointment.getStatus() != 0) {
			return R.error("当前状态不允许确认");
		}
		if (appointment.getComboId() != null && appointment.getComboId() > 0) {
			ChargeComboEntity chargeCombo = chargeComboService.getById(appointment.getComboId());
			if (chargeCombo != null) {
				appointment.setComboName(chargeCombo.getComboName());
			}
		}
		appointment.setStatus(1);
		appointment.setUpdateAt(new Date());
		return R.ok(appointmentService.updateById(appointment));
	}

	/**
	 * 转为到店登记（状态1->3）
	 */
	@Override
	public R<Boolean> convert(Long id) {
		AppointmentEntity appointment = appointmentService.getById(id);
		if (appointment == null) {
			return R.error("预约记录不存在");
		}
		if (appointment.getStatus() != 1) {
			return R.error("请先确认预约再转为登记");
		}
		appointment.setStatus(3);
		appointment.setUpdateAt(new Date());
		return R.ok(appointmentService.updateById(appointment));
	}

	/**
	 * 取消预约（状态->2）
	 */
	@Override
	public R<Boolean> cancel(Long id) {
		AppointmentEntity appointment = appointmentService.getById(id);
		if (appointment == null) {
			return R.error("预约记录不存在");
		}
		appointment.setStatus(2);
		appointment.setUpdateAt(new Date());
		return R.ok(appointmentService.updateById(appointment));
	}

}
