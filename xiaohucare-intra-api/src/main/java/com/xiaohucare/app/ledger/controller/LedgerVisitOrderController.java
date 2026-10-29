package com.xiaohucare.app.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.xiaohucare.app.ledger.entity.LedgerVisitOrderEntity;
import com.xiaohucare.app.ledger.entity.LedgerVisitRecordEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.service.LedgerOrderLogService;
import com.xiaohucare.app.ledger.service.LedgerVisitAutoService;
import com.xiaohucare.app.ledger.service.LedgerVisitOrderService;
import com.xiaohucare.app.ledger.service.LedgerVisitRecordService;
import com.xiaohucare.app.ledger.vo.LedgerVisitOrderVO;
import com.xiaohucare.app.ledger.vo.LedgerVisitRecordVO;
import com.xiaohucare.app.ledger.wrapper.LedgerVisitOrderWrapper;
import com.xiaohucare.app.ledger.wrapper.LedgerVisitRecordWrapper;
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
 * 探访工单(关爱探访)：CRUD + 工单流转 + 探访记录 + 自动化触发
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Tag(name = "台账-探访工单")
@RestController
@RequestMapping("/intra/care/ledger/visit")
public class LedgerVisitOrderController extends BaseController {

	@Autowired
	private LedgerVisitOrderService orderService;
	@Autowired
	private LedgerVisitRecordService recordService;
	@Autowired
	private LedgerOrderLogService orderLogService;
	@Autowired
	private LedgerElderService elderService;
	@Autowired
	private LedgerVisitAutoService visitAutoService;

