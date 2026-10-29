package com.xiaohucare.app.care.constants;

/**
 * 宠物卡状态枚举
 * 0-待上架 1-在售 2-交易中 3-已售 4-租赁中 5-已下架
 */
public enum StoreCardStateEnum {
    PENDING(0, "待上架"),
    ON_SALE(1, "在售"),
    TRADING(2, "交易中"),
    SOLD(3, "已售"),
    RENTING(4, "租赁中"),
    OFF_SHELF(5, "已下架"),
    UNKNOWN(-1, "未知");

    private final int code;
    private final String name;

    StoreCardStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static StoreCardStateEnum getByCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (StoreCardStateEnum state : values()) {
            if (state.code == code) {
                return state;
            }
        }
        return UNKNOWN;
    }

    public boolean isOnSale() {
        return this == ON_SALE;
    }

    public boolean isTrading() {
        return this == TRADING;
    }

    public boolean isSold() {
        return this == SOLD;
    }

    public boolean isRenting() {
        return this == RENTING;
    }
}
