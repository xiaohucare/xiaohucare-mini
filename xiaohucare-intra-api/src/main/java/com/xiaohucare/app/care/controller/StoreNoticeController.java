package com.xiaohucare.app.care.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohucare.common.core.page.TableDataInfo;
import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.system.domain.SysNotice;
import com.xiaohucare.framework.system.service.ISysNoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 公告 信息操作处理
 * @author xiaohucare
 */
@Tag(name = "通知公告")
@RestController
@RequestMapping("/intra/care/notice")
public class StoreNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;
    /**
     * 获取通知公告列表
     */
    @Operation(summary = "获取通知公告列表")
    @PreAuthorize("@ss.hasPermi('care:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }
    /**
     * 根据通知公告编号获取详细信息
     */
    @Operation(summary = "根据通知公告编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('care:notice:list')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }
}
