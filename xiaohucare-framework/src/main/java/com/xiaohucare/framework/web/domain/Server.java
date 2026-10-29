package com.xiaohucare.framework.web.domain;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.xiaohucare.common.utils.Arith;
import com.xiaohucare.common.utils.ip.IpUtils;
import com.xiaohucare.framework.web.domain.server.Cpu;
import com.xiaohucare.framework.web.domain.server.Jvm;
import com.xiaohucare.framework.web.domain.server.Mem;
import com.xiaohucare.framework.web.domain.server.Sys;
import com.xiaohucare.framework.web.domain.server.SysFile;
import com.xiaohucare.framework.web.domain.server.ProcessInfo;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * 服务器相关信息
 * 
 * @author xiaohucare
 */
public class Server
{
    private static final int OSHI_WAIT_SECOND = 1000;
    
    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();

    /**
     * 当前进程信息
     */
    private ProcessInfo processInfo = new ProcessInfo();

    public Cpu getCpu()
    {
        return cpu;
    }

    public void setCpu(Cpu cpu)
    {
        this.cpu = cpu;
    }

    public Mem getMem()
    {
        return mem;
    }

    public void setMem(Mem mem)
    {
        this.mem = mem;
    }

    public Jvm getJvm()
    {
        return jvm;
    }

    public void setJvm(Jvm jvm)
    {
        this.jvm = jvm;
    }

    public Sys getSys()
    {
        return sys;
    }

    public void setSys(Sys sys)
    {
        this.sys = sys;
    }

    public List<SysFile> getSysFiles()
    {
        return sysFiles;
    }

    public void setSysFiles(List<SysFile> sysFiles)
    {
        this.sysFiles = sysFiles;
    }

    public ProcessInfo getProcessInfo()
    {
        return processInfo;
    }

    public void setProcessInfo(ProcessInfo processInfo)
    {
        this.processInfo = processInfo;
    }

    public void copyTo() throws Exception
    {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor());

        setMemInfo(hal.getMemory());

        setSysInfo();

        setJvmInfo();

        setSysFiles(si.getOperatingSystem());
        
        setProcessInfo(si.getOperatingSystem());
    }

    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor)
    {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory)
    {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo()
    {
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws UnknownHostException
    {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os)
    {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray)
        {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /**
     * 设置当前进程信息
     */
    private void setProcessInfo(OperatingSystem os)
    {
        try
        {
            // 获取当前 Java 进程的 PID
            long pid = ProcessHandle.current().pid();
            processInfo.setPid(pid);

            // 获取当前进程信息
            oshi.software.os.OSProcess process = os.getProcess((int) pid);
            if (process != null)
            {
                // 进程名称
                processInfo.setName(process.getName());
                
                // 进程路径
                processInfo.setPath(process.getPath());
                
                // 进程实际使用的物理内存（RSS - Resident Set Size）
                long residentSetSize = process.getResidentSetSize();
                processInfo.setPhysicalMemory(convertFileSize(residentSetSize));
                processInfo.setPhysicalMemoryBytes(residentSetSize);
                
                // 进程虚拟内存
                long virtualSize = process.getVirtualSize();
                processInfo.setVirtualMemory(convertFileSize(virtualSize));
                processInfo.setVirtualMemoryBytes(virtualSize);
                
                // 进程 CPU 使用率
                processInfo.setCpuUsage(process.getProcessCpuLoadCumulative());
                
                // 进程启动时间
                processInfo.setStartTime(new java.util.Date(process.getStartTime()));
                
                // 进程运行时间（毫秒）
                processInfo.setUptime(process.getUpTime());
                
                // 进程线程数
                processInfo.setThreadCount(process.getThreadCount());
            }
        }
        catch (Exception e)
        {
            // 如果获取失败，记录错误信息
            processInfo.setError("获取进程信息失败: " + e.getMessage());
        }
    }

    /**
     * 字节转换
     * 
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size)
    {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else
        {
            return String.format("%d B", size);
        }
    }


}
