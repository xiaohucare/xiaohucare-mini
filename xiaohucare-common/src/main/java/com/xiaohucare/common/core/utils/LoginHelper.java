package com.xiaohucare.common.core.utils;

/**
 * 登录帮助工具类（简化版本）
 * 
 * TODO: 这是一个临时实现，后续需要集成完整的认证框架
 */
public class LoginHelper {
    
    /**
     * 获取当前用户ID
     * TODO: 暂时返回默认值 1L
     */
    public static Long getUserId() {
        // 暂时返回默认用户ID
        return 1L;
    }
    
    /**
     * 获取当前用户名
     * TODO: 暂时返回默认值
     */
    public static String getUsername() {
        return "default_user";
    }
    
    /**
     * 检查是否已登录
     * TODO: 暂时返回 true
     */
    public static boolean isLogin() {
        return true;
    }
}
