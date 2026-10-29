package com.xiaohucare.framework.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid配置类
 * 
 * 注意：Druid 1.2.28 的监控页面仍使用 javax.servlet，与 Spring Boot 3 不兼容
 * 暂时禁用监控页面，仅保留数据源配置功能
 */
@Configuration
public class DruidConfig {

    /**
     * 配置Druid监控界面
     * TODO: 等待 Druid 完全支持 Jakarta EE 后启用
     */
    // @Bean
    // public ServletRegistrationBean<?> statViewServlet() {
    //     // Druid 监控页面暂时禁用
    //     return null;
    // }

    /**
     * 配置Web监控
     * TODO: 等待 Druid 完全支持 Jakarta EE 后启用
     */
    // @Bean
    // public FilterRegistrationBean<?> webStatFilter() {
    //     // Druid Web 监控暂时禁用
    //     return null;
    // }
}