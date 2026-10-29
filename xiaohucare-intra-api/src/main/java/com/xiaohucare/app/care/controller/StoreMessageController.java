package com.xiaohucare.app.care.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.xiaohucare.app.care.entity.StoreMessageEntity;
import com.xiaohucare.app.care.entity.StoreMessageTipsEntity;
import com.xiaohucare.app.care.service.StoreMessageService;
import com.xiaohucare.app.care.service.StoreMessageTipsService;
import com.xiaohucare.app.care.vo.StoreMessageTipsVO;
import com.xiaohucare.app.care.vo.StoreMessageVO;
import com.xiaohucare.app.care.wrapper.StoreMessageTipsWrapper;
import com.xiaohucare.app.care.wrapper.StoreMessageWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 门店消息(店铺通知)
 * 
 * @author xiaohucare
 * @date 2026-10-26
 */
@Slf4j
@RestController
@RequestMapping("intra/care/storemessage")
public class StoreMessageController extends BaseController {

	@Autowired
	private StoreMessageService storeMessageService;

	@Autowired
	private StoreMessageTipsService storeMessageTipsService;

	/**
	 * 列表
	 */
	@Operation(summary = "列表")
	@Parameters({
			@Parameter(name = "title", description = "标题"),
			@Parameter(name = "tipsType", description = "分类: 0=系统 1=活动 2=交易"),
			@Parameter(name = "state", description = "状态: 0=待处理 1=已处理 2=已关闭")
	})
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('care:storemessage:list')")
	public R<VPage<StoreMessageVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<StoreMessageEntity> queryWrapper = new QueryWrapper<>();
		

		if (params.containsKey("title") && StringUtils.isNotBlank(params.get("title").toString())) {
			queryWrapper.like("title", params.get("title"));
		}
		if (params.containsKey("tipsType") && StringUtils.isNotBlank(params.get("tipsType").toString())) {
			queryWrapper.eq("tips_type", params.get("tipsType"));
		}
		if (params.containsKey("state") && StringUtils.isNotBlank(params.get("state").toString())) {
			queryWrapper.eq("state", params.get("state"));
		}

		queryWrapper.orderByAsc("sequence");
		queryWrapper.orderByDesc("create_at");

