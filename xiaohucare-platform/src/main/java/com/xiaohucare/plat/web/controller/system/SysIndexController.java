package com.xiaohucare.plat.web.controller.system;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.xiaohucare.common.config.XiaohucareConfig;
import com.xiaohucare.common.utils.StringUtils;

/**
 * 首页
 * 
 * @author xiaohucare
 */
@Controller
public class SysIndexController {
	/** 系统基础配置 */
	@Autowired
	private XiaohucareConfig xiaohucareConfig;

	/**
	 * 访问首页，提示语
	 */
//	@RequestMapping("/")
//	public String index() {
//		return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", xiaohucareConfig.getName(), xiaohucareConfig.getVersion());
//	}
	@GetMapping(value = { "/", "/index.html", "/index" })
	public String execute(Model model, HttpServletRequest request) {
		
		// 当前sessionId
		model.addAttribute("sessionId", request.getSession().getId());
		// 单点退出地址
		model.addAttribute("tips", StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", xiaohucareConfig.getName(),
				xiaohucareConfig.getVersion()));
		return "index";
	}
}
