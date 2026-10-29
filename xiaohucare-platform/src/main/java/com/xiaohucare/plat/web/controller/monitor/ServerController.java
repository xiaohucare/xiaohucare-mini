package com.xiaohucare.plat.web.controller.monitor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.web.domain.Server;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 服务器监控
 * 
 * @author xiaohucare
 */
@Tag(name = "服务器监控")
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @Operation(summary = "获取服务器监控信息")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
