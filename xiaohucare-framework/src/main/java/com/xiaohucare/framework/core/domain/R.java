package com.xiaohucare.framework.core.domain;

import java.io.Serializable;

import com.xiaohucare.common.constant.HttpStatus;
import com.xiaohucare.framework.core.api.vo.Response;

/**
 * 响应信息主体
 *
 * @author xiaohucare
 */
public class R<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS =Response.OK;// HttpStatus.SUCCESS;

    /** 失败 */
    public static final int FAIL = HttpStatus.ERROR;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok()
    {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data)
    {
        return restResult(data, SUCCESS, "操作成功");
    }
    public static <T>  R<T> data(T data) {
    	return restResult(data, SUCCESS, "操作成功");
	}
    public static <T> R<T> success(String msg)
    {
        return restResult(null, SUCCESS, msg);
    }
    /**
	 * 返回R
	 *
	 * @param data 数据
	 * @param message  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> data(T data, String message) {
		 return restResult(data, SUCCESS, message);
	}

    public static <T> R<T> ok(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

   
    public static  <T> R<T> error(String msg) {
    	return restResult(null, FAIL, msg);
	}
    public static <T> R<T> fail()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg)
    {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public static <T> Boolean isError(R<T> ret)
    {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret)
    {
        return R.SUCCESS == ret.getCode();
    }

	

	
}
