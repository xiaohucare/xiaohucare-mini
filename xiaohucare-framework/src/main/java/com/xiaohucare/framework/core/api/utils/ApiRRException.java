package com.xiaohucare.framework.core.api.utils;

import com.xiaohucare.framework.core.api.vo.Response;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ApiRRException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private int code = Response.BUSINESS_ERROR;

    public ApiRRException(String errmsg) {
        super(errmsg);
        log.info("----------ApiRRException.errmsg------:"+errmsg);
        this.message = errmsg;
    }

    public ApiRRException(String errmsg, Throwable e) {
        super(errmsg, e);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.Throwable------:"+e);
        this.message = errmsg;
    }

    public ApiRRException(String errmsg, int errno) {
        super(errmsg);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.errno------:"+errno);
        this.message = errmsg;
        this.code = errno;
    }

    public ApiRRException(String errmsg, int errno, Throwable e) {
        super(errmsg, e);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.errno------:"+errno+"----------ApiRRException.Throwable------:"+e);
        this.message = errmsg;
        this.code = errno;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

  
}
