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
import com.xiaohucare.app.care.entity.ResidentCardPackageEntity;
import com.xiaohucare.app.care.service.ResidentCardPackageService;
import com.xiaohucare.app.care.vo.ResidentCardPackageVO;
import com.xiaohucare.app.care.wrapper.ResidentCardPackageWrapper;
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
 * 会员卡套餐表
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@RestController
@RequestMapping("intra/care/residentcardpackage")
public class ResidentCardPackageController extends BaseController{
    @Autowired
    private ResidentCardPackageService petOwnerCardPackageService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "套餐名称"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:list')")
    public R<VPage<ResidentCardPackageVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ResidentCardPackageEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"), ResidentCardPackageEntity.class);
        queryWrapper.orderByAsc("sort_order").orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
        // 门店隔离：登录账号有门店时只看本店套餐
        queryWrapper.eq("is_delete", 0);
        IPage<ResidentCardPackageEntity> pages = petOwnerCardPackageService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ResidentCardPackageWrapper.build().toVPageVO(pages));
    }

    /**
     * 启用的套餐列表（下拉选择）
     */
    @GetMapping("/enabledList")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:list')")
    public R<List<ResidentCardPackageVO>> enabledList() {
        QueryWrapper<ResidentCardPackageEntity> qw = new QueryWrapper<>();
        qw.eq("status", 1).eq("is_delete", 0);
        // 门店隔离：开卡 / 充值下拉只展示本店启用中的套餐
        qw.orderByAsc("sort_order").orderByDesc("id");
        List<ResidentCardPackageEntity> list = petOwnerCardPackageService.list(qw);
        return R.data(ResidentCardPackageWrapper.build().listVO(list));
    }

    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) {
        QueryWrapper<ResidentCardPackageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(id) as total");
        queryWrapper.eq("is_delete", 0);
        Map<String, Object> stat = petOwnerCardPackageService.getMap(queryWrapper);
        return R.data(stat);
    }

    @Operation(summary = "导出")
	@Log(title = "套餐导出", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:residentcardpackage:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ResidentCardPackageEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"), ResidentCardPackageEntity.class);
		queryWrapper.orderByDesc("id");
		 List<ResidentCardPackageEntity> list = petOwnerCardPackageService.list(queryWrapper);
		ExcelUtil<ResidentCardPackageEntity> util = new ExcelUtil<>(ResidentCardPackageEntity.class);
		util.exportExcel(response, list, "套餐数据");
	}

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:detail')")
    public R<ResidentCardPackageVO> detail(@PathVariable("id") Long id){
		ResidentCardPackageEntity petOwnerCardPackage = petOwnerCardPackageService.getById(id);
	    return R.data(ResidentCardPackageWrapper.build().entityVO(petOwnerCardPackage));
    }

    /**
     * 更新套餐状态
     */
    @Operation(summary = "更新套餐状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:submit')")
    public R<Boolean> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        ResidentCardPackageEntity pkg = petOwnerCardPackageService.getById(id);
        if (pkg == null) return R.error("套餐不存在");
        pkg.setStatus(status);
        pkg.setUpdateAt(new Date());
        return R.ok(petOwnerCardPackageService.updateById(pkg));
    }

     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:submit')")
    public R<ResidentCardPackageVO> submit(@Valid @RequestBody ResidentCardPackageVO petOwnerCardPackage){
   		if(petOwnerCardPackage.getId()==null) {
            petOwnerCardPackage.setCreateAt(new Date());
            petOwnerCardPackage.setIsDelete(0);
            if(petOwnerCardPackage.getStatus() == null) {
                petOwnerCardPackage.setStatus(1);
            }
        } 
        petOwnerCardPackage.setUpdateAt(new Date());
    	if(petOwnerCardPackageService.saveOrUpdate(petOwnerCardPackage)) {
    		 return R.ok(petOwnerCardPackage);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:residentcardpackage:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(petOwnerCardPackageService.removeByIds(Arrays.asList(ids)));
    }

}

