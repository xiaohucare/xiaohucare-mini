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
import com.xiaohucare.app.ledger.entity.LedgerElderEntity;
import com.xiaohucare.app.ledger.service.LedgerElderService;
import com.xiaohucare.app.ledger.vo.LedgerElderVO;
import com.xiaohucare.app.ledger.wrapper.LedgerElderWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
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
 * 智慧台账-老人档案
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
@Tag(name = "台账-老人档案")
@RestController
@RequestMapping("/intra/care/ledger/elder")
public class LedgerElderController extends BaseController {

	@Autowired
	private LedgerElderService ledgerElderService;

	@Operation(summary = "列表")
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('ledger:elder:list')")
	public R<VPage<LedgerElderVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerElderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("name"), LedgerElderEntity.class);
		
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		queryWrapper.like(StringUtils.isNotNull(params.get("elderType")), "elder_types", params.get("elderType"));
		queryWrapper.orderByDesc("id");
		IPage<LedgerElderEntity> pages = ledgerElderService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(LedgerElderWrapper.build().toVPageVO(pages));
	}

	@Operation(summary = "详情")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('ledger:elder:query')")
	public R<LedgerElderVO> detail(@PathVariable("id") Long id) {
		LedgerElderEntity elder = ledgerElderService.getById(id);
		return R.data(LedgerElderWrapper.build().entityVO(elder));
	}

	@Operation(summary = "保存&更新")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('ledger:elder:add')")
	public R<LedgerElderVO> submit(@Valid @RequestBody LedgerElderVO elder) {
		if (elder.getId() == null) {
			elder.setCreateAt(new Date());
			elder.setCreateBy(getUsername());
		}
		elder.setUpdateAt(new Date());
		elder.setUpdateBy(getUsername());
		if (ledgerElderService.saveOrUpdate(elder)) {
			return R.ok(elder);
		}
		return R.error("保存失败");
	}

	@Operation(summary = "删除")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('ledger:elder:remove')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		return R.ok(ledgerElderService.removeByIds(Arrays.asList(ids)));
	}

	@Operation(summary = "导出")
	@Log(title = "老人台账", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('ledger:elder:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<LedgerElderEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("name"), LedgerElderEntity.class);
		
		queryWrapper.orderByDesc("id");
		List<LedgerElderEntity> list = ledgerElderService.list(queryWrapper);
		ExcelUtil<LedgerElderEntity> util = new ExcelUtil<>(LedgerElderEntity.class);
		util.exportExcel(response, list, "老人台账");
	}
}
