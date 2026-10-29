package com.xiaohucare.app.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.ledger.constants.LedgerConstants;
import com.xiaohucare.app.ledger.entity.LedgerElderEntity;
import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;
import com.xiaohucare.app.ledger.entity.LedgerPatrolRiskItemEntity;
import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.service.LedgerPatrolOrderService;
import com.xiaohucare.app.ledger.service.LedgerPatrolRiskItemService;
import com.xiaohucare.app.ledger.service.LedgerVisitOrderService;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 台账汇总报表：老人档案分布 + 探访工单统计 + 巡访工单/隐患统计 + 近 N 天生成趋势（按当前机构聚合）。
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Tag(name = "台账-汇总报表")
@RestController
@RequestMapping("/intra/care/ledger/report")
public class LedgerReportController extends BaseController {

	@Autowired
	private LedgerElderService elderService;
	@Autowired
	private LedgerVisitOrderService orderService;
	@Autowired
	private LedgerPatrolOrderService patrolService;
	@Autowired
	private LedgerPatrolRiskItemService riskItemService;

	@Operation(summary = "汇总总览(概况+分布+趋势)")
	@GetMapping("/overview")
	@PreAuthorize("@ss.hasPermi('ledger:report:list')")
	public R<Map<String, Object>> overview(@RequestParam(required = false, defaultValue = "14") int trendDays) {
		if (trendDays < 1) trendDays = 1;
		if (trendDays > 90) trendDays = 90;

		Map<String, Object> result = new LinkedHashMap<>();
		result.put("summary", buildSummary());
		result.put("elderByRisk", groupCount(true, "risk_level"));
		result.put("elderByCare", groupCount(true, "care_level"));
		result.put("elderBySource", groupCount(true, "elder_source"));
		result.put("orderByStatus", groupCount(false, "order_status"));
		result.put("orderByType", groupCount(false, "visit_type"));
		result.put("orderTrend", buildTrend(trendDays));
		// ===== 巡访工单维度 =====
		result.put("patrolSummary", buildPatrolSummary());
		result.put("patrolByStatus", groupPatrol("order_status"));
		result.put("patrolByType", groupPatrol("patrol_type"));
		result.put("patrolByRisk", groupPatrol("risk_level"));
		result.put("riskItemTop", groupRiskCode());
		result.put("patrolTrend", buildPatrolTrend(trendDays));
		return R.data(result);
	}

