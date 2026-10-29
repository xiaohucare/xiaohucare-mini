package com.xiaohucare.app.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * xxl-job config
 */
@Configuration
public class XxlJobConfig {
    private final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);
    
    @Value("${xxl-job.enabled:true}")
    private boolean enabled;

    @Value("${xxl-job.admin.addresses:}")
    private String adminAddresses;

    @Value("${xxl-job.admin.accessToken:}")
    private String accessToken;

    @Value("${xxl-job.admin.timeout:3}")
    private int timeout;

    @Value("${xxl-job.executor.app-name:}")
    private String appname;

    @Value("${xxl-job.executor.address:}")
    private String address;

    @Value("${xxl-job.executor.ip:}")
    private String ip;

    @Value("${xxl-job.executor.port:8189}")
    private int port;

    @Value("${xxl-job.executor.log-path:./xxl-job/platform}")
    private String logPath;

    @Value("${xxl-job.executor.log-retention-days:30}")
    private int logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        // 如果禁用了XXL-JOB，则不创建执行器
        if (!enabled) {
            logger.info(">>>>>>>>>>> xxl-job is disabled, skip config init >>>>>>>>>>>");
            return null;
        }
        
        logger.info(">>>>>>>>>>> xxl-job config init >>>>>>>>>>>");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setTimeout(timeout);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}