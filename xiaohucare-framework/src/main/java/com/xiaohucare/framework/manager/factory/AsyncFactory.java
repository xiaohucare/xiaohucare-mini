package com.xiaohucare.framework.manager.factory;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaohucare.common.constant.Constants;
import com.xiaohucare.common.utils.LogUtils;
import com.xiaohucare.common.utils.ServletUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.common.utils.ip.AddressUtils;
import com.xiaohucare.common.utils.ip.IPOfflineUtil;
import com.xiaohucare.common.utils.ip.IPOnlineUtil;
import com.xiaohucare.common.utils.ip.IpCityVo;
import com.xiaohucare.common.utils.ip.IpUtils;
import com.xiaohucare.common.utils.spring.SpringUtils;
import com.xiaohucare.framework.system.domain.SysLogininfor;
import com.xiaohucare.framework.system.domain.SysOperLog;
import com.xiaohucare.framework.system.service.ISysLogininforService;
import com.xiaohucare.framework.system.service.ISysOperLogService;

import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步工厂（产生任务用）
 * 
 * @author xiaohucare
 */
@Slf4j
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
            	 String address=null;
//                String address = AddressUtils.getRealAddressByIP(ip);
                IpCityVo ipCityVo=IPOfflineUtil.getRegion(ip);
                if(ipCityVo==null||(ipCityVo.getStatus()!=null&&ipCityVo.getStatus()!=0)) {
                	ipCityVo=IPOnlineUtil.getNetIPAPIByIP(ip);
                }
                log.info("========ipCityVo========="+JSONUtil.toJsonStr(ipCityVo));
//                String address = AddressUtils.getRealAddressByIP(ip);
                address=ipCityVo.getTips();
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
