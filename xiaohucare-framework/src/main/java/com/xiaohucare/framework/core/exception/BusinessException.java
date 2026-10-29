package com.xiaohucare.framework.core.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private int code = 500;
    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
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


}

