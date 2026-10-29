package com.xiaohucare.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;

/**
 * Druid 监控页手动注册配置。
 *
 * <p>平台模块使用 dynamic-datasource + HikariCP 数据源，导致
 * druid-spring-boot-3-starter 的 DruidDataSourceAutoConfigure
 * （@ConditionalOnProperty spring.datasource.type = DruidDataSource）整体退避，
 * 其中的 StatViewServlet / WebStatFilter 也随之不注册，
 * 访问 /druid/login.html 会 404 并转发到 /error，最终被 Spring Security 拦成 401。
 *
 * <p>此处在不改变 Hikari 连接池的前提下，手动注册 jakarta 版监控 Servlet 与过滤器，
 * 使 Druid Web 监控页可正常打开（Web URI 统计可用；SQL/数据源页因未使用 Druid
 * 连接池而为空）。可通过 spring.datasource.druid.stat-view-servlet.enabled=true 开启。
 *
 * @author xiaohucare
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
public class DruidMonitorConfig {

    @Value("${spring.datasource.druid.stat-view-servlet.login-username:}")
    private String loginUsername;

    @Value("${spring.datasource.druid.stat-view-servlet.login-password:}")
    private String loginPassword;

    /**
     * 注册 Druid 监控 StatViewServlet，映射到 /druid/*（相对 context-path）。
     *
     * <p>监控页可查看数据源、SQL、Session 等敏感信息，开启时必须配置强账号密码，
     * 否则直接启动失败，避免以默认账号对外暴露。
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        if (loginUsername == null || loginUsername.trim().isEmpty()
                || loginPassword == null || loginPassword.trim().isEmpty()) {
            throw new IllegalStateException("已开启 Druid 监控页（stat-view-servlet.enabled=true），"
                    + "必须通过环境变量 DRUID_MONITOR_USERNAME / DRUID_MONITOR_PASSWORD 设置控制台账号密码；"
                    + "若无需要请将 spring.datasource.druid.stat-view-servlet.enabled 设为 false（默认即为 false）");
        }
        ServletRegistrationBean<StatViewServlet> registration =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        // 控制台登录账号（与系统登录无关），配置后即启用登录页
        registration.addInitParameter("loginUsername", loginUsername);
        registration.addInitParameter("loginPassword", loginPassword);
        // 允许所有 IP 访问（留空即不限制），如需白名单可配置 allow
        registration.addInitParameter("allow", "");
        // 生产环境禁用手动重置
        registration.addInitParameter("resetEnable", "false");
        return registration;
    }

    /**
     * 注册 Druid Web 统计过滤器，采集 URI / Session 维度的监控数据。
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> registration =
                new FilterRegistrationBean<>(new WebStatFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        registration.addInitParameter("sessionStatEnable", "false");
        registration.addInitParameter("profileEnable", "true");
        return registration;
    }
}
