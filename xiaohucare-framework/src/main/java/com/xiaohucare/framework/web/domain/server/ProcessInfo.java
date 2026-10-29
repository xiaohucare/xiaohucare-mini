package com.xiaohucare.framework.web.domain.server;

    /**
     * 进程信息内部类
     */
    public  class ProcessInfo
    {
        /** 进程ID */
        private Long pid;
        
        /** 进程名称 */
        private String name;
        
        /** 进程路径 */
        private String path;
        
        /** 物理内存（格式化） */
        private String physicalMemory;
        
        /** 物理内存（字节） */
        private Long physicalMemoryBytes;
        
        /** 虚拟内存（格式化） */
        private String virtualMemory;
        
        /** 虚拟内存（字节） */
        private Long virtualMemoryBytes;
        
        /** CPU使用率 */
        private Double cpuUsage;
        
        /** 启动时间 */
        private java.util.Date startTime;
        
        /** 运行时间（毫秒） */
        private Long uptime;
        
        /** 线程数 */
        private Integer threadCount;
        
        /** 错误信息 */
        private String error;

        public Long getPid()
        {
            return pid;
        }

        public void setPid(Long pid)
        {
            this.pid = pid;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getPath()
        {
            return path;
        }

        public void setPath(String path)
        {
            this.path = path;
        }

        public String getPhysicalMemory()
        {
            return physicalMemory;
        }

        public void setPhysicalMemory(String physicalMemory)
        {
            this.physicalMemory = physicalMemory;
        }

        public Long getPhysicalMemoryBytes()
        {
            return physicalMemoryBytes;
        }

        public void setPhysicalMemoryBytes(Long physicalMemoryBytes)
        {
            this.physicalMemoryBytes = physicalMemoryBytes;
        }

        public String getVirtualMemory()
        {
            return virtualMemory;
        }

        public void setVirtualMemory(String virtualMemory)
        {
            this.virtualMemory = virtualMemory;
        }

        public Long getVirtualMemoryBytes()
        {
            return virtualMemoryBytes;
        }

        public void setVirtualMemoryBytes(Long virtualMemoryBytes)
        {
            this.virtualMemoryBytes = virtualMemoryBytes;
        }

        public Double getCpuUsage()
        {
            return cpuUsage;
        }

        public void setCpuUsage(Double cpuUsage)
        {
            this.cpuUsage = cpuUsage;
        }

        public java.util.Date getStartTime()
        {
            return startTime;
        }

        public void setStartTime(java.util.Date startTime)
        {
            this.startTime = startTime;
        }

        public Long getUptime()
        {
            return uptime;
        }

        public void setUptime(Long uptime)
        {
            this.uptime = uptime;
        }

        public Integer getThreadCount()
        {
            return threadCount;
        }

        public void setThreadCount(Integer threadCount)
        {
            this.threadCount = threadCount;
        }

        public String getError()
        {
            return error;
        }

        public void setError(String error)
        {
            this.error = error;
        }
    }
