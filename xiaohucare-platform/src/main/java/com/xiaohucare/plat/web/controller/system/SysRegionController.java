package com.xiaohucare.plat.web.controller.system;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VQueryKit;
import com.xiaohucare.framework.system.domain.SysRegion;
import com.xiaohucare.framework.system.service.ISysRegionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 地区管理
 * 
 * @author xiaohucare
 */
@Tag(name = "地区管理")
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends BaseController
{
    @Autowired
    private ISysRegionService sysRegionService;
    @GetMapping("/list")
    @Operation(summary = "查询地区列表")
    @PreAuthorize("@ss.hasPermi('system:region:list')")
    public R<IPage<SysRegion>> list(@RequestParam Map<String, Object> params){
         QueryWrapper<SysRegion> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SysRegion.class);
        //  queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
         IPage<SysRegion> pages  = sysRegionService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(pages);
    }
    @Operation(summary = "查询地区列表ByParentId")
    @PreAuthorize("@ss.hasPermi('system:region:list')")
    @GetMapping("/listByParentId/{parentId}")
    public AjaxResult listByParentId(@PathVariable Long parentId)
    {
         QueryWrapper<SysRegion> queryWrapper = new QueryWrapper<>();
         queryWrapper.eq("parent_id", parentId);
         List<SysRegion> list = sysRegionService.list(queryWrapper);
         return success(list);
    }

    /**
     * 根据地区编号获取详细信息
     */
    @Operation(summary = "根据地区ID查询地区信息")
    @PreAuthorize("@ss.hasPermi('system:region:query')")
    @GetMapping(value = "/{regionId}")
    public AjaxResult getInfo(@PathVariable Long regionId)
    {
        return success(sysRegionService.getById(regionId));
    }
    /**
     * 新增角色
     */
    @Operation(summary = "新增地区")
    @PreAuthorize("@ss.hasPermi('system:region:add')")
    @Log(title = "地区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRegion region)
    {
        return toAjax(sysRegionService.save(region));

    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改地区信息")
    @PreAuthorize("@ss.hasPermi('system:region:edit')")
    @Log(title = "地区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRegion region)
    {
          sysRegionService.updateById(region);
          return success();
       
    }

    /**
     * 删除地区
     */
    @Operation(summary = "删除地区")
    @PreAuthorize("@ss.hasPermi('system:region:remove')")
    @Log(title = "地区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regionIds}")
    public AjaxResult remove(@PathVariable Long[] regionIds)
    {
        return toAjax(sysRegionService.removeByIds(Arrays.asList(regionIds)));
    }
  
}
