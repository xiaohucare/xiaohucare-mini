package com.xiaohucare.framework.security.handle;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson2.JSON;
import com.xiaohucare.common.constant.Constants;
import com.xiaohucare.common.utils.MessageUtils;
import com.xiaohucare.common.utils.ServletUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.core.domain.model.LoginUser;
import com.xiaohucare.framework.manager.AsyncManager;
import com.xiaohucare.framework.manager.factory.AsyncFactory;
import com.xiaohucare.framework.web.service.TokenService;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author xiaohucare
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
