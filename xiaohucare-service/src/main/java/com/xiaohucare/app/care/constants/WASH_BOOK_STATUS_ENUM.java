package com.xiaohucare.app.care.constants;

public enum WASH_BOOK_STATUS_ENUM {
//   { value: 0, key: 'PENDING', label: '待确认', type: 'warning' },
//   { value: 1, key: 'CONFIRMED', label: '已确认', type: '' },
//   { value: 2, key: 'IN_PROGRESS', label: '进行中', type: 'primary' },
//   { value: 3, key: 'COMPLETED', label: '洗护完成，待结算', type: 'success' },
//   { value: 4, key: 'PAID', label: '已支付，待交付', type: 'info' },
//   { value: 5, key: 'CANCELLED', label: '已取消', type: 'danger' },
//   { value: 10, key: 'DONE', label: '已完成', type: 'success' }

    WAIT_CONFIRM(0, "待确认"),
    CONFIRMED(1, "已确认"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "洗护完成，待结算"),
    PAID(4, "已支付，待交付"),
    CANCELLED(5, "已取消"),
    DONE(10, "已完成");

    private int code;
    private String message;

    WASH_BOOK_STATUS_ENUM(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public static WASH_BOOK_STATUS_ENUM fromCode(int code) {
        for (WASH_BOOK_STATUS_ENUM status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return WAIT_CONFIRM;
    }

}