	@Operation(summary = "列表")
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('ledger:visit:list')")
	public R<VPage<LedgerVisitOrderVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerVisitOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("orderNo", "elderName", "employeeName"), LedgerVisitOrderEntity.class);
		
		queryWrapper.eq(StringUtils.isNotNull(params.get("orderStatus")), "order_status", params.get("orderStatus"));
		queryWrapper.eq(StringUtils.isNotNull(params.get("employeeId")), "employee_id", params.get("employeeId"));
		queryWrapper.like(StringUtils.isNotNull(params.get("orderNo")), "order_no", params.get("orderNo"));
		queryWrapper.like(StringUtils.isNotNull(params.get("elderName")), "elder_name", params.get("elderName"));
		queryWrapper.like(StringUtils.isNotNull(params.get("employeeName")), "employee_name", params.get("employeeName"));
		queryWrapper.orderByDesc("id");
		IPage<LedgerVisitOrderEntity> pages = orderService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(LedgerVisitOrderWrapper.build().toVPageVO(pages));
	}

	@Operation(summary = "详情")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('ledger:visit:query')")
	public R<LedgerVisitOrderVO> detail(@PathVariable("id") Long id) {
		return R.data(LedgerVisitOrderWrapper.build().entityVO(orderService.getById(id)));
	}

	@Operation(summary = "保存&更新(人工新建/编辑工单)")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('ledger:visit:add')")
	public R<LedgerVisitOrderVO> submit(@Valid @RequestBody LedgerVisitOrderVO order) {
		// 首次使用本机构台账时,自动初始化机构可自定义字典
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
					order.setStoreName(elder.getStoreName());
				}
			}
			order.setOrderStatus(order.getEmployeeId() != null ? LedgerConstants.VISIT_STATUS_WAIT_VISIT
					: LedgerConstants.VISIT_STATUS_WAIT_ASSIGN);
			order.setCreateAt(new Date());
			order.setCreateBy(getUsername());
		}
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		if (orderService.saveOrUpdate(order)) {
			return R.ok(order);
		}
		return R.error("保存失败");
	}

	@Operation(summary = "删除")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('ledger:visit:remove')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		return R.ok(orderService.removeByIds(Arrays.asList(ids)));
	}

	@Operation(summary = "导出")
	@PreAuthorize("@ss.hasPermi('ledger:visit:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerVisitOrderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("orderNo", "elderName"), LedgerVisitOrderEntity.class);
		
		queryWrapper.orderByDesc("id");
		List<LedgerVisitOrderEntity> list = orderService.list(queryWrapper);
		ExcelUtil<LedgerVisitOrderEntity> util = new ExcelUtil<>(LedgerVisitOrderEntity.class);
		util.exportExcel(response, list, "探访工单");
	}

	// ================= 工单流转 =================

	@Operation(summary = "分派/指派巡访员")
	@PostMapping("/dispatch")
	@PreAuthorize("@ss.hasPermi('ledger:visit:dispatch')")
	public R<Boolean> dispatch(@RequestParam Long orderId, @RequestParam Long employeeId,
			@RequestParam(required = false) String employeeName) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		order.setEmployeeId(employeeId);
		order.setEmployeeName(employeeName);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_WAIT_VISIT);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_ASSIGN, from, order.getOrderStatus(), null, employeeId, "分派巡访员：" + employeeName);
		return R.ok(true);
	}

	@Operation(summary = "转派")
	@PostMapping("/transfer")
	@PreAuthorize("@ss.hasPermi('ledger:visit:dispatch')")
	public R<Boolean> transfer(@RequestParam Long orderId, @RequestParam Long employeeId,
			@RequestParam(required = false) String employeeName) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Long fromEmp = order.getEmployeeId();
		order.setEmployeeId(employeeId);
		order.setEmployeeName(employeeName);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_TRANSFER, order.getOrderStatus(), order.getOrderStatus(), fromEmp, employeeId,
				"转派给：" + employeeName);
		return R.ok(true);
	}

	@Operation(summary = "撤回(取消指派,回到待分配)")
	@PostMapping("/revoke")
	@PreAuthorize("@ss.hasPermi('ledger:visit:dispatch')")
	public R<Boolean> revoke(@RequestParam Long orderId) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		final Integer from = order.getOrderStatus();
		Long fromEmp = order.getEmployeeId();
		Date now = new Date();
		// updateById 会忽略 null 字段,清空指派人需用 UpdateWrapper 显式 set null
		UpdateWrapper<LedgerVisitOrderEntity> uw = new UpdateWrapper<>();
		uw.eq("id", orderId)
				.set("employee_id", null)
				.set("employee_name", null)
				.set("order_status", LedgerConstants.VISIT_STATUS_WAIT_ASSIGN)
				.set("update_at", now)
				.set("update_by", getUsername());
		orderService.update(uw);
		order.setEmployeeId(null);
		order.setEmployeeName(null);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_WAIT_ASSIGN);
		addLog(order, LedgerConstants.ACTION_REVOKE, from, order.getOrderStatus(), fromEmp, null, "撤回指派");
		return R.ok(true);
	}

	@Operation(summary = "上门签到")
	@PostMapping("/sign")
	@PreAuthorize("@ss.hasPermi('ledger:visit:execute')")
	public R<Boolean> sign(@RequestParam Long orderId, @RequestParam(required = false) String signLocation) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Date now = new Date();
		order.setSignTime(now);
		order.setSignLocation(signLocation);
		order.setVisitStartTime(now);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_WAIT_VISIT);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_SIGN, order.getOrderStatus(), order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "上门签到：" + signLocation);
		return R.ok(true);
	}

	@Operation(summary = "完成探访")
	@PostMapping("/finish")
	@PreAuthorize("@ss.hasPermi('ledger:visit:execute')")
	public R<Boolean> finish(@RequestParam Long orderId) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		Date now = new Date();
		order.setVisitEndTime(now);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_FINISHED);
		order.setUpdateAt(now);
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_FINISH, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "完成探访");
		return R.ok(true);
	}

	@Operation(summary = "延期")
	@PostMapping("/delay")
	@PreAuthorize("@ss.hasPermi('ledger:visit:execute')")
	public R<Boolean> delay(@RequestParam Long orderId, @RequestParam(required = false) String delayReason) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		order.setDelayReason(delayReason);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_DELAY);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_DELAY, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "延期：" + delayReason);
		return R.ok(true);
	}

	@Operation(summary = "取消")
	@PostMapping("/cancel")
	@PreAuthorize("@ss.hasPermi('ledger:visit:execute')")
	public R<Boolean> cancel(@RequestParam Long orderId, @RequestParam(required = false) String cancelReason) {
		LedgerVisitOrderEntity order = loadOwn(orderId);
		if (order == null) {
			return R.error("工单不存在");
		}
		Integer from = order.getOrderStatus();
		order.setCancelReason(cancelReason);
		order.setOrderStatus(LedgerConstants.VISIT_STATUS_CANCEL);
		order.setUpdateAt(new Date());
		order.setUpdateBy(getUsername());
		orderService.updateById(order);
		addLog(order, LedgerConstants.ACTION_CANCEL, from, order.getOrderStatus(), order.getEmployeeId(),
				order.getEmployeeId(), "取消：" + cancelReason);
		return R.ok(true);
	}

	// ================= 探访记录 =================

	@Operation(summary = "按工单查询探访记录")
	@GetMapping("/record/{orderId}")
	@PreAuthorize("@ss.hasPermi('ledger:visit:query')")
	public R<LedgerVisitRecordVO> getRecord(@PathVariable("orderId") Long orderId) {
		QueryWrapper<LedgerVisitRecordEntity> qw = new QueryWrapper<>();
		qw.eq("order_id", orderId).orderByDesc("id").last("limit 1");
		LedgerVisitRecordEntity record = recordService.getOne(qw);
		return R.data(LedgerVisitRecordWrapper.build().entityVO(record));
	}

	@Operation(summary = "提交探访记录(finish=true 同时完工单)")
	@PostMapping("/record/submit")
	@PreAuthorize("@ss.hasPermi('ledger:visit:execute')")
	public R<LedgerVisitRecordVO> submitRecord(@Valid @RequestBody LedgerVisitRecordVO record,
			@RequestParam(required = false, defaultValue = "false") boolean finish) {
		LedgerVisitOrderEntity order = loadOwn(record.getOrderId());
		if (order == null) {
			return R.error("工单不存在");
		}
		Date now = new Date();
		record.setElderId(order.getElderId());
		record.setElderName(order.getElderName());
		if (record.getId() == null) {
			record.setCreateBy(getUsername());
			record.setCreateAt(now);
		}
		record.setUpdateBy(getUsername());
		record.setUpdateAt(now);
		if (record.getRealVisitTime() == null) {
			record.setRealVisitTime(now);
		}
		if (!recordService.saveOrUpdate(record)) {
			return R.error("保存失败");
		}
		if (finish) {
			order.setVisitEndTime(now);
			order.setOrderStatus(LedgerConstants.VISIT_STATUS_FINISHED);
			order.setUpdateAt(now);
			order.setUpdateBy(getUsername());
			orderService.updateById(order);
			addLog(order, LedgerConstants.ACTION_FINISH, null, order.getOrderStatus(), order.getEmployeeId(),
					order.getEmployeeId(), "提交探访记录并完成");
		}
		return R.ok(record);
	}

	// ================= 流转日志 & 自动化 =================

	@Operation(summary = "查询工单流转日志")
	@GetMapping("/logs/{orderId}")
	@PreAuthorize("@ss.hasPermi('ledger:visit:query')")
	public R<List<LedgerOrderLogEntity>> logs(@PathVariable("orderId") Long orderId) {
		QueryWrapper<LedgerOrderLogEntity> qw = new QueryWrapper<>();
		qw.eq("order_kind", LedgerConstants.ORDER_KIND_VISIT).eq("order_id", orderId).orderByAsc("id");
		return R.data(orderLogService.list(qw));
	}

	@Operation(summary = "手动触发全量自动生成(到期计划)")
	@PostMapping("/autoGenerate")
	@PreAuthorize("@ss.hasPermi('ledger:visit:dispatch')")
	public R<Integer> autoGenerate() {
		return R.data(visitAutoService.generateDuePlans());
	}

	// ================= 内部辅助 =================

	private LedgerVisitOrderEntity loadOwn(Long orderId) {
		LedgerVisitOrderEntity order = orderService.getById(orderId);
		return order;
	}

	private String nextOrderNo(Date date) {
		String prefix = "TF" + new SimpleDateFormat("yyyyMMdd").format(date);
		QueryWrapper<LedgerVisitOrderEntity> cq = new QueryWrapper<>();
		cq.likeRight("order_no", prefix);
		long cnt = orderService.count(cq);
		return prefix + String.format("%04d", cnt + 1);
	}

	private void addLog(LedgerVisitOrderEntity order, String action, Integer fromStatus, Integer toStatus,
			Long fromEmployeeId, Long toEmployeeId, String content) {
		LedgerOrderLogEntity log = new LedgerOrderLogEntity();
		log.setOrderKind(LedgerConstants.ORDER_KIND_VISIT);
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
