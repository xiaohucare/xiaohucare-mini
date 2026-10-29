package com.xiaohucare.common.extra.utils;

import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;

/**
 * 系统监控工具类 优先使用 oshi 获取系统信息
 * 
 * @author xiaohucare
 */
@Slf4j
@Component
public class SysMonitorUtil {
	private static final SystemInfo SYSTEM_INFO = new SystemInfo();
	/**
	 * 获取当前进程的 PID
	 */
	public static int getPid() {
		String pidStr = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		return Integer.parseInt(pidStr);
	}
	/**
	 * 获取当前进程的内存信息（MB）
	 * 
	 * @return 进程实际使用的物理内存（MB）
	 */
	public static double getProcessMemoryMB() {
		try {
			int pid = getPid();
			OSProcess process = SYSTEM_INFO.getOperatingSystem().getProcess(pid);
			return process.getResidentSetSize() / 1024.0 / 1024.0;
		} catch (Exception e) {
			log.error("获取进程内存失败: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * 获取 JVM 已使用内存（MB）
	 */
	public static double getJvmUsedMemoryMB() {
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		return usedMemory / 1024.0 / 1024.0;
	}

	/**
	 * 获取 JVM 总内存（MB）
	 */
	public static double getJvmTotalMemoryMB() {
		return Runtime.getRuntime().totalMemory() / 1024.0 / 1024.0;
	}

	/**
	 * 获取 JVM 最大可用内存（MB）
	 */
	public static double getJvmMaxMemoryMB() {
		return Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0;
	}

	/**
	 * 获取 JVM 空闲内存（MB）
	 */
	public static double getJvmFreeMemoryMB() {
		return Runtime.getRuntime().freeMemory() / 1024.0 / 1024.0;
	}

	/**
	 * 获取 JVM 内存使用率（%）
	 */
	public static double getJvmMemoryUsagePercent() {
		double usedMB = getJvmUsedMemoryMB();
		double maxMB = getJvmMaxMemoryMB();
		if (maxMB == 0) {
			return 0;
		}
		return (usedMB / maxMB) * 100;
	}

	/**
	 * 获取系统总内存（GB）
	 */
	public static double getTotalMemoryGB() {
		GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
		return memory.getTotal() / 1024.0 / 1024.0 / 1024.0;
	}

	/**
	 * 获取系统可用内存（GB）
	 */
	public static double getAvailableMemoryGB() {
		GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
		return memory.getAvailable() / 1024.0 / 1024.0 / 1024.0;
	}

	/**
	 * 获取系统内存使用率（%）
	 */
	public static double getSystemMemoryUsagePercent() {
		GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
		long total = memory.getTotal();
		long available = memory.getAvailable();
		if (total == 0) {
			return 0;
		}
		return ((total - available) / (double) total) * 100;
	}

	/**
	 * 获取 CPU 使用率（%）
	 */
	public static double getCpuUsagePercent() {
		try {
			CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
			long[] ticks = processor.getSystemCpuLoadTicks();
			// 等待 1 秒再次采样
			Thread.sleep(1000);
			ticks = processor.getSystemCpuLoadTicks();
			return processor.getSystemCpuLoadBetweenTicks(ticks) * 100;
		} catch (Exception e) {
			log.error("获取 CPU 使用率失败: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * 获取 CPU 核心数
	 */
	public static int getCpuCores() {
		return SYSTEM_INFO.getHardware().getProcessor().getLogicalProcessorCount();
	}

	/**
	 * 打印完整的系统信息
	 */
	public static void printSystemInfo() {
		try {
			int pid = getPid();
			double processMemMB = getProcessMemoryMB();
			double jvmUsedMB = getJvmUsedMemoryMB();
			double jvmTotalMB = getJvmTotalMemoryMB();
			double jvmMaxMB = getJvmMaxMemoryMB();
			double jvmFreeMB = getJvmFreeMemoryMB();
			double jvmUsagePercent = getJvmMemoryUsagePercent();
			double systemTotalGB = getTotalMemoryGB();
			double systemAvailableGB = getAvailableMemoryGB();
			double systemUsagePercent = getSystemMemoryUsagePercent();
			double cpuUsagePercent = getCpuUsagePercent();
			int cpuCores = getCpuCores();

			log.info("\n" + "========================================\n" + "📊 系统监控信息\n"
					+ "========================================\n" + "🔖 进程号 (PID): {}\n"
					+ "💾 JVM 内存使用: {} MB / {} MB (最大: {} MB)\n" + "💿 进程物理内存: {} MB\n" + "📈 JVM 内存使用率: {}%\n"
					+ "🧹 JVM 空闲内存: {} MB\n" + "----------------------------------------\n" + "🖥️  系统总内存: {} GB\n"
					+ "💽 系统可用内存: {} GB\n" + "📊 系统内存使用率: {}%\n" + "----------------------------------------\n"
					+ "⚙️  CPU 核心数: {}\n" + "📈 CPU 使用率: {}%\n" + "========================================", pid,
					formatDouble(jvmUsedMB), formatDouble(jvmTotalMB), formatDouble(jvmMaxMB),
					formatDouble(processMemMB), formatDouble(jvmUsagePercent), formatDouble(jvmFreeMB),
					formatDouble(systemTotalGB), formatDouble(systemAvailableGB), formatDouble(systemUsagePercent),
					cpuCores, formatDouble(cpuUsagePercent));
		} catch (Exception e) {
			log.error("获取系统信息失败: {}", e.getMessage(), e);
		}
	}

	/**
	 * 打印简化的系统信息（仅 JVM 和进程信息）
	 */
	public static void printSimpleInfo() {
		try {
			int pid = getPid();
			double processMemMB = getProcessMemoryMB();
			double jvmUsedMB = getJvmUsedMemoryMB();
			double jvmMaxMB = getJvmMaxMemoryMB();
			double jvmFreeMB = getJvmFreeMemoryMB();
			double jvmUsagePercent = getJvmMemoryUsagePercent();

			log.info(
					"\n" + "========================================\n" + "📊 系统内存信息\n"
							+ "========================================\n" + "🔖 进程号 (PID): {}\n"
							+ "💾 JVM 内存使用: {} MB (最大: {} MB)\n" + "💿 进程物理内存: {} MB\n" + "📈 内存使用率: {}%\n"
							+ "🧹 空闲内存: {} MB\n" + "========================================",
					pid, formatDouble(jvmUsedMB), formatDouble(jvmMaxMB), formatDouble(processMemMB),
					formatDouble(jvmUsagePercent), formatDouble(jvmFreeMB));
		} catch (Exception e) {
			log.error("获取系统信息失败: {}", e.getMessage(), e);
		}
	}

	/**
	 * 格式化浮点数，保留 2 位小数
	 */
	private static String formatDouble(double value) {
		return String.format("%.2f", value);
	}

	/**
	 * 获取系统信息摘要（用于日志或监控）
	 */
	public static String getInfoSummary() {
		return String.format("PID: %d | JVM: %.2fMB/%.2fMB (%.2f%%) | 进程内存: %.2fMB | CPU: %.2f%%", getPid(),
				getJvmUsedMemoryMB(), getJvmMaxMemoryMB(), getJvmMemoryUsagePercent(), getProcessMemoryMB(),
				getCpuUsagePercent());
	}
}
