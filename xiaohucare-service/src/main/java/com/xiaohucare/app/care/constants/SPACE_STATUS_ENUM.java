package com.xiaohucare.app.care.constants;

public enum SPACE_STATUS_ENUM {
    DISABLED(0, "禁用"),
    IDLE(1, "空闲"),
    BOOKED(2, "已预订"),
    UNDER_REPAIR(3, "维修中"),
    CLEANING(4, "清洁中"),
    USED(5, "已使用");
    private final int code;
    private final String message;
    SPACE_STATUS_ENUM(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public static SPACE_STATUS_ENUM fromCode(int code) {
        for (SPACE_STATUS_ENUM value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return DISABLED;
    }
    public static SPACE_STATUS_ENUM fromMessage(String message) {
        for (SPACE_STATUS_ENUM value : values()) {
            if (value.message.equals(message)) {
                return value;
            }
        }
        return DISABLED;
    }
}
