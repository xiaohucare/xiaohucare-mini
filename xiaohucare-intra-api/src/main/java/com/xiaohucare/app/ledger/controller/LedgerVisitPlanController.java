package com.xiaohucare.app.ledger.controller;

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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.ledger.entity.LedgerVisitPlanEntity;
import com.xiaohucare.app.ledger.service.LedgerPatrolAutoService;
import com.xiaohucare.app.ledger.service.LedgerVisitAutoService;
import com.xiaohucare.app.ledger.service.LedgerVisitPlanService;
import com.xiaohucare.app.ledger.vo.LedgerVisitPlanVO;
import com.xiaohucare.app.ledger.wrapper.LedgerVisitPlanWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * 探访/巡访计划配置
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Tag(name = "台账-巡访计划")
@RestController
@RequestMapping("/intra/care/ledger/plan")
public class LedgerVisitPlanController extends BaseController {

	@Autowired
	private LedgerVisitPlanService planService;
	@Autowired
	private LedgerVisitAutoService visitAutoService;
	@Autowired
	private LedgerPatrolAutoService patrolAutoService;

	@Operation(summary = "列表")
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('ledger:plan:list')")
	public R<VPage<LedgerVisitPlanVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerVisitPlanEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("planName"), LedgerVisitPlanEntity.class);
		
		queryWrapper.like(StringUtils.isNotNull(params.get("planName")), "plan_name", params.get("planName"));
		queryWrapper.orderByDesc("id");
		IPage<LedgerVisitPlanEntity> pages = planService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(LedgerVisitPlanWrapper.build().toVPageVO(pages));
	}

	@Operation(summary = "详情")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('ledger:plan:query')")
	public R<LedgerVisitPlanVO> detail(@PathVariable("id") Long id) {
		return R.data(LedgerVisitPlanWrapper.build().entityVO(planService.getById(id)));
	}

	@Operation(summary = "保存&更新")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('ledger:plan:add')")
	public R<LedgerVisitPlanVO> submit(@Valid @RequestBody LedgerVisitPlanVO plan) {
		if (plan.getId() == null) {
			plan.setCreateAt(new Date());
			plan.setCreateBy(getUsername());
			if (plan.getState() == null) {
				plan.setState(1);
			}
			// 新建且未设下次执行时间则视为立即可执行,交给定时/手动触发
			if (plan.getNextGenTime() == null) {
				plan.setNextGenTime(new Date());
			}
		}
		plan.setUpdateAt(new Date());
		plan.setUpdateBy(getUsername());
		if (planService.saveOrUpdate(plan)) {
			return R.ok(plan);
		}
		return R.error("保存失败");
	}

	@Operation(summary = "删除")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('ledger:plan:remove')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		return R.ok(planService.removeByIds(Arrays.asList(ids)));
	}

	@Operation(summary = "立即执行单个计划(手动触发生成工单)")
	@PostMapping("/generate/{id}")
	@PreAuthorize("@ss.hasPermi('ledger:plan:edit')")
	public R<Integer> generate(@PathVariable("id") Long id) {
		LedgerVisitPlanEntity plan = planService.getById(id);
		if (plan == null) {
			return R.error("计划不存在");
		}
		// 按计划类型分流：2=巡访计划生成巡访工单，其余为探访计划
		if (plan.getPlanType() != null && plan.getPlanType() == 2) {
			return R.data(patrolAutoService.generateByPlan(plan));
		}
		return R.data(visitAutoService.generateByPlan(plan));
	}

	@Operation(summary = "查询本机构计划列表(下拉)")
	@GetMapping("/options")
	@PreAuthorize("@ss.hasPermi('ledger:plan:list')")
	public R<List<LedgerVisitPlanVO>> options() {
		QueryWrapper<LedgerVisitPlanEntity> qw = new QueryWrapper<>();
		qw.eq("state", 1).orderByDesc("id");
		return R.data(LedgerVisitPlanWrapper.build().listVO(planService.list(qw)));
	}
}
