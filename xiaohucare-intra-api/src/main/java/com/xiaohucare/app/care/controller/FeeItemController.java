package com.xiaohucare.app.care.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.FeeItemEntity;
import com.xiaohucare.app.care.service.FeeItemService;
import com.xiaohucare.app.care.vo.FeeItemVO;
import com.xiaohucare.app.care.wrapper.FeeItemWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
/**
 * 收费类目表
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
@RestController
@RequestMapping("intra/care/feeitem")
public class FeeItemController extends BaseController{
    @Autowired
    private FeeItemService feeItemService;
    /**
     * 列表（兼容browse）
     */
    @Operation(summary = "列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('care:feeitem:list')")
    public R<VPage<FeeItemVO>> list(@RequestParam Map<String, Object> params){
        QueryWrapper<FeeItemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        // 按类目名称模糊查询
        queryWrapper.like(StringUtils.isNotNull(params.get("itemName")), "item_name", params.get("itemName"));
        // 按项目类型筛选
        queryWrapper.eq(StringUtils.isNotNull(params.get("itemType")), "item_type", params.get("itemType"));
        // 按计费方式筛选
        queryWrapper.eq(StringUtils.isNotNull(params.get("chargeMode")), "charge_mode", params.get("chargeMode"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("chargeKind")), "charge_kind", params.get("chargeKind"));
        // 按状态筛选
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")), "status", params.get("status"));
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
        IPage<FeeItemEntity> pages = feeItemService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(FeeItemWrapper.build().toVPageVO(pages));
    }

    /**
     * 列表（旧接口兼容）
     */
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:feeitem:list')")
    public R<VPage<FeeItemVO>> browse(@RequestParam Map<String, Object> params){
        return list(params);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:feeitem:detail')")
    public R<FeeItemVO> detail(@PathVariable("id") Long id){
        FeeItemEntity feeItem = feeItemService.getById(id);
        return R.data(FeeItemWrapper.build().entityVO(feeItem));
    }

    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:feeitem:submit')")
    public R<FeeItemVO> submit(@Valid @RequestBody FeeItemVO feeItem){
        if(feeItem.getId() == null) {
            feeItem.setCreateAt(new Date());
            feeItem.setIsDelete(0);
            // 默认启用
            if(feeItem.getStatus() == null) {
                feeItem.setStatus(1);
            }
        }
        feeItem.setUpdateAt(new Date());
        if(feeItemService.saveOrUpdate(feeItem)) {
            return R.ok(feeItem);
        }
        return R.error("数据错误");
    }

    /**
     * 启用/禁用
     */
    @Operation(summary = "启用禁用")
    @PostMapping("/toggle/{id}")
    @PreAuthorize("@ss.hasPermi('care:feeitem:submit')")
    public R<Boolean> toggle(@PathVariable("id") Long id, @RequestBody Map<String, Object> body){
        Object statusVal = body.get("status");
        if(statusVal == null) {
            // 兼容前端传 enabled 字段（Boolean）
            Object enabledVal = body.get("enabled");
            statusVal = Boolean.TRUE.equals(enabledVal) ? 1 : 0;
        }
        UpdateWrapper<FeeItemEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("status", statusVal).set("update_at", new Date());
        return R.ok(feeItemService.update(wrapper));
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:feeitem:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(feeItemService.removeByIds(Arrays.asList(ids)));
    }

}
