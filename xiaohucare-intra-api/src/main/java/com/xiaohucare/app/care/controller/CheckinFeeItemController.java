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
import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.service.CheckinFeeItemService;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.app.care.wrapper.CheckinFeeItemWrapper;
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
 * 入住登记费用项表（一人一单，挂在 e_care_checkin.id 上）
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@RestController
@RequestMapping("intra/hotel/checkinfeeitem")
public class CheckinFeeItemController extends BaseController{
    @Autowired
    private CheckinFeeItemService checkinFeeItemService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:list')")
    public R<VPage<CheckinFeeItemVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<CheckinFeeItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), CheckinFeeItemEntity.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<CheckinFeeItemEntity> pages = checkinFeeItemService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(CheckinFeeItemWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<CheckinFeeItemEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), CheckinFeeItemEntity.class);
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=checkinFeeItemService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<CheckinFeeItemEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				CheckinFeeItemEntity.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<CheckinFeeItemEntity> checkinFeeItemList = checkinFeeItemService.list(queryWrapper);
		ExcelUtil<CheckinFeeItemEntity> util = new ExcelUtil<>(CheckinFeeItemEntity.class);
		util.exportExcel(response, checkinFeeItemList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:detail')")
    public R<CheckinFeeItemVO> detail(@PathVariable("id") Long id){
		CheckinFeeItemEntity checkinFeeItem = checkinFeeItemService.getById(id);
	    return R.data(CheckinFeeItemWrapper.build().entityVO(checkinFeeItem));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:submit')")
    public R<CheckinFeeItemVO> submit(@Valid @RequestBody CheckinFeeItemVO checkinFeeItem){
   		if(checkinFeeItem.getId()==null) {
            checkinFeeItem.setCreateAt(new Date());
            checkinFeeItem.setIsDelete(0);
        } 
    	if(checkinFeeItemService.saveOrUpdate(checkinFeeItem)) {
    		 return R.ok(checkinFeeItem);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:checkinpetfeeitem:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(checkinFeeItemService.removeByIds(Arrays.asList(ids)));
    }

}
