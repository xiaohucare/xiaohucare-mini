package com.xiaohucare.app.care.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import com.xiaohucare.app.care.entity.ChargeComboEntity;
import com.xiaohucare.app.care.entity.ComboFeeItemEntity;
import com.xiaohucare.app.care.entity.FeeItemEntity;
import com.xiaohucare.app.care.service.ChargeComboService;
import com.xiaohucare.app.care.service.ComboFeeItemService;
import com.xiaohucare.app.care.service.FeeItemService;
import com.xiaohucare.app.care.vo.ChargeComboVO;
import com.xiaohucare.app.care.vo.FeeItemVO;
import com.xiaohucare.app.care.wrapper.ChargeComboWrapper;
import com.xiaohucare.app.care.wrapper.FeeItemWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
/**
 * 收费套餐表
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
@RestController
@RequestMapping("intra/care/chargecombo")
public class ChargeComboController extends BaseController{
    @Autowired
    private ChargeComboService chargeComboService;
    @Autowired
    private FeeItemService feeItemService;
    @Autowired
    private ComboFeeItemService comboFeeItemService;
    /**
     * 列表（兼容browse）
     */
    @Operation(summary = "列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:list')")
    public R<VPage<ChargeComboVO>> list(@RequestParam Map<String, Object> params){
        QueryWrapper<ChargeComboEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        
        // 按套餐名称模糊查询
        queryWrapper.like(StringUtils.isNotNull(params.get("packageName")), "combo_name", params.get("packageName"));
        // 按适用护理等级筛选（suitable_for 为逗号分隔的护理等级编码）
        queryWrapper.like(StringUtils.isNotNull(params.get("careLevel")), "suitable_for", params.get("careLevel"));
        // 按状态筛选
        queryWrapper.eq(StringUtils.isNotNull(params.get("status")), "status", params.get("status"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("chargeKind")), "charge_kind", params.get("chargeKind"));
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
        IPage<ChargeComboEntity> pages = chargeComboService.page(VQueryKit.getPage(params), queryWrapper);
        VPage<ChargeComboVO> vPage = ChargeComboWrapper.build().toVPageVO(pages);
        
        // 当 chargeKind=1（洗护套餐）时，关联查询 feeItemVoList
        if (vPage != null && vPage.getList() != null) {
            Object chargeKind = params.get("chargeKind");
            boolean isGrooming = chargeKind != null && 1==Integer.valueOf(chargeKind.toString());
            if (isGrooming) {
                for (ChargeComboVO vo : vPage.getList()) {
                    QueryWrapper<ComboFeeItemEntity> relQuery = new QueryWrapper<>();
                    relQuery.eq("combo_id", vo.getId());
                    List<ComboFeeItemEntity> relList = comboFeeItemService.list(relQuery);
                    if (!relList.isEmpty()) {
                        List<Long> itemIds = relList.stream()
                            .map(ComboFeeItemEntity::getItemId).collect(java.util.stream.Collectors.toList());
                        List<FeeItemEntity> items = feeItemService.listByIds(itemIds);
                        List<FeeItemVO> feeItemVOs = items.stream().map(item -> {
                            FeeItemVO itemVO = FeeItemWrapper.build().entityVO(item);
                            relList.stream()
                                .filter(r -> r.getItemId().equals(item.getId()))
                                .findFirst()
                                .ifPresent(r -> {
                                    itemVO.setMandatory(r.getMandatory());
                                    itemVO.setQuantity(r.getQuantity());
                                });
                            return itemVO;
                        }).collect(java.util.stream.Collectors.toList());
                        vo.setFeeItemVoList(feeItemVOs);
                    }
                }
            }
        }
        
        return R.data(vPage);
    }

    /**
     * 列表（旧接口兼容）
     */
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:list')")
    public R<VPage<ChargeComboVO>> browse(@RequestParam Map<String, Object> params){
        return list(params);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:detail')")
    public R<ChargeComboVO> detail(@PathVariable("id") Long id){
        ChargeComboEntity chargeCombo = chargeComboService.getById(id);
        ChargeComboVO vo = ChargeComboWrapper.build().entityVO(chargeCombo);
        if (vo != null) {
            // 关联查询收费类目列表
            QueryWrapper<ComboFeeItemEntity> relQuery = new QueryWrapper<>();
            relQuery.eq("combo_id", id);
            List<ComboFeeItemEntity> relList = comboFeeItemService.list(relQuery);
            if (!relList.isEmpty()) {
                List<Long> itemIds = relList.stream()
                    .map(ComboFeeItemEntity::getItemId).collect(java.util.stream.Collectors.toList());
                List<FeeItemEntity> items = feeItemService.listByIds(itemIds);
                // 合并 mandatory/quantity 字段
                List<FeeItemVO> feeItemVOs = items.stream().map(item -> {
                    FeeItemVO itemVO = FeeItemWrapper.build().entityVO(item);
                    relList.stream()
                        .filter(r -> r.getItemId().equals(item.getId()))
                        .findFirst()
                        .ifPresent(r -> {
                            itemVO.setMandatory(r.getMandatory());
                            itemVO.setQuantity(r.getQuantity());
                        });
                    return itemVO;
                }).collect(java.util.stream.Collectors.toList());
                vo.setFeeItemVoList(feeItemVOs);
            }
        }
        return R.data(vo);
    }

    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:submit')")
    public R<ChargeComboVO> submit(@Valid @RequestBody ChargeComboVO chargeCombo){
        if(chargeCombo.getId() == null) {
            chargeCombo.setCreateAt(new Date());
            chargeCombo.setIsDelete(0);
            if(chargeCombo.getStatus() == null) {
                chargeCombo.setStatus(1);
            }
        }
        chargeCombo.setUpdateAt(new Date());
        if(!chargeComboService.saveOrUpdate(chargeCombo)) {
            return R.error("数据错误");
        }
        // 同步保存套餐-费用项关联关系
        if(chargeCombo.getFeeItemVoList() != null) {
            // 删除旧关联
            QueryWrapper<ComboFeeItemEntity> delQuery = new QueryWrapper<>();
            delQuery.eq("combo_id", chargeCombo.getId());
            comboFeeItemService.remove(delQuery);
            // 插入新关联
            List<ComboFeeItemEntity> relList = chargeCombo.getFeeItemVoList().stream()
                .map(item -> {
                    ComboFeeItemEntity rel = new ComboFeeItemEntity();
                    rel.setComboId(chargeCombo.getId());
                    rel.setItemId(item.getId());
                    rel.setMandatory(item.getMandatory() != null ? item.getMandatory() : 0);
                    rel.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1);
                    rel.setCreateAt(new Date());
                    return rel;
                }).collect(java.util.stream.Collectors.toList());
            if(!relList.isEmpty()) {
                comboFeeItemService.saveBatch(relList);
            }
        }
        return R.ok(chargeCombo);
    }

    /**
     * 启用/禁用
     */
    @Operation(summary = "启用禁用")
    @PostMapping("/toggle/{id}")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:submit')")
    public R<Boolean> toggle(@PathVariable("id") Long id, @RequestBody Map<String, Object> body){
        Object statusVal = body.get("status");
        if(statusVal == null) {
            // 兼容前端传 enabled 字段（Boolean）
            Object enabledVal = body.get("enabled");
            statusVal = Boolean.TRUE.equals(enabledVal) ? 1 : 0;
        }
        UpdateWrapper<ChargeComboEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("status", statusVal).set("update_at", new Date());
        return R.ok(chargeComboService.update(wrapper));
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:chargecombo:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(chargeComboService.removeByIds(Arrays.asList(ids)));
    }

}
