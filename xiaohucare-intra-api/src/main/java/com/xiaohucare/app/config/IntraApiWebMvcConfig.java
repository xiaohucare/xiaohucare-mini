package com.xiaohucare.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xiaohucare.app.interceptor.ApiIntraStoreAuthInterceptor;

/**
 * WebMvc配置 - 注册门店内部API拦截器
 * 
 * @author xiaohucare
 */
@Configuration
public class IntraApiWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private ApiIntraStoreAuthInterceptor apiIntraStoreAuthInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册门店内部API授权拦截器
        registry.addInterceptor(apiIntraStoreAuthInterceptor)
                .addPathPatterns("/intra/care/**")  // 拦截所有/intra/care/**路径
                .excludePathPatterns(
                    "/intra/care/static/**",        // 排除静态资源
                    "/intra/care/upload/**"         // 排除上传路径（如有）
                );
    }
}
