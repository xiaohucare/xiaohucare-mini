package com.xiaohucare.framework.oss.enums;

/**
 * 存储类型枚举
 * 
 * @author xiaohucare
 */
public enum StorageType {
    
    /**
     * 本地存储
     */
    LOCAL("local", "本地存储"),
    
    /**
     * 阿里云OSS
     */
    ALIYUN("aliyun", "阿里云OSS"),
    
    /**
     * 华为云OSS
     */
    HUAWEI("huawei", "华为云OSS"),
    
    /**
     * RustFS
     */
    RUSTFS("rustfs", "RustFS存储");
    
    private final String code;
    private final String name;
    
    StorageType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据code获取枚举
     */
    public static StorageType getByCode(String code) {
        for (StorageType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}