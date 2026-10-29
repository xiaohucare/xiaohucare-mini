package com.xiaohucare.framework.core.api.utils;
import com.xiaohucare.common.utils.StringUtils;

/**
 * 数据校验
 */
public abstract class ApiAssert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isNullOrEmpty(str)||StringUtils.isBlank(str)) {
            throw new ApiRRException(message);
        }
    }
    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ApiRRException(message);
        }
    }
    public static void error(String message) {
    	throw new ApiRRException(message);
    }
    public static void error(int code,String message) {
    	throw new ApiRRException(message,code);
    }
}
