package com.xiaohucare.app.care.controller;
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
import com.xiaohucare.app.care.entity.ComboFeeItemEntity;
import com.xiaohucare.app.care.service.ComboFeeItemService;
import com.xiaohucare.app.care.vo.ComboFeeItemVO;
import com.xiaohucare.app.care.wrapper.ComboFeeItemWrapper;
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
 * 套餐费用项关联表
 * @author xiaohucare
 * @date 2026-03-21 13:47:57
 */
@RestController
@RequestMapping("intra/care/combofeeitem")
public class ComboFeeItemController extends BaseController{
    @Autowired
    private ComboFeeItemService comboFeeItemService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:combofeeitem:list')")
    public R<VPage<ComboFeeItemVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<ComboFeeItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), ComboFeeItemEntity.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<ComboFeeItemEntity> pages = comboFeeItemService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(ComboFeeItemWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:combofeeitem:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<ComboFeeItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), ComboFeeItemEntity.class);
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=comboFeeItemService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:combofeeitem:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<ComboFeeItemEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				ComboFeeItemEntity.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<ComboFeeItemEntity> comboFeeItemList = comboFeeItemService.list(queryWrapper);
		ExcelUtil<ComboFeeItemEntity> util = new ExcelUtil<>(ComboFeeItemEntity.class);
		util.exportExcel(response, comboFeeItemList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:combofeeitem:detail')")
    public R<ComboFeeItemVO> detail(@PathVariable("id") Long id){
		ComboFeeItemEntity comboFeeItem = comboFeeItemService.getById(id);
	    return R.data(ComboFeeItemWrapper.build().entityVO(comboFeeItem));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:combofeeitem:submit')")
    public R<ComboFeeItemVO> submit(@Valid @RequestBody ComboFeeItemVO comboFeeItem){
   		if(comboFeeItem.getId()==null) {
            comboFeeItem.setCreateAt(new Date());
            comboFeeItem.setIsDelete(0);
        } 
        comboFeeItem.setUpdateAt(new Date());
    	if(comboFeeItemService.saveOrUpdate(comboFeeItem)) {
    		 return R.ok(comboFeeItem);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:combofeeitem:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(comboFeeItemService.removeByIds(Arrays.asList(ids)));
    }

}
