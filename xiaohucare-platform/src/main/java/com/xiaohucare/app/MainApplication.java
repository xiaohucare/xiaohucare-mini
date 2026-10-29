package com.xiaohucare.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xiaohucare.common.extra.utils.OpenBrowserUtil;

/**
 * 启动程序
 * @author xiaohucare
 */
@SpringBootApplication(scanBasePackages = "com.xiaohucare")
@EnableScheduling
public class MainApplication {
	public static void main(String[] args) {
		if (System.getProperty("java.version").startsWith("17") || 
		    System.getProperty("java.version").startsWith("21")) {
			System.out.println("检测到 JDK 17/21，请确保已配置 --add-opens JVM 参数");
		}
		// 显式设置激活的 profile
		SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
		builder.profiles("druid", "oss");
		ConfigurableApplicationContext application = builder.run(args);
		
		System.out.println("(♥◠‿◠)ﾉﾞ  小护康养 mini 版后台服务启动成功   ლ(´ڡ`ლ)ﾞ  \n");
		Environment env = application.getEnvironment();
		OpenBrowserUtil.openBrowser(env);
	}
}