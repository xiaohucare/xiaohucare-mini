package com.xiaohucare.framework.core.validator;

import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.api.utils.ApiRRException;

/**
 * 数据校验
 *
 * @author lipengjun
 * @date 2017-03-23 15:50
 */
public abstract class ApiAssert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
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
