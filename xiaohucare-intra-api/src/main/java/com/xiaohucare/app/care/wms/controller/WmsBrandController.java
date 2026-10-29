package com.xiaohucare.app.care.wms.controller;
import java.text.ParseException;
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
import com.xiaohucare.app.care.wms.entity.WmsBrandEntity;
import com.xiaohucare.app.care.wms.service.WmsBrandService;
import com.xiaohucare.app.care.wms.vo.WmsBrandVO;
import com.xiaohucare.app.care.wms.wrapper.WmsBrandWrapper;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.DateUtils;
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
 * 商品品牌表
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@RestController
@RequestMapping("intra/wms/brand")
public class WmsBrandController extends BaseController{
    @Autowired
    private WmsBrandService brandService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:brand:list')")
    public R<VPage<WmsBrandVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<WmsBrandEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), WmsBrandEntity.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
         
        IPage<WmsBrandEntity> pages = brandService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(WmsBrandWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:brand:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<WmsBrandEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), WmsBrandEntity.class);
         queryWrapper.select("count(id) as totalCount");
         
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=brandService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:brand:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<WmsBrandEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				WmsBrandEntity.class);
		queryWrapper.orderByDesc("id");
		 
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<WmsBrandEntity> brandList = brandService.list(queryWrapper);
		ExcelUtil<WmsBrandEntity> util = new ExcelUtil<>(WmsBrandEntity.class);
		util.exportExcel(response, brandList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:brand:query')")
    public R<WmsBrandVO> detail(@PathVariable("id") Long id){
		WmsBrandEntity brand = brandService.getById(id);
	    return R.data(WmsBrandWrapper.build().entityVO(brand));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:brand:add,wms:brand:edit')")
    public R<WmsBrandVO> submit(@Valid @RequestBody WmsBrandVO brand){
   		if(brand.getId()==null) {
            brand.setCreateAt(new Date());
            brand.setIsDelete(0);
        } 
        brand.setUpdateAt(new Date());
    	if(brandService.saveOrUpdate(brand)) {
    		 return R.ok(brand);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:brand:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(brandService.removeByIds(Arrays.asList(ids)));
    }

}
