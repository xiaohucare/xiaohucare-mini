package com.xiaohucare.app.care.constants;

public enum WASH_JOB_STATUS_ENUM {
    
    WAITING(0, "待接宠"),
    BATHING(1, "正在洗浴"),
    DRYING(2, "正在烘干"),
    GROOMING(3, "正在美容"),
    CHECKING(4, "自检中"),
    REVIEW(5, "复检中"),
    SETTLE(6, "已复检"),
    READY(7, "待提宠"),
    DONE(10, "已完成");

    private int code;
    private String message;

    WASH_JOB_STATUS_ENUM(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public static WASH_JOB_STATUS_ENUM fromCode(int code) {
        for (WASH_JOB_STATUS_ENUM status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return WAITING;
    }

}
