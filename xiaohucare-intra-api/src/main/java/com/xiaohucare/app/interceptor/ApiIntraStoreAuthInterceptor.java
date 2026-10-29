package com.xiaohucare.app.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.xiaohucare.common.extra.utils.JsonUtil;
import com.xiaohucare.framework.core.api.utils.ApiThreadContext;
import com.xiaohucare.framework.core.api.vo.ApiHeaderVo;
import com.xiaohucare.framework.core.utils.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class ApiIntraStoreAuthInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	  ApiThreadContext.init();
          ApiHeaderVo apiHeaderVo=ApiThreadContext.getApiHeader();
          if(apiHeaderVo==null) {
          	apiHeaderVo=new ApiHeaderVo();
          }
          ApiThreadContext.putApiHeader(apiHeaderVo);
          log.info("-----------ApiIntraStoreAuthInterceptor-----------本次请求相关信息----apiHeaderVo-----------"+JsonUtil.getJsonByObj(apiHeaderVo));
          return true;
    }
}
