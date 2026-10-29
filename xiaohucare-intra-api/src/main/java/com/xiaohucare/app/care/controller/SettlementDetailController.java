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
import com.xiaohucare.app.care.entity.SettlementDetailEntity;
import com.xiaohucare.app.care.service.SettlementDetailService;
import com.xiaohucare.app.care.vo.SettlementDetailVO;
import com.xiaohucare.app.care.wrapper.SettlementDetailWrapper;
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
 * 结算明细表
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@RestController
@RequestMapping("intra/hotel/settlementdetail")
public class SettlementDetailController extends BaseController{
    @Autowired
    private SettlementDetailService settlementDetailService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:settlementdetail:list')")
    public R<VPage<SettlementDetailVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<SettlementDetailEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SettlementDetailEntity.class);
//        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<SettlementDetailEntity> pages = settlementDetailService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SettlementDetailWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:settlementdetail:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SettlementDetailEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SettlementDetailEntity.class);
//         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=settlementDetailService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:settlementdetail:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SettlementDetailEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SettlementDetailEntity.class);
//		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SettlementDetailEntity> settlementDetailList = settlementDetailService.list(queryWrapper);
		ExcelUtil<SettlementDetailEntity> util = new ExcelUtil<>(SettlementDetailEntity.class);
		util.exportExcel(response, settlementDetailList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:settlementdetail:detail')")
    public R<SettlementDetailVO> detail(@PathVariable("id") Long id){
		SettlementDetailEntity settlementDetail = settlementDetailService.getById(id);
	    return R.data(SettlementDetailWrapper.build().entityVO(settlementDetail));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:settlementdetail:submit')")
    public R<SettlementDetailVO> submit(@Valid @RequestBody SettlementDetailVO settlementDetail){
   		if(settlementDetail.getId()==null) {
            settlementDetail.setCreateAt(new Date());
            settlementDetail.setIsDelete(0);
        } 
        settlementDetail.setUpdateAt(new Date());
    	if(settlementDetailService.saveOrUpdate(settlementDetail)) {
    		 return R.ok(settlementDetail);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:settlementdetail:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(settlementDetailService.removeByIds(Arrays.asList(ids)));
    }

}