	/** 概况计数 */
	private Map<String, Object> buildSummary() {
		Map<String, Object> s = new LinkedHashMap<>();
		// 老人：正常档案(state=1)总数 + 高风险(risk_level=3)数
		s.put("elderTotal", elderService.count(qwElder().eq("state", 1)));
		s.put("elderHighRisk", elderService.count(qwElder().eq("state", 1).eq("risk_level", 3)));

		long total = orderService.count(qwOrder());
		long waitAssign = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_WAIT_ASSIGN));
		long waitVisit = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_WAIT_VISIT));
		long revisit = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_REVISIT));
		long done = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_FINISHED));
		long delay = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_DELAY));
		long cancel = orderService.count(qwOrder().eq("order_status", LedgerConstants.VISIT_STATUS_CANCEL));
		long monthNew = orderService.count(qwOrder().ge("create_at", monthStart()));

		// 有效工单(剔除取消)完成率
		long effective = total - cancel;
		double doneRate = effective > 0 ? (done * 100.0 / effective) : 0d;

		s.put("orderTotal", total);
		s.put("orderWaitAssign", waitAssign);
		s.put("orderDoing", waitVisit + revisit);
		s.put("orderDone", done);
		s.put("orderDelay", delay);
		s.put("orderCancel", cancel);
		s.put("monthNew", monthNew);
		s.put("doneRate", Math.round(doneRate * 10) / 10.0);
		return s;
	}

	/** 分布：按 code 列 groupBy，返回 [{k,c}] */
	private List<Map<String, Object>> groupCount(boolean elder, String col) {
		List<Map<String, Object>> raw = elder
				? elderService.listMaps(qwElder().clone().select(col + " as k", "count(*) as c").groupBy(col))
				: orderService.listMaps(qwOrder().clone().select(col + " as k", "count(*) as c").groupBy(col));
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> m : raw) {
			Object k = m.get("k");
			Object c = m.get("c");
			if (k == null) continue; // 忽略未填写(null 分组)
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("k", k instanceof Number ? ((Number) k).intValue() : k);
			item.put("c", c instanceof Number ? ((Number) c).longValue() : 0L);
			list.add(item);
		}
		return list;
	}

	/** 近 N 天每日工单生成量，缺失日期补 0 */
	private List<Map<String, Object>> buildTrend(int days) {
		Date begin = dayBeginAdd(days - 1);
		QueryWrapper<LedgerVisitOrderEntity> qw = qwOrder().ge("create_at", begin);
		qw.select("DATE(create_at) as d", "count(*) as c").groupBy("DATE(create_at)");
		List<Map<String, Object>> maps = orderService.listMaps(qw);
		Map<String, Long> byDay = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> m : maps) {
			Object d = m.get("d");
			Object c = m.get("c");
			String key = d == null ? null : sdf.format(toDate(d));
			if (key != null) byDay.put(key, c instanceof Number ? ((Number) c).longValue() : 0L);
		}
		List<Map<String, Object>> trend = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		for (int i = 0; i < days; i++) {
			String key = sdf.format(cal.getTime());
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("date", key);
			item.put("count", byDay.getOrDefault(key, 0L));
			trend.add(item);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return trend;
	}

	/** 巡访概况：状态计数 + 结案率 + 隐患整改率 + 复查工单数 */
	private Map<String, Object> buildPatrolSummary() {
		Map<String, Object> s = new LinkedHashMap<>();
		long total = patrolService.count(qwPatrol());
		long waitDispatch = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_WAIT_DISPATCH));
		long waitPatrol = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_WAIT_PATROL));
		long disposing = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_DISPOSING));
		long waitReview = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_WAIT_REVIEW));
		long closed = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_CLOSED));
		long cancel = patrolService.count(qwPatrol().eq("order_status", LedgerConstants.PATROL_STATUS_CANCEL));
		long monthNew = patrolService.count(qwPatrol().ge("create_at", monthStart()));
		long recheck = patrolService.count(qwPatrol().eq("patrol_type", LedgerConstants.PATROL_TYPE_RECHECK));

		long effective = total - cancel;
		double closeRate = effective > 0 ? (closed * 100.0 / effective) : 0d;

		// 隐患明细(本机构,通过子查询关联巡访工单)
		QueryWrapper<LedgerPatrolRiskItemEntity> riskQw = ownRiskItems();
		long riskTotal = riskItemService.count(riskQw.clone());
		long riskFixed = riskItemService.count(riskQw.clone().eq("is_fixed", 1));
		double fixRate = riskTotal > 0 ? (riskFixed * 100.0 / riskTotal) : 0d;

		s.put("patrolTotal", total);
		s.put("patrolWaitDispatch", waitDispatch);
		s.put("patrolDoing", waitPatrol + disposing + waitReview);
		s.put("patrolClosed", closed);
		s.put("patrolCancel", cancel);
		s.put("patrolMonthNew", monthNew);
		s.put("patrolRecheck", recheck);
		s.put("patrolCloseRate", Math.round(closeRate * 10) / 10.0);
		s.put("riskTotal", riskTotal);
		s.put("riskFixed", riskFixed);
		s.put("riskFixRate", Math.round(fixRate * 10) / 10.0);
		return s;
	}

	/** 巡访分布：按 code 列 groupBy，返回 [{k,c}] */
	private List<Map<String, Object>> groupPatrol(String col) {
		return normalizeKV(patrolService.listMaps(
				qwPatrol().clone().select(col + " as k", "count(*) as c").groupBy(col)));
	}

	/** 隐患项 Top N：按 risk_code 统计数量与已整改数（仅本机构工单） */
	private List<Map<String, Object>> groupRiskCode() {
		List<Map<String, Object>> raw = riskItemService.listMaps(ownRiskItems()
				.select("risk_code as k", "count(*) as c", "sum(is_fixed = 1) as fixed")
				.groupBy("risk_code").orderByDesc("c").last("limit 8"));
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> m : raw) {
			Object k = m.get("k");
			if (k == null) continue;
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("k", k);
			item.put("c", m.get("c") instanceof Number ? ((Number) m.get("c")).longValue() : 0L);
			item.put("fixed", m.get("fixed") instanceof Number ? ((Number) m.get("fixed")).longValue() : 0L);
			list.add(item);
		}
		return list;
	}

	/** 巡访工单近 N 天每日生成量，缺失日期补 0 */
	private List<Map<String, Object>> buildPatrolTrend(int days) {
		Date begin = dayBeginAdd(days - 1);
		QueryWrapper<LedgerPatrolOrderEntity> qw = qwPatrol().ge("create_at", begin);
		qw.select("DATE(create_at) as d", "count(*) as c").groupBy("DATE(create_at)");
		return fillTrend(patrolService.listMaps(qw), begin, days);
	}

	/** 将 [{d,c}] 映射按日期补齐为 [{date,count}] */
	private List<Map<String, Object>> fillTrend(List<Map<String, Object>> maps, Date begin, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Long> byDay = new LinkedHashMap<>();
		for (Map<String, Object> m : maps) {
			Object d = m.get("d");
			Object c = m.get("c");
			String key = d == null ? null : sdf.format(toDate(d));
			if (key != null) byDay.put(key, c instanceof Number ? ((Number) c).longValue() : 0L);
		}
		List<Map<String, Object>> trend = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		for (int i = 0; i < days; i++) {
			String key = sdf.format(cal.getTime());
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("date", key);
			item.put("count", byDay.getOrDefault(key, 0L));
			trend.add(item);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return trend;
	}

	/** 将 [{k,c}] 分组结果清洗掉 null 分组并规整数字类型 */
	private List<Map<String, Object>> normalizeKV(List<Map<String, Object>> raw) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> m : raw) {
			Object k = m.get("k");
			Object c = m.get("c");
			if (k == null) continue;
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("k", k instanceof Number ? ((Number) k).intValue() : k);
			item.put("c", c instanceof Number ? ((Number) c).longValue() : 0L);
			list.add(item);
		}
		return list;
	}

	private QueryWrapper<LedgerPatrolOrderEntity> qwPatrol() {
		return new QueryWrapper<LedgerPatrolOrderEntity>();
	}

	/** 隐患明细通过子查询限定为巡访工单对应的隐患 */
	private QueryWrapper<LedgerPatrolRiskItemEntity> ownRiskItems() {
		return new QueryWrapper<LedgerPatrolRiskItemEntity>()
				.inSql("order_id", "select id from e_ledger_patrol_order");
	}

	private QueryWrapper<LedgerElderEntity> qwElder() {
		return new QueryWrapper<LedgerElderEntity>();
	}

	private QueryWrapper<LedgerVisitOrderEntity> qwOrder() {
		return new QueryWrapper<LedgerVisitOrderEntity>();
	}

	private Date monthStart() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	private Date dayBeginAdd(int minusDays) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, -minusDays);
		return c.getTime();
	}

	private Date toDate(Object d) {
		if (d instanceof java.sql.Date) return new Date(((java.sql.Date) d).getTime());
		if (d instanceof java.util.Date) return (Date) d;
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(d));
		} catch (Exception e) {
			return new Date();
		}
	}
}
