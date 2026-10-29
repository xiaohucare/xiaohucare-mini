package com.xiaohucare.framework.core.exception;

/**
 * 自定义异常
 *
 * @author xiaohucare
 * @date 2016年10月27日 下午10:11:27
 */
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;
    private int code = 500;
    public RRException(String message) {
        super(message);
        this.message = message;
    }
    public RRException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public RRException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public RRException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
