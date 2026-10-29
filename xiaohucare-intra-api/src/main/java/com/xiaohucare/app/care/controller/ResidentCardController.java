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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.ResidentCardEntity;
import com.xiaohucare.app.care.service.ResidentCardService;
import com.xiaohucare.app.care.vo.ResidentCardVO;
import com.xiaohucare.app.care.wrapper.ResidentCardWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
/**
 * 会员卡表
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@RestController
@RequestMapping("intra/care/residentcard")
public class ResidentCardController extends BaseController{
    @Autowired
    private ResidentCardService residentCardService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "cardNo", description = "卡号"),
		        @Parameter(name = "residentName", description = "客户姓名"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:residentcard:list')")
    public R<VPage<ResidentCardVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ResidentCardEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("cardNo", "residentName"), ResidentCardEntity.class);
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("cardNo")), "card_no", params.get("cardNo"));
        queryWrapper.like(StringUtils.isNotNull(params.get("residentName")), "resident_name", params.get("residentName"));
        if (StringUtils.isNotNull(params.get("cardLevel"))) {
            queryWrapper.eq("card_level", params.get("cardLevel"));
        }
        if (StringUtils.isNotNull(params.get("residentId"))) {
            queryWrapper.eq("resident_id", params.get("residentId"));
        }
        if (StringUtils.isNotNull(params.get("status"))) {
            queryWrapper.eq("status", params.get("status"));
        }
        // 门店隔离：登录账号有门店时只看本店会员卡
        queryWrapper.eq("is_delete", 0);
        IPage<ResidentCardEntity> pages = residentCardService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ResidentCardWrapper.build().toVPageVO(pages));
    }

    /**
     * 统计
     */
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:residentcard:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) {
        return R.data(residentCardService.getStat());
    }

    /**
     * 根据客户ID查询会员卡
     */
    @GetMapping("/byOwner/{residentId}")
    @PreAuthorize("@ss.hasPermi('care:residentcard:list') or @ss.hasPermi('resident:resident:list')")
    public R<ResidentCardVO> byOwner(@PathVariable("residentId") Long residentId) {
        QueryWrapper<ResidentCardEntity> qw = new QueryWrapper<>();
        qw.eq("resident_id", residentId).eq("is_delete", 0).eq("status", 1).last("LIMIT 1");
        ResidentCardEntity card = residentCardService.getOne(qw);
        return R.data(ResidentCardWrapper.build().entityVO(card));
    }

    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:residentcard:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ResidentCardEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("cardNo"), ResidentCardEntity.class);
		queryWrapper.orderByDesc("id");
		 List<ResidentCardEntity> list = residentCardService.list(queryWrapper);
		ExcelUtil<ResidentCardEntity> util = new ExcelUtil<>(ResidentCardEntity.class);
		util.exportExcel(response, list, "会员卡数据");
	}

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:residentcard:detail')")
    public R<ResidentCardVO> detail(@PathVariable("id") Long id){
		ResidentCardEntity petOwnerCard = residentCardService.getById(id);
	    return R.data(ResidentCardWrapper.build().entityVO(petOwnerCard));
    }

    /**
     * 开卡
     */
    @Operation(summary = "开卡")
    @PostMapping("/open")
    @PreAuthorize("@ss.hasPermi('care:residentcard:submit')")
    public R<ResidentCardVO> open(@Valid @RequestBody ResidentCardVO petOwnerCard) {
        // 开卡归属当前门店（已有卡转为充值时以原卡门店为准）
        ResidentCardVO result = residentCardService.openCard(petOwnerCard);
        if (result != null && result.getId() != null) {
            return R.ok(result);
        }
        return R.error("开卡失败");
    }

    /**
     * 充値
     */
    @Operation(summary = "充値")
    @PostMapping("/recharge")
    @PreAuthorize("@ss.hasPermi('care:residentcard:submit')")
    public R<Boolean> recharge(@RequestBody Map<String, Object> params) {
        return residentCardService.recharge(params) ? R.ok(true) : R.error("充値失败");
    }

    /**
     * 消费
     */
    @Operation(summary = "消费")
    @PostMapping("/consume")
    @PreAuthorize("@ss.hasPermi('care:residentcard:submit')")
    public R<Boolean> consume(@RequestBody Map<String, Object> params) {
        return residentCardService.consume(params) ? R.ok(true) : R.error("消费失败");
    }

    /**
     * 挂失/解挂/注销
     */
    @Operation(summary = "修改卡状态")
    @PostMapping("/toggleStatus")
    @PreAuthorize("@ss.hasPermi('care:residentcard:submit')")
    public R<Boolean> toggleStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        return residentCardService.toggleStatus(id, status) ? R.ok(true) : R.error("操作失败");
    }

     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:residentcard:submit')")
    public R<ResidentCardVO> submit(@Valid @RequestBody ResidentCardVO petOwnerCard){
   		if(petOwnerCard.getId()==null) {
            petOwnerCard.setCreateAt(new Date());
            petOwnerCard.setIsDelete(0);
        } 
        petOwnerCard.setUpdateAt(new Date());
    	if(residentCardService.saveOrUpdate(petOwnerCard)) {
    		 return R.ok(petOwnerCard);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:residentcard:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(residentCardService.removeByIds(Arrays.asList(ids)));
    }

}

