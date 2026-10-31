package com.xiaohucare.app.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.ledger.constants.LedgerConstants;
import com.xiaohucare.app.ledger.entity.LedgerElderEntity;
import com.xiaohucare.app.ledger.entity.LedgerOrderLogEntity;
import com.xiaohucare.app.ledger.entity.LedgerPatrolOrderEntity;
import com.xiaohucare.app.ledger.entity.LedgerPatrolRiskItemEntity;
import com.xiaohucare.app.ledger.entity.LedgerRevisitEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.service.LedgerOrderLogService;
import com.xiaohucare.app.ledger.service.LedgerPatrolAutoService;
import com.xiaohucare.app.ledger.service.LedgerPatrolOrderService;
import com.xiaohucare.app.ledger.service.LedgerPatrolRiskItemService;
import com.xiaohucare.app.ledger.service.LedgerRevisitService;
import com.xiaohucare.app.ledger.vo.LedgerPatrolOrderVO;
import com.xiaohucare.app.ledger.vo.LedgerPatrolRiskItemVO;
import com.xiaohucare.app.ledger.vo.LedgerRevisitVO;
import com.xiaohucare.app.ledger.wrapper.LedgerPatrolOrderWrapper;
import com.xiaohucare.app.ledger.wrapper.LedgerPatrolRiskItemWrapper;
import com.xiaohucare.app.ledger.wrapper.LedgerRevisitWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * 巡访工单(安全巡查/隐患处置)：CRUD + 处置流转(分派→签到→处置→复查→结案) + 隐患明细 + 回访 + 自动化
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Tag(name = "台账-巡访工单")
@RestController
@RequestMapping("/intra/care/ledger/patrol")
public class LedgerPatrolOrderController extends BaseController {

	@Autowired
	private LedgerPatrolOrderService patrolService;
	@Autowired
	private LedgerPatrolRiskItemService riskItemService;
	@Autowired
	private LedgerRevisitService revisitService;
	@Autowired
	private LedgerOrderLogService orderLogService;
	@Autowired
	private LedgerElderService elderService;
	@Autowired
	private LedgerPatrolAutoService patrolAutoService;

