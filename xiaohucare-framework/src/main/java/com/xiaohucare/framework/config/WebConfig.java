package com.xiaohucare.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，解决中文乱码问题
 * 注意:字符编码过滤器已由Spring Boot自动配置提供，通过application.yml中的spring.http.encoding配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // CharacterEncodingFilter 由 Spring Boot 自动配置提供
    // 配置项见 application.yml: spring.http.encoding.charset/enabled/force
}