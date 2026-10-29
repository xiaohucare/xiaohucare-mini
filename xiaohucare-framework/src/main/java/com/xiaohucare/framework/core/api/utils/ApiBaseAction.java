package com.xiaohucare.framework.core.api.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.fastjson.JSONObject;
import com.xiaohucare.framework.core.api.vo.ResultMap;

public class ApiBaseAction {
    protected  Log logger = LogFactory.getLog(this.getClass());
    /**
     * 得到request对象
     */
    @Autowired
    protected HttpServletRequest request;
    /**
     * 得到response对象
     */
    @Autowired
    protected HttpServletResponse response;
    /**
     * 参数绑定异常
     *UnauthorizedException.class,
     */
    @ExceptionHandler({BindException.class, MissingServletRequestParameterException.class, TypeMismatchException.class})
    @ResponseBody
    public Map<String, Object> bindException(Exception e) {
        if (e instanceof BindException) {
            return toResponsObject(1, "参数绑定异常", e.getMessage());
        }
        return toResponsObject(1, "处理异常", e.getMessage());
    }

    public ResultMap toResponsObject(int requestCode, String msg, Object data) {
        return ResultMap.success(requestCode, msg, data);
    }

    public ResultMap toResponsSuccess(Object data) {
    	 return ResultMap.success(data);
    }

    public ResultMap toResponsMsgSuccess(String msg) {
        return toResponsObject(0, msg, "");
    }

    public ResultMap toResponsSuccessForSelect(Object data) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("list", data);
        return toResponsObject(0, "执行成功", result);
    }

    public ResultMap toResponsFail(String msg) {
        return toResponsObject(1, msg, null);
    }
    public ResultMap toResponsLoginFail(String msg) {
    	return toResponsObject(10, msg, null);
    }
    /**
     * initBinder 初始化绑定 <br>
     * 这里处理了3种类型<br>
     * 1、字符串自动 trim 去掉前后空格 <br>
     * 2、java.util.Date 转换为 "yyyy-MM-dd HH:mm:ss" 格式<br>
     * 3、java.sql.Date 转换为 "yyyy-MM-dd" 格式<br>
     * 4、java.util.Timestamps 时间转换
     *
     * @param binder  WebDataBinder 要注册的binder
     * @param request 前端请求
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	dateFormat.setLenient(false);//是否严格解析时间 false则严格解析 true宽松解析
    	binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        // 字符串自动Trim
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }
    
    /**
     * 获取请求方IP
     *
     * @return 客户端Ip
     */
    public String getClientIp() {
        String xff = request.getHeader("x-forwarded-for");
        if (xff == null) {
            return "8.8.8.8";
        }
        return xff;
    }
    public JSONObject getJsonRequest() {
        JSONObject result = null;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader();) {
            char[] buff = new char[1024];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
            result = JSONObject.parseObject(sb.toString());
            if(result==null) {
	       		 result = new JSONObject();
	       	 }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public  Map<String, Object> getParameters() {
		Enumeration<String> enume = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (enume.hasMoreElements()) {
			String key = enume.nextElement();
			String value = request.getParameter(key);
			map.put(key, value);
		}
		return map;
	}
	public  Map<String, Object> getParameters(boolean printLog) {
		Enumeration<String> enume = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (enume.hasMoreElements()) {
			String key = enume.nextElement();
			String value = request.getParameter(key);
			map.put(key, value);
			if (printLog) {
				logger.info(key + "==>" + value);
			}
		}
		return map;
	}
    protected void buildBussinessText(HttpServletResponse response,String context) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public Object getAttribute(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Object) session.getAttribute(name);
		} else {
			return null;
		}
	}
	public void setAttribute(HttpServletRequest request, String name, Object value) {
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}
	public HashMap<String, String> getParameterRequest() {
    	HashMap<String, String> parmas=new HashMap<String, String>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			parmas.put(paraName, request.getParameter(paraName));
		}
        return parmas;
    }
}