	@Operation(summary = "列表")
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:list')")
	public R<VPage<LedgerPatrolOrderVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerPatrolOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("orderNo", "elderName", "employeeName"), LedgerPatrolOrderEntity.class);
		
		queryWrapper.eq(StringUtils.isNotNull(params.get("orderStatus")), "order_status", params.get("orderStatus"));
		queryWrapper.eq(StringUtils.isNotNull(params.get("patrolType")), "patrol_type", params.get("patrolType"));
		queryWrapper.eq(StringUtils.isNotNull(params.get("riskLevel")), "risk_level", params.get("riskLevel"));
		queryWrapper.like(StringUtils.isNotNull(params.get("orderNo")), "order_no", params.get("orderNo"));
		queryWrapper.like(StringUtils.isNotNull(params.get("elderName")), "elder_name", params.get("elderName"));
		queryWrapper.like(StringUtils.isNotNull(params.get("employeeName")), "employee_name", params.get("employeeName"));
		queryWrapper.orderByDesc("id");
		IPage<LedgerPatrolOrderEntity> pages = patrolService.page(VQueryKit.getPage(params), queryWrapper);
		VPage<LedgerPatrolOrderVO> page = LedgerPatrolOrderWrapper.build().toVPageVO(pages);
		// 列表批量聚合隐患数量(总数/已整改数)，供前端展示整改进度
		fillRiskCounts(page.getList());
		return R.data(page);
	}

	@Operation(summary = "详情")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:query')")
	public R<LedgerPatrolOrderVO> detail(@PathVariable("id") Long id) {
		LedgerPatrolOrderVO vo = LedgerPatrolOrderWrapper.build().entityVO(patrolService.getById(id));
		if (vo != null) {
			fillRiskCounts(Collections.singletonList(vo));
		}
		return R.data(vo);
	}

	@Operation(summary = "保存&更新(人工上报/编辑工单)")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:add')")
	public R<LedgerPatrolOrderVO> submit(@Valid @RequestBody LedgerPatrolOrderVO order) {
		if (order.getId() != null && loadOwn(order.getId()) == null) {
			return R.error("工单不存在或越权");
		}
		if (order.getId() == null) {
			order.setOrderNo(nextOrderNo(new Date()));
			order.setSourceType(LedgerConstants.SOURCE_MANUAL);
			if (order.getElderId() != null) {
				LedgerElderEntity elder = elderService.getById(order.getElderId());
				if (elder != null) {
					order.setElderName(elder.getName());
					if (order.getRiskLevel() == null) {
						order.setRiskLevel(elder.getRiskLevel());
					}
				}
			}
			if (order.getPatrolType() == null) {
				order.setPatrolType(LedgerConstants.PATROL_TYPE_DAILY);
			}
			if (order.getRiskLevel() == null) {
				order.setRiskLevel(1);
			}
			order.setOrderStatus(order.getEmployeeId() != null ? LedgerConstants.PATROL_STATUS_WAIT_PATROL
					: LedgerConstants.PATROL_STATUS_WAIT_DISPATCH);
			order.setCreateAt(new Date());
			order.setCreateBy(getUsername());
		}
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		if (patrolService.saveOrUpdate(order)) {
			return R.ok(order);
		}
		return R.error("保存失败");
	}

	@Operation(summary = "删除")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:remove')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		return R.ok(patrolService.removeByIds(Arrays.asList(ids)));
	}

	@Operation(summary = "导出")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerPatrolOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("orderNo", "elderName"), LedgerPatrolOrderEntity.class);
		
		queryWrapper.orderByDesc("id");
		List<LedgerPatrolOrderEntity> list = patrolService.list(queryWrapper);
		ExcelUtil<LedgerPatrolOrderEntity> util = new ExcelUtil<>(LedgerPatrolOrderEntity.class);
		util.exportExcel(response, list, "巡访工单");
	}

	// ================= 工单流转 =================

	@Operation(summary = "分派/指派巡访员")
	@PostMapping("/dispatch")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispatch')")
	public R<Boolean> dispatch(@RequestParam Long orderId, @RequestParam Long employeeId,
			@RequestParam(required = false) String employeeName) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		order.setEmployeeId(employeeId);
		order.setEmployeeName(employeeName);
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_WAIT_PATROL);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_ASSIGN, from, order.getOrderStatus(), null, employeeId, "分派巡访员：" + employeeName);
		return R.ok(true);
	}

	@Operation(summary = "转派")
	@PostMapping("/transfer")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispatch')")
	public R<Boolean> transfer(@RequestParam Long orderId, @RequestParam Long employeeId,
			@RequestParam(required = false) String employeeName) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Long fromEmp = order.getEmployeeId();
		order.setEmployeeId(employeeId);
		order.setEmployeeName(employeeName);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_TRANSFER, order.getOrderStatus(), order.getOrderStatus(), fromEmp, employeeId,
				"转派给：" + employeeName);
		return R.ok(true);
	}

	@Operation(summary = "撤回(取消指派,回到待分派)")
	@PostMapping("/revoke")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispatch')")
	public R<Boolean> revoke(@RequestParam Long orderId) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		final Integer from = order.getOrderStatus();
		Long fromEmp = order.getEmployeeId();
		Date now = new Date();
		// updateById 会忽略 null 字段,清空指派人需用 UpdateWrapper 显式 set null
		UpdateWrapper<LedgerPatrolOrderEntity> uw = new UpdateWrapper<>();
		uw.eq("id", orderId)
				.set("employee_id", null)
				.set("employee_name", null)
				.set("order_status", LedgerConstants.PATROL_STATUS_WAIT_DISPATCH)
				.set("update_at", now)
				.set("update_by", getUsername());
		patrolService.update(uw);
		order.setEmployeeId(null);
		order.setEmployeeName(null);
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_WAIT_DISPATCH);
		addLog(order, LedgerConstants.ACTION_REVOKE, from, order.getOrderStatus(), fromEmp, null, "撤回指派");
		return R.ok(true);
	}

	@Operation(summary = "到场签到")
	@PostMapping("/sign")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<Boolean> sign(@RequestParam Long orderId, @RequestParam(required = false) String patrolPlace) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Date now = new Date();
		order.setSignTime(now);
		if (StringUtils.isNotEmpty(patrolPlace)) {
			order.setPatrolPlace(patrolPlace);
		}
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_SIGN, order.getOrderStatus(), order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "到场签到：" + patrolPlace);
		return R.ok(true);
	}

	@Operation(summary = "开始处置(待巡访→处置中)")
	@PostMapping("/start")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<Boolean> start(@RequestParam Long orderId) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setRealPatrolTime(now);
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_DISPOSING);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_START, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "开始巡防处置");
		return R.ok(true);
	}

	@Operation(summary = "提交处置意见(处置中→待复查)")
	@PostMapping("/dispose")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<Boolean> dispose(@RequestParam Long orderId, @RequestParam(required = false) String disposeOpinion,
			@RequestParam(required = false) String patrolImages,
			@RequestParam(required = false) String rectifyEmployeeId,
			@RequestParam(required = false) String rectifyEmployeeName,
			@RequestParam(required = false) String rectifyDeadline) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		order.setDisposeOpinion(disposeOpinion);
		if (StringUtils.isNotEmpty(patrolImages)) {
			order.setPatrolImages(patrolImages);
		}
		if (StringUtils.isNotEmpty(rectifyEmployeeId)) {
			order.setRectifyEmployeeId(Long.valueOf(rectifyEmployeeId));
			order.setRectifyEmployeeName(rectifyEmployeeName);
		}
		Date deadline = parseDateTime(rectifyDeadline);
		if (deadline != null) {
			order.setRectifyDeadline(deadline);
		}
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_WAIT_REVIEW);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_RECTIFY, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "提交处置意见,转待复查");
		return R.ok(true);
	}

	@Operation(summary = "复查(pass=true 结案,否则退回处置中)")
	@PostMapping("/review")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:review')")
	public R<Boolean> review(@RequestParam Long orderId, @RequestParam boolean pass,
			@RequestParam(required = false) String reviewResult) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setReviewTime(now);
		order.setReviewResult(reviewResult);
		order.setReviewBy(getUsername());
		if (pass) {
			order.setOrderStatus(LedgerConstants.PATROL_STATUS_CLOSED);
			patrolService.updateById(order);
			addLog(order, LedgerConstants.ACTION_CLOSE, from, order.getOrderStatus(), order.getEmployeeId(),
					order.getEmployeeId(), "复查通过,结案：" + reviewResult);
		} else {
			order.setOrderStatus(LedgerConstants.PATROL_STATUS_DISPOSING);
			patrolService.updateById(order);
			addLog(order, LedgerConstants.ACTION_REJECT, from, order.getOrderStatus(), order.getEmployeeId(),
					order.getEmployeeId(), "复查不通过,退回处置：" + reviewResult);
		}
		return R.ok(true);
	}

	@Operation(summary = "直接结案")
	@PostMapping("/close")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:review')")
	public R<Boolean> close(@RequestParam Long orderId, @RequestParam(required = false) String reviewResult) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setReviewTime(now);
		order.setReviewResult(reviewResult);
		order.setReviewBy(getUsername());
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_CLOSED);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_CLOSE, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "结案：" + reviewResult);
		return R.ok(true);
	}

	@Operation(summary = "取消")
	@PostMapping("/cancel")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<Boolean> cancel(@RequestParam Long orderId, @RequestParam(required = false) String cancelReason) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setCancelReason(cancelReason);
		order.setCancelTime(now);
		order.setOrderStatus(LedgerConstants.PATROL_STATUS_CANCEL);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_CANCEL, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "取消：" + cancelReason);
		return R.ok(true);
	}

	@Operation(summary = "延期(调整计划巡访时间)")
	@PostMapping("/delay")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispatch')")
	public R<Boolean> delay(@RequestParam Long orderId, @RequestParam String planPatrolTime,
			@RequestParam(required = false) String delayReason) {
		LedgerPatrolOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		if (order.getOrderStatus() != null && (order.getOrderStatus() == LedgerConstants.PATROL_STATUS_CLOSED
				|| order.getOrderStatus() == LedgerConstants.PATROL_STATUS_CANCEL)) {
			return R.error("已结案或已取消的工单不能延期");
		}
		Date planTime = parseDateTime(planPatrolTime);
		if (planTime == null) {
			return R.error("计划巡访时间格式错误");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setPlanPatrolTime(planTime);
		order.setDelayReason(delayReason);
		order.setDelayTime(now);
		order.setDelayCount((order.getDelayCount() == null ? 0 : order.getDelayCount()) + 1);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		patrolService.updateById(order);
		addLog(order, LedgerConstants.ACTION_DELAY, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "计划时间调整为 " + planPatrolTime
						+ (StringUtils.isNotEmpty(delayReason) ? "，延期原因：" + delayReason : ""));
		return R.ok(true);
	}

	// ================= 隐患明细 =================

	@Operation(summary = "按工单查询隐患明细")
	@GetMapping("/riskItems/{orderId}")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:query')")
	public R<List<LedgerPatrolRiskItemVO>> riskItems(@PathVariable("orderId") Long orderId) {
		QueryWrapper<LedgerPatrolRiskItemEntity> qw = new QueryWrapper<>();
		qw.eq("order_id", orderId).orderByAsc("id");
		return R.data(LedgerPatrolRiskItemWrapper.build().listVO(riskItemService.list(qw)));
	}

	@Operation(summary = "新增/修改隐患明细")
	@PostMapping("/riskItem/submit")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<LedgerPatrolRiskItemVO> submitRiskItem(@Valid @RequestBody LedgerPatrolRiskItemVO item) {
		if (loadOwn(item.getOrderId()) == null) {
			return R.error("工单不存在或越权");
		}
		Date now = new Date();
		if (item.getId() == null) {
			item.setCreateAt(now);
			if (item.getIsFixed() == null) {
				item.setIsFixed(0);
			}
		}
		// 标记已整改时补全整改时间/整改人
		if (item.getIsFixed() != null && item.getIsFixed() == 1) {
			if (item.getFixTime() == null) {
				item.setFixTime(now);
			}
			if (StringUtils.isEmpty(item.getFixBy())) {
				item.setFixBy(getUsername());
			}
		}
		item.setUpdateAt(now);
		if (riskItemService.saveOrUpdate(item)) {
			return R.ok(item);
		}
		return R.error("保存失败");
	}

	@Operation(summary = "删除隐患明细")
	@PostMapping("/riskItem/delete")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispose')")
	public R<Boolean> deleteRiskItem(@RequestBody Long[] ids) {
		// 归属校验：隐患明细所属巡访工单须属本机构
		for (Long id : ids) {
			LedgerPatrolRiskItemEntity it = riskItemService.getById(id);
			if (it != null && loadOwn(it.getOrderId()) == null) {
				return R.error("存在越权隐患明细,删除中止");
			}
		}
		return R.ok(riskItemService.removeByIds(Arrays.asList(ids)));
	}

	@Operation(summary = "标记隐患已整改(支持整改说明/照片/整改人)")
	@PostMapping("/riskItem/fix")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:review')")
	public R<Boolean> fixRiskItem(@RequestBody LedgerPatrolRiskItemVO item) {
		if (item == null || item.getId() == null) {
			return R.error("隐患明细ID不能为空");
		}
		LedgerPatrolRiskItemEntity it = riskItemService.getById(item.getId());
		if (it == null || loadOwn(it.getOrderId()) == null) {
			return R.error("隐患明细不存在或越权");
		}
		Date now = new Date();
		it.setIsFixed(1);
		it.setFixTime(now);
		it.setFixDesc(item.getFixDesc());
		it.setFixImages(item.getFixImages());
		it.setFixBy(StringUtils.isNotEmpty(item.getFixBy()) ? item.getFixBy() : getUsername());
		it.setUpdateAt(now);
		return R.ok(riskItemService.updateById(it));
	}

	// ================= 回访 =================

	@Operation(summary = "按工单查询回访记录")
	@GetMapping("/revisit/{orderId}")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:query')")
	public R<List<LedgerRevisitVO>> revisits(@PathVariable("orderId") Long orderId) {
		QueryWrapper<LedgerRevisitEntity> qw = new QueryWrapper<>();
		qw.eq("order_kind", LedgerConstants.ORDER_KIND_PATROL).eq("order_id", orderId).orderByAsc("id");
		return R.data(LedgerRevisitWrapper.build().listVO(revisitService.list(qw)));
	}

	@Operation(summary = "提交回访记录(不合格自动生成隐患复查工单)")
	@PostMapping("/revisit/submit")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:review')")
	public R<LedgerRevisitVO> submitRevisit(@Valid @RequestBody LedgerRevisitVO revisit) {
		LedgerPatrolOrderEntity order = loadOwn(revisit.getOrderId());
		if (order == null) {
			return R.error("工单不存在或越权");
		}
		Date now = new Date();
		revisit.setOrderKind(LedgerConstants.ORDER_KIND_PATROL);
		revisit.setElderId(order.getElderId());
		if (revisit.getRevisitTime() == null) {
			revisit.setRevisitTime(now);
		}
		if (revisit.getId() == null) {
			revisit.setCreateBy(getUsername());
			revisit.setCreateAt(now);
		}
		// 不合格：生成隐患复查工单(patrol_type=4,来源指向原单)
		if (revisit.getIsQualified() != null && revisit.getIsQualified() == 0) {
			LedgerPatrolOrderEntity recheck = new LedgerPatrolOrderEntity();
			recheck.setElderId(order.getElderId());
			recheck.setElderName(order.getElderName());
			recheck.setOrderNo(nextOrderNo(now));
			recheck.setPatrolType(LedgerConstants.PATROL_TYPE_RECHECK);
			recheck.setRiskLevel(order.getRiskLevel() != null ? order.getRiskLevel() : 1);
			recheck.setPlanPatrolTime(now);
			recheck.setOrderStatus(order.getEmployeeId() != null ? LedgerConstants.PATROL_STATUS_WAIT_PATROL
					: LedgerConstants.PATROL_STATUS_WAIT_DISPATCH);
			recheck.setEmployeeId(order.getEmployeeId());
			recheck.setEmployeeName(order.getEmployeeName());
			recheck.setSourceOrderId(order.getId());
			recheck.setSourceType(LedgerConstants.SOURCE_MANUAL);
			recheck.setCreateBy(getUsername());
			recheck.setCreateAt(now);
			recheck.setUpdateAt(now);
			patrolService.save(recheck);
			revisit.setRevisitOrderId(recheck.getId());
			addLog(recheck, LedgerConstants.ACTION_ASSIGN, null, recheck.getOrderStatus(), null, recheck.getEmployeeId(),
					"回访不合格自动生成隐患复查工单");
		}
		if (revisitService.saveOrUpdate(revisit)) {
			return R.ok(revisit);
		}
		return R.error("保存失败");
	}

	// ================= 流转日志 & 自动化 =================

	@Operation(summary = "查询工单流转日志")
	@GetMapping("/logs/{orderId}")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:query')")
	public R<List<LedgerOrderLogEntity>> logs(@PathVariable("orderId") Long orderId) {
		QueryWrapper<LedgerOrderLogEntity> qw = new QueryWrapper<>();
		qw.eq("order_kind", LedgerConstants.ORDER_KIND_PATROL).eq("order_id", orderId).orderByAsc("id");
		return R.data(orderLogService.list(qw));
	}

	@Operation(summary = "手动触发全量自动生成(到期巡访计划)")
	@PostMapping("/autoGenerate")
	@PreAuthorize("@ss.hasPermi('ledger:patrol:dispatch')")
	public R<Integer> autoGenerate() {
		return R.data(patrolAutoService.generateDuePlans());
	}

	// ================= 内部辅助 =================

	/** 批量填充隐患数量(总数/已整改数)，避免列表逐行查询 */
	private void fillRiskCounts(List<LedgerPatrolOrderVO> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		List<Long> orderIds = new ArrayList<>();
		for (LedgerPatrolOrderVO vo : list) {
			if (vo.getId() != null) {
				orderIds.add(vo.getId());
			}
		}
		if (orderIds.isEmpty()) {
			return;
		}
		QueryWrapper<LedgerPatrolRiskItemEntity> qw = new QueryWrapper<>();
		qw.select("order_id AS oid", "COUNT(*) AS total", "SUM(is_fixed = 1) AS fixed")
				.in("order_id", orderIds).groupBy("order_id");
		Map<Long, int[]> counts = new HashMap<>();
		for (Map<String, Object> row : riskItemService.listMaps(qw)) {
			Object oid = row.get("oid");
			if (!(oid instanceof Number)) {
				continue;
			}
			Object total = row.get("total");
			Object fixed = row.get("fixed");
			counts.put(((Number) oid).longValue(), new int[] {
					total instanceof Number ? ((Number) total).intValue() : 0,
					fixed instanceof Number ? ((Number) fixed).intValue() : 0 });
		}
		for (LedgerPatrolOrderVO vo : list) {
			int[] c = counts.get(vo.getId());
			vo.setRiskItemCount(c == null ? 0 : c[0]);
			vo.setRiskFixedCount(c == null ? 0 : c[1]);
		}
	}

	private LedgerPatrolOrderEntity loadOwn(Long orderId) {
		LedgerPatrolOrderEntity order = patrolService.getById(orderId);
		return order;
	}

	private String nextOrderNo(Date date) {
		String prefix = "XF" + new SimpleDateFormat("yyyyMMdd").format(date);
		QueryWrapper<LedgerPatrolOrderEntity> cq = new QueryWrapper<>();
		cq.likeRight("order_no", prefix);
		long cnt = patrolService.count(cq);
		return prefix + String.format("%04d", cnt + 1);
	}

	private Date parseDateTime(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
		} catch (Exception e) {
			return null;
		}
	}

	private void addLog(LedgerPatrolOrderEntity order, String action, Integer fromStatus, Integer toStatus,
			Long fromEmployeeId, Long toEmployeeId, String content) {
		LedgerOrderLogEntity log = new LedgerOrderLogEntity();
		log.setOrderKind(LedgerConstants.ORDER_KIND_PATROL);
		log.setOrderId(order.getId());
		log.setOrderNo(order.getOrderNo());
		log.setAction(action);
		log.setFromStatus(fromStatus);
		log.setToStatus(toStatus);
		log.setFromEmployeeId(fromEmployeeId);
		log.setToEmployeeId(toEmployeeId);
		log.setContent(content);
		log.setCreateBy(getUsername());
		log.setCreateAt(new Date());
		orderLogService.save(log);
	}
}
