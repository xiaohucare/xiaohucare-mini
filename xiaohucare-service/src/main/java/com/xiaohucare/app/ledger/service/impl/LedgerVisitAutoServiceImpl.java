package com.xiaohucare.app.ledger.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.ledger.constants.LedgerConstants;
import com.xiaohucare.app.ledger.entity.LedgerElderEntity;
import com.xiaohucare.app.ledger.entity.LedgerPlanLogEntity;
import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;
import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.service.LedgerPlanLogService;
import com.xiaohucare.app.ledger.service.LedgerVisitAutoService;
import com.xiaohucare.app.ledger.service.LedgerVisitOrderService;
import com.xiaohucare.app.ledger.service.LedgerVisitPlanService;
import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.common.utils.DateUtils;

/**
 * 探访工单自动化 Service 实现。
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerVisitAutoServiceImpl implements LedgerVisitAutoService {

	private static final Logger log = LoggerFactory.getLogger(LedgerVisitAutoServiceImpl.class);

	@Autowired
	private LedgerVisitPlanService planService;
	@Autowired
	private LedgerElderService elderService;
	@Autowired
	private LedgerVisitOrderService orderService;
	@Autowired
	private LedgerPlanLogService planLogService;
	@Autowired
	private EmployeeService employeeService;

	@Override
	public int generateDuePlans() {
		Date now = new Date();
		QueryWrapper<LedgerVisitPlanEntity> qw = new QueryWrapper<>();
		qw.eq("plan_type", 1).eq("state", 1);
		qw.and(w -> w.le("next_gen_time", now).or().isNull("next_gen_time"));
		List<LedgerVisitPlanEntity> plans = planService.list(qw);
		int total = 0;
		for (LedgerVisitPlanEntity plan : plans) {
			try {
				total += generateByPlan(plan);
			} catch (Exception e) {
				log.error("探访计划自动生成失败 planId={}", plan.getId(), e);
			}
		}
		return total;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int generateByPlan(LedgerVisitPlanEntity plan) {
		if (plan == null) {
			return 0;
		}
		Date now = new Date();
		// 字典自动初始化(幂等)

		// 1. 匹配目标老人
		QueryWrapper<LedgerElderEntity> eq = new QueryWrapper<>();
		eq.eq("state", 1);
		if (plan.getRiskLevel() != null) {
			eq.eq("risk_level", plan.getRiskLevel());
		}
		if (StringUtils.hasText(plan.getCareLevels())) {
			List<Integer> levels = new ArrayList<>();
			for (String s : plan.getCareLevels().split(",")) {
				String t = s.trim();
				if (!t.isEmpty()) {
					levels.add(Integer.valueOf(t));
				}
			}
			if (!levels.isEmpty()) {
				eq.in("care_level", levels);
			}
		}
		if (StringUtils.hasText(plan.getElderTypes())) {
			String[] ts = plan.getElderTypes().split(",");
			eq.and(w -> {
				boolean first = true;
				for (String s : ts) {
					String t = s.trim();
					if (t.isEmpty()) {
						continue;
					}
					if (!first) {
						w.or();
					}
					w.like("elder_types", t);
					first = false;
				}
			});
		}
		List<LedgerElderEntity> elders = elderService.list(eq);

		// 2. 自动指派人(可选)
		Long autoEmpId = plan.getAutoAssignEmployeeId();
		String autoEmpName = null;
		if (autoEmpId != null) {
			EmployeeEntity emp = employeeService.getById(autoEmpId);
			if (emp != null) {
				autoEmpName = emp.getName();
			}
		}

		Date periodStart = cycleStart(now, plan.getFrequencyType());
		int gen = 0;
		int skip = 0;
		for (LedgerElderEntity elder : elders) {
			// 幂等：同计划同老人在本周期已有未完成工单则跳过
			QueryWrapper<LedgerVisitOrderEntity> exist = new QueryWrapper<>();
			exist.eq("elder_id", elder.getId())
					.eq("plan_id", plan.getId())
					.in("order_status", LedgerConstants.VISIT_STATUS_WAIT_ASSIGN, LedgerConstants.VISIT_STATUS_WAIT_VISIT)
					.ge("plan_visit_time", periodStart);
			if (orderService.count(exist) > 0) {
				skip++;
				continue;
			}
			LedgerVisitOrderEntity order = new LedgerVisitOrderEntity();
			order.setStoreName(elder.getStoreName());
			order.setElderId(elder.getId());
			order.setElderName(elder.getName());
			order.setOrderNo(nextOrderNo(now));
			order.setVisitType(plan.getDefaultVisitType() != null ? plan.getDefaultVisitType() : 1);
			order.setPlanVisitTime(now);
			order.setSourceType(LedgerConstants.SOURCE_PLAN);
			order.setPlanId(plan.getId());
			if (autoEmpId != null) {
				order.setEmployeeId(autoEmpId);
				order.setEmployeeName(autoEmpName);
				order.setOrderStatus(LedgerConstants.VISIT_STATUS_WAIT_VISIT);
			} else {
				order.setOrderStatus(LedgerConstants.VISIT_STATUS_WAIT_ASSIGN);
			}
			order.setCreateBy("system");
			order.setCreateAt(now);
			order.setUpdateAt(now);
			orderService.save(order);
			gen++;
		}

		// 3. 推进计划下次执行时间
		plan.setLastGenTime(now);
		plan.setNextGenTime(nextGenTime(now, plan.getFrequencyType()));
		plan.setUpdateAt(now);
		planService.updateById(plan);

		// 4. 执行日志
		LedgerPlanLogEntity planLog = new LedgerPlanLogEntity();
		planLog.setPlanId(plan.getId());
		planLog.setGenCount(gen);
		planLog.setSkipCount(skip);
		planLog.setExecStatus(skip > 0 ? 2 : 1);
		planLog.setCreateAt(now);
		planLogService.save(planLog);
		return gen;
	}

	/** 生成探访工单编号：TF + yyyyMMdd + 4位机构当日流水 */
	private String nextOrderNo(Date date) {
		String prefix = "TF" + new SimpleDateFormat("yyyyMMdd").format(date);
		QueryWrapper<LedgerVisitOrderEntity> cq = new QueryWrapper<>();
		cq.likeRight("order_no", prefix);
		long cnt = orderService.count(cq);
		return prefix + String.format("%04d", cnt + 1);
	}

	/** 当前周期起点(用于幂等去重) */
	private Date cycleStart(Date now, Integer frequencyType) {
		int freq = frequencyType == null ? LedgerConstants.FREQ_MONTHLY : frequencyType;
		if (freq == LedgerConstants.FREQ_DAILY) {
			return DateUtils.getStartOfDate(now);
		}
		if (freq == LedgerConstants.FREQ_WEEKLY) {
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return DateUtils.getStartOfDate(c.getTime());
		}
		return DateUtils.getMonthFirstDayOfDate(now);
	}

	/** 依据频次计算下次执行时间 */
	private Date nextGenTime(Date now, Integer frequencyType) {
		int freq = frequencyType == null ? LedgerConstants.FREQ_MONTHLY : frequencyType;
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		if (freq == LedgerConstants.FREQ_DAILY) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		} else if (freq == LedgerConstants.FREQ_WEEKLY) {
			c.add(Calendar.DAY_OF_MONTH, 7);
		} else {
			c.add(Calendar.MONTH, 1);
		}
		return c.getTime();
	}
}