		IPage<StoreMessageEntity> pages = storeMessageService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(StoreMessageWrapper.build().toVPageVO(pages));
	}

	/**
	 * 统计(当前门店各状态数量)
	 */
	@Operation(summary = "统计")
	@GetMapping("/listStat")
	@PreAuthorize("@ss.hasPermi('care:storemessage:list')")
	public R<Map<String, Object>> listStat() {
		Map<String, Object> stat = new LinkedHashMap<>();
		stat.put("total", countByState(null));
		stat.put("pending", countByState(0));
		stat.put("handled", countByState(1));
		stat.put("closed", countByState(2));
		return R.data(stat);
	}

	/**
	 * 未读提示列表
	 */
	@Operation(summary = "未读提示列表")
	@GetMapping("/tips/browse")
	@PreAuthorize("@ss.hasPermi('care:storemessage:list')")
	public R<List<StoreMessageTipsVO>> tipsBrowse() {
		QueryWrapper<StoreMessageTipsEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("tips_type");
		List<StoreMessageTipsEntity> list = storeMessageTipsService.list(queryWrapper);
		return R.data(StoreMessageTipsWrapper.build().listVO(list));
	}

	/**
	 * 全部已读(按分类清零未读数, tipsType 为空则清零全部分类)
	 */
	@Operation(summary = "全部已读")
	@Log(title = "门店消息", businessType = BusinessType.UPDATE)
	@PostMapping("/tips/read")
	@PreAuthorize("@ss.hasPermi('care:storemessage:edit')")
	public R<Boolean> readTips(@RequestBody Map<String, Object> params) {
		try {
			Integer tipsType = null;
			if (params != null && params.get("tipsType") != null && StringUtils.isNotBlank(params.get("tipsType").toString())) {
				tipsType = Integer.valueOf(params.get("tipsType").toString());
			}
			return R.ok(storeMessageTipsService.resetUnread( tipsType));
		} catch (Exception e) {
			log.error("门店消息标记已读失败", e);
			return R.error("操作失败：" + e.getMessage());
		}
	}

	/**
	 * 信息
	 */
	@Operation(summary = "信息")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('care:storemessage:query')")
	public R<StoreMessageVO> detail(@PathVariable("id") Long id) {
		StoreMessageEntity storeMessage = storeMessageService.getById(id);
		if (storeMessage != null) {
			// 浏览详情时累加浏览次数
			storeMessageService.increaseViewCount(id);
			storeMessage.setViewCount((storeMessage.getViewCount() == null ? 0 : storeMessage.getViewCount()) + 1);
		}
		return R.data(StoreMessageWrapper.build().entityVO(storeMessage));
	}

	/**
	 * 保存&更新
	 */
	@Operation(summary = "保存&更新")
	@Log(title = "门店消息", businessType = BusinessType.INSERT)
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('care:storemessage:submit')")
	public R<Boolean> submit(@Valid @RequestBody StoreMessageVO storeMessage) {
		try {
			Date now = new Date();
			if (storeMessage.getId() == null) {
				storeMessage.setCreateAt(now);
				storeMessage.setUpdateAt(now);
				storeMessage.setIsDelete(0);
				if (storeMessage.getViewCount() == null) {
					storeMessage.setViewCount(0);
				}
				if (storeMessage.getReplyCount() == null) {
					storeMessage.setReplyCount(0);
				}
				if (storeMessage.getWorth() == null) {
					storeMessage.setWorth(0);
				}
				if (storeMessage.getTipsType() == null) {
					storeMessage.setTipsType(0);
				}
				if (storeMessage.getState() == null) {
					storeMessage.setState(0);
				}
				boolean saved = storeMessageService.save(storeMessage);
				if (saved) {
					// 新消息刷新对应分类的未读提示
					storeMessageTipsService.refreshOnNewNotice(storeMessage.getTipsType(),
							storeMessage.getId(), storeMessage.getTitle(), storeMessage.getContent());
					return R.ok(true);
				}
				return R.error("数据保存失败");
			} else {
				storeMessage.setUpdateAt(now);
				if (storeMessageService.updateById(storeMessage)) {
					return R.ok(true);
				}
				return R.error("数据更新失败");
			}
		} catch (Exception e) {
			log.error("保存门店消息失败", e);
			return R.error("数据保存失败：" + e.getMessage());
		}
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@Log(title = "门店消息", businessType = BusinessType.DELETE)
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('care:storemessage:delete')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		try {
			return R.ok(storeMessageService.removeByIds(Arrays.asList(ids)));
		} catch (Exception e) {
			log.error("删除门店消息失败", e);
			return R.error("删除失败：" + e.getMessage());
		}
	}

	/**
	 * 更新状态: 0=待处理 1=已处理 2=已关闭
	 */
	@Operation(summary = "更新状态")
	@Log(title = "门店消息", businessType = BusinessType.UPDATE)
	@PostMapping("/updateState")
	@PreAuthorize("@ss.hasPermi('care:storemessage:edit')")
	public R<Boolean> updateState(@RequestBody Map<String, Object> params) {
		try {
			Long id = Long.valueOf(params.get("id").toString());
			Integer state = Integer.valueOf(params.get("state").toString());

			StoreMessageEntity message = storeMessageService.getById(id);
			if (message == null) {
				return R.error("消息不存在");
			}
			message.setState(state);
			message.setUpdateAt(new Date());

			if (storeMessageService.updateById(message)) {
				return R.ok(true);
			}
			return R.error("状态更新失败");
		} catch (Exception e) {
			log.error("更新门店消息状态失败", e);
			return R.error("状态更新失败：" + e.getMessage());
		}
	}

	/**
	 * 导出
	 */
	@Operation(summary = "导出")
	@Log(title = "门店消息", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:storemessage:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<StoreMessageEntity> queryWrapper = new QueryWrapper<>();
		
		if (params.containsKey("title") && StringUtils.isNotBlank(params.get("title").toString())) {
			queryWrapper.like("title", params.get("title"));
		}
		if (params.containsKey("tipsType") && StringUtils.isNotBlank(params.get("tipsType").toString())) {
			queryWrapper.eq("tips_type", params.get("tipsType"));
		}
		if (params.containsKey("state") && StringUtils.isNotBlank(params.get("state").toString())) {
			queryWrapper.eq("state", params.get("state"));
		}
		queryWrapper.orderByAsc("sequence").orderByDesc("create_at");

		List<StoreMessageEntity> list = storeMessageService.list(queryWrapper);
		ExcelUtil<StoreMessageEntity> util = new ExcelUtil<>(StoreMessageEntity.class);
		util.exportExcel(response, list, "门店消息数据");
	}

	/**
	 * 按状态统计当前门店消息数量, state 为空则统计全部
	 */
	private long countByState(Integer state) {
		QueryWrapper<StoreMessageEntity> queryWrapper = new QueryWrapper<>();
		if (state != null) {
			queryWrapper.eq("state", state);
		}
		return storeMessageService.count(queryWrapper);
	}

}
