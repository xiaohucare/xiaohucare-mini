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
import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;
import com.xiaohucare.app.ledger.entity.LedgerPlanLogEntity;
import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.service.LedgerPatrolAutoService;
import com.xiaohucare.app.ledger.service.LedgerPatrolOrderService;
import com.xiaohucare.app.ledger.service.LedgerPlanLogService;
import com.xiaohucare.app.ledger.service.LedgerVisitPlanService;
import com.xiaohucare.app.care.entity.EmployeeEntity;
import com.xiaohucare.app.care.service.EmployeeService;
import com.xiaohucare.common.utils.DateUtils;

/**
 * 巡访工单自动化 Service 实现。
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Service
public class LedgerPatrolAutoServiceImpl implements LedgerPatrolAutoService {

	private static final Logger log = LoggerFactory.getLogger(LedgerPatrolAutoServiceImpl.class);

	@Autowired
	private LedgerVisitPlanService planService;
	@Autowired
	private LedgerElderService elderService;
	@Autowired
	private LedgerPatrolOrderService patrolOrderService;
	@Autowired
	private LedgerPlanLogService planLogService;
	@Autowired
	private EmployeeService employeeService;

	@Override
	public int generateDuePlans() {
		Date now = new Date();
		QueryWrapper<LedgerVisitPlanEntity> qw = new QueryWrapper<>();
		qw.eq("plan_type", 2).eq("state", 1);
		qw.and(w -> w.le("next_gen_time", now).or().isNull("next_gen_time"));
		List<LedgerVisitPlanEntity> plans = planService.list(qw);
		int total = 0;
		for (LedgerVisitPlanEntity plan : plans) {
			try {
				total += generateByPlan(plan);
			} catch (Exception e) {
				log.error("巡访计划自动生成失败 planId={}", plan.getId(), e);
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
			// 幂等：同计划同老人在本周期已有未完成巡访工单则跳过
			QueryWrapper<LedgerPatrolOrderEntity> exist = new QueryWrapper<>();
			exist.eq("elder_id", elder.getId())
					.eq("plan_id", plan.getId())
					.in("order_status", LedgerConstants.PATROL_STATUS_WAIT_DISPATCH,
							LedgerConstants.PATROL_STATUS_WAIT_PATROL, LedgerConstants.PATROL_STATUS_DISPOSING)
					.ge("plan_patrol_time", periodStart);
			if (patrolOrderService.count(exist) > 0) {
				skip++;
				continue;
			}
			LedgerPatrolOrderEntity order = new LedgerPatrolOrderEntity();
			order.setElderId(elder.getId());
			order.setElderName(elder.getName());
			order.setOrderNo(nextOrderNo(now));
			order.setPatrolType(plan.getDefaultVisitType() != null ? plan.getDefaultVisitType()
					: LedgerConstants.PATROL_TYPE_DAILY);
			order.setRiskLevel(elder.getRiskLevel() != null ? elder.getRiskLevel() : 1);
			order.setPlanPatrolTime(now);
			order.setSourceType(LedgerConstants.SOURCE_PLAN);
			order.setPlanId(plan.getId());
			if (autoEmpId != null) {
				order.setEmployeeId(autoEmpId);
				order.setEmployeeName(autoEmpName);
				order.setOrderStatus(LedgerConstants.PATROL_STATUS_WAIT_PATROL);
			} else {
				order.setOrderStatus(LedgerConstants.PATROL_STATUS_WAIT_DISPATCH);
			}
			order.setCreateBy("system");
			order.setCreateAt(now);
			order.setUpdateAt(now);
			patrolOrderService.save(order);
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

	/** 生成巡访工单编号：XF + yyyyMMdd + 4位机构当日流水 */
	private String nextOrderNo(Date date) {
		String prefix = "XF" + new SimpleDateFormat("yyyyMMdd").format(date);
		QueryWrapper<LedgerPatrolOrderEntity> cq = new QueryWrapper<>();
		cq.likeRight("order_no", prefix);
		long cnt = patrolOrderService.count(cq);
		return prefix + String.format("%04d", cnt + 1);
	}

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
