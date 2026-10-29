package com.xiaohucare.common.extra.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.springframework.core.env.Environment;

import com.xiaohucare.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class OpenBrowserUtil {
	public static void openBrowser(Environment env){
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String port = env.getProperty("server.port");
			String path = env.getProperty("server.servlet.context-path");
			if (StringUtils.isEmpty(path)) {
				path = "";
			}
			log.info("\n\t"
					+ "----------------------------------------------------------\n\t"
					+ "Application  is running! Access URLs:\n\t" + "Local访问网址: \t\t http://localhost:" + port + path
					+ "\n\t" + "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t"
					+ "----------------------------------------------------------");
			String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			log.info("当前项目进程号：" + jvmName.split("@")[0]);
			String url="http://"+ip+":"+port + path;
			OpenBrowserUtil.openBrowser(url);
		} catch (UnknownHostException e) {
		}
	}
	public static void openBrowser(String url){
		String os=getOperatingSystem();
		try {
			openBrowser(url, os);
		} catch (Exception e) {
		} 
	}
	/**
	 * 获取当前操作系统类型
	 * 
	 * @return 操作系统名称 (windows, mac, linux, solaris, 或其他)
	 */
	public static String getOperatingSystem() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return "windows";
		} else if (osName.contains("mac")) {
			return "mac";
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			return "linux";
		} else if (osName.contains("sunos")) {
			return "solaris";
		} else {
			return "other";
		}
	}
	
	/**
	 * 使用默认浏览器打开指定URL
	 * 
	 * @param url 要打开的网址
	 * @param os  操作系统类型
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 */
	public static void openBrowser(String url, String os) throws IOException, URISyntaxException, InterruptedException {
		// 首先尝试使用Desktop类，这是最通用的方法
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI(url));
			return;
		}
		// 如果Desktop类不支持，则根据操作系统使用特定命令
		Runtime runtime = Runtime.getRuntime();
		switch (os) {
		case "windows":
			// Windows系统
			runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			break;
		case "mac":
			// Mac系统
			runtime.exec(new String[] { "open", url });
			break;
		case "linux":
		case "solaris":
			// Linux/Unix系统，尝试几种可能的命令
			String[] browsers = { "xdg-open", "gnome-open", "kde-open", "sensible-browser" };
			boolean opened = false;

			for (String browser : browsers) {
				try {
					runtime.exec(new String[] { browser, url });
					opened = true;
					break;
				} catch (IOException e) {
				}
			}
			if (!opened) {
				throw new IOException("无法找到可用的浏览器命令");
			}
			break;
		default:
			throw new UnsupportedOperationException("不支持的操作系统: " + os);
		}
	}
}
