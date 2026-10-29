package com.xiaohucare.app.care.controller;

import java.util.Arrays;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.ResidentContractEntity;
import com.xiaohucare.app.care.service.ResidentContractService;
import com.xiaohucare.app.care.vo.ResidentContractVO;
import com.xiaohucare.app.care.wrapper.ResidentContractWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 合同管理（合同 + 模版）
 *
 * 合同与模版共用 e_care_resident_contract：模版行 contract_type=100 且 contract_no 以 TP- 开头。
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
@Tag(name = "合同管理")
@RestController
@RequestMapping("intra/care/contract")
public class ResidentContractController extends BaseController {

	@Autowired
	private ResidentContractService residentContractService;

	/**
	 * 合同列表（自动排除模版行）
	 */
	@Operation(summary = "合同列表")
	@Parameters({
			@Parameter(name = "contractNo", description = "合同编号"),
			@Parameter(name = "residentNo", description = "长者编号"),
			@Parameter(name = "residentName", description = "长者姓名"),
			@Parameter(name = "residentId", description = "长者ID，长者档案页跳转关联合同时按ID精确筛选"),
			@Parameter(name = "status", description = "状态 0待签署 1已签署 2已完成 3已作废")
	})
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('care:contract:list')")
	public R<VPage<ResidentContractVO>> browse(@RequestParam Map<String, Object> params) {
		Map<String, Object> query = cleanParams(params);
		QueryWrapper<ResidentContractEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(query,
				Arrays.asList("contractNo", "residentNo", "residentName"), ResidentContractEntity.class);
		// 模版行不进入合同列表
		  
		queryWrapper.notLikeRight("contract_no", ResidentContractEntity.NO_PREFIX_TEMPLATE);
		queryWrapper.like(StringUtils.isNotNull(query.get("contractNo")), "contract_no", query.get("contractNo"));
		queryWrapper.like(StringUtils.isNotNull(query.get("residentNo")), "resident_no", query.get("residentNo"));
		queryWrapper.like(StringUtils.isNotNull(query.get("residentName")), "resident_name", query.get("residentName"));
		queryWrapper.orderByDesc("id");
		IPage<ResidentContractEntity> pages = residentContractService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(ResidentContractWrapper.build().toVPageVO(pages));
	}

	/**
	 * 合同详情
	 */
	@Operation(summary = "合同详情")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('care:contract:list')")
	public R<ResidentContractVO> detail(@PathVariable("id") Long id) {
		return R.data(ResidentContractWrapper.build().entityVO(residentContractService.getById(id)));
	}

	/**
	 * 保存 / 更新合同
	 */
	@Operation(summary = "保存合同")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('care:contract:submit')")
	public R<ResidentContractVO> submit(@RequestBody ResidentContractVO contract) {
		ResidentContractVO result = residentContractService.submitContract(contract);
		return result != null ? R.ok(result) : R.error("保存失败");
	}

	/**
	 * 签署合同
	 */
	@Operation(summary = "签署合同")
	@PostMapping("/sign")
	@PreAuthorize("@ss.hasPermi('care:contract:sign')")
	public R<Boolean> sign(@RequestBody Map<String, Object> params) {
		if (params.get("id") == null) {
			return R.error("缺少合同ID");
		}
		Long id = Long.valueOf(params.get("id").toString());
		String signDate = params.get("signDate") != null ? params.get("signDate").toString() : null;
		Date date = StrUtil.isNotBlank(signDate) ? DateUtil.parse(signDate) : new Date();
		String remark = params.get("remark") != null ? params.get("remark").toString() : null;
		Integer signType = params.get("signType") != null ? Integer.valueOf(params.get("signType").toString()) : null;
		return residentContractService.signContract(id, date, remark, signType) ? R.ok(true) : R.error("签署失败，合同不存在或已作废");
	}

	/**
	 * 删除合同（逻辑删除）
	 */
	@Operation(summary = "删除合同")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('care:contract:delete')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		return R.ok(residentContractService.removeByIds(Arrays.asList(ids)));
	}

	/**
	 * 模版列表
	 */
	@Operation(summary = "模版列表")
	@Parameters({
			@Parameter(name = "templateName", description = "模版名称"),
			@Parameter(name = "status", description = "状态 1启用 0停用")
	})
	@GetMapping("/templates")
	@PreAuthorize("@ss.hasPermi('care:contract:template')")
	public R<List<ResidentContractVO>> templates(@RequestParam Map<String, Object> params) {
		String templateName = params.get("templateName") != null ? params.get("templateName").toString() : null;
		if (StrUtil.isBlank(templateName) && params.get("name") != null) {
			templateName = params.get("name").toString();
		}
		Object statusParam = params.get("status");
		Integer status = statusParam != null && StrUtil.isNumeric(String.valueOf(statusParam))
				? Integer.valueOf(String.valueOf(statusParam))
				: null;
		return R.data(residentContractService.listTemplates( templateName, status));
	}

	/**
	 * 保存 / 更新模版（模板归属固定为当前机构，通用模版不可修改）
	 */
	@Operation(summary = "保存模版")
	@PostMapping("/template/submit")
	@PreAuthorize("@ss.hasPermi('care:contract:template')")
	public R<ResidentContractVO> submitTemplate(@RequestBody ResidentContractVO template) {
		ResidentContractVO result = residentContractService.submitTemplate(template);
		return result != null ? R.ok(result) : R.error("保存失败");
	}

	/**
	 * 删除模版（逻辑删除，仅作用于本机构的 TP- 行）
	 */
	@Operation(summary = "删除模版")
	@PostMapping("/template/delete")
	@PreAuthorize("@ss.hasPermi('care:contract:template')")
	public R<Boolean> deleteTemplates(@RequestBody Long[] ids) {
		return R.ok(residentContractService.removeTemplates(Arrays.asList(ids)));
	}

	/**
	 * 去掉空值参数：避免下拉清空后的空串被拼成查询条件
	 */
	private Map<String, Object> cleanParams(Map<String, Object> params) {
		Map<String, Object> query = new HashMap<>(params);
		query.entrySet().removeIf(entry -> entry.getValue() == null || StrUtil.isBlank(String.valueOf(entry.getValue())));
		return query;
	}
}
