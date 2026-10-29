package com.xiaohucare.framework.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xiaohucare.framework.core.api.utils.ApiRRException;
import com.xiaohucare.framework.core.api.vo.Response;
import com.xiaohucare.framework.core.api.vo.ResultMap;

/**
 * 异常处理器
 *
 * @author xiaohucare
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice(value = {"com.xiaohucare"})
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(RRException.class)
    public ResultMap handleRRException(RRException e) {
        return ResultMap.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultMap handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return  ResultMap.error(Response.DB_ERROR,"数据库中已存在该记录");
    }

//    @ExceptionHandler(AuthorizationException.class)
//    public ResultMap handleAuthorizationException(AuthorizationException e) {
//        logger.error(e.getMessage(), e);
//        return ResultMap.error(Response.AUTH_ERROR,"没有权限，请联系管理员授权");
//    }

    @ExceptionHandler(Exception.class)
    public ResultMap handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error();
    }

    @ExceptionHandler(ApiRRException.class)
    public ResultMap handleApiRRException(ApiRRException e) {
    	 return ResultMap.error(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public ResultMap handleBusinessException(BusinessException e) {
    	 return ResultMap.error(e.getCode(), e.getMessage());
    }
}
