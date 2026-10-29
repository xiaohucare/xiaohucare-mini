package com.xiaohucare.app.care.constants;

/**
 * 宠物拍照部位枚举
 * @author system
 * @date 2026-05-25
 */
public enum PET_BODY_PART_ENUM {

    /** 脸部 */
    FACE("face", "脸部", 1),
    
    /** 头部 */
    HEAD("head", "头部", 2),
    
    /** 身体 */
    BODY("body", "身体", 3),
    
    /** 腿部 */
    LEGS("legs", "腿部", 4),
    
    /** 尾巴 */
    TAIL("tail", "尾巴", 5),
    
    /** 爪子 */
    PAWS("paws", "爪子", 6),
    
    /** 耳朵 */
    EARS("ears", "耳朵", 7),
    
    /** 整体 */
    OVERALL("overall", "整体", 8);

    /** 部位代码 */
    private final String code;
    
    /** 部位名称 */
    private final String name;
    private final Integer index;

    PET_BODY_PART_ENUM(String code, String name, Integer index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public Integer getIndex() {
        return index;
    }
    

    /**
     * 根据代码获取枚举
     * 
     * @param code 部位代码
     * @return 枚举值，如果未找到返回 null
     */
    public static PET_BODY_PART_ENUM fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (PET_BODY_PART_ENUM part : values()) {
            if (part.code.equals(code)) {
                return part;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     * 
     * @param name 部位名称
     * @return 枚举值，如果未找到返回 null
     */
    public static PET_BODY_PART_ENUM fromName(String name) {
        if (name == null) {
            return null;
        }
        for (PET_BODY_PART_ENUM part : values()) {
            if (part.name.equals(name)) {
                return part;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code + "-" + name;
    }
}
