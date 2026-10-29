package com.xiaohucare.plat.web.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * SpringDoc OpenAPI 配置
 * 从 Knife4j/Springfox 迁移到 SpringDoc
 */
@Configuration
public class Knife4jOpenApiConfig {

    @Value("${spring.application.name:小护康养 mini 版}")
    private String serviceName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(serviceName + " API文档")
                        .description("小护康养 mini 版API接口文档")
                        .version("1.0")
                        .contact(new Contact()
                                .name("大鱼科技")
                                .url("https://www.chongkelai.com")
                                .email("support@chongkelai.com")));
    }
}