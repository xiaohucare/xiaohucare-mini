package com.xiaohucare.plat.web.controller.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohucare.common.constant.CacheConstants;
import com.xiaohucare.common.core.page.TableDataInfo;
import com.xiaohucare.common.core.redis.RedisUtil;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.domain.model.LoginUser;
import com.xiaohucare.framework.system.domain.SysUserOnline;
import com.xiaohucare.framework.system.service.ISysUserOnlineService;

import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
/**
 * 在线用户监控
 * 
 * @author xiaohucare
 */
@Slf4j
@Tag(name = "在线用户监控")
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    @Autowired
    private ISysUserOnlineService userOnlineService;

   @Autowired
	private RedisUtil redisUtil;
    @Operation(summary = "获取在线用户列表")
    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName)
    {
       Set<Object> keys = redisUtil.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
		log.info(JSONUtil.toJsonStr(keys));
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (Object key : keys)
        {
            try {
            Object userJson = redisUtil.getNoPre(key.toString());
				if (userJson == null) {
					continue;
				}
			LoginUser user = JSONUtil.toBean(userJson.toString(), LoginUser.class);
			
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName))
            {
                userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
            }
            else if (StringUtils.isNotEmpty(ipaddr))
            {
                userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
            }
            else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser()))
            {
                userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
            }
            else
            {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
            } catch (Exception e) {
				log.error("数据转化错误：" + key.toString());
				Object sss = redisUtil.getNoPre(key.toString());
				if (JSONUtil.isJson(sss.toString())) {
					log.error("数据数据：" + JSONUtil.toJsonStr(sss));
				} else {
					log.error("数据数据：" + sss);
				}
			}
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId)
    {
       redisUtil.del(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }
}
