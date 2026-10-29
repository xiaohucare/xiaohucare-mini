package com.xiaohucare.app.care.constants;

public enum HOTEL_BOOK_STATUS_ENUM {
//0-待确认 1-已确认 2-已取消 3-已登记 4-已取消 10-已入驻 11-已离店 12--已结算
    WAIT_CONFIRM(0, "待确认"),
    CONFIRMED(1, "已确认"),
    CANCELLED(2, "已取消"),
    REGISTERED(3, "已登记"),
    
    CHECK_IN(10, "已入驻"),
    CHECK_OUT(11, "已离店"),
    SETTLED(12, "已结算");

    private int code;
    private String message;

    HOTEL_BOOK_STATUS_ENUM(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public static HOTEL_BOOK_STATUS_ENUM fromCode(int code) {
        for (HOTEL_BOOK_STATUS_ENUM status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return WAIT_CONFIRM;
    }

}
