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
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.service.SettlementService;
import com.xiaohucare.app.care.vo.SettlementVO;
import com.xiaohucare.app.care.wrapper.SettlementWrapper;
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
 * 结算表
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@RestController
@RequestMapping("intra/hotel/settlement")
public class SettlementController extends BaseController{
    @Autowired
    private SettlementService settlementService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:settlement:list')")
    public R<VPage<SettlementVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<SettlementEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SettlementEntity.class);
        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<SettlementEntity> pages = settlementService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SettlementWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:settlement:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SettlementEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SettlementEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=settlementService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:settlement:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SettlementEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SettlementEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SettlementEntity> settlementList = settlementService.list(queryWrapper);
		ExcelUtil<SettlementEntity> util = new ExcelUtil<>(SettlementEntity.class);
		util.exportExcel(response, settlementList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:settlement:detail')")
    public R<SettlementVO> detail(@PathVariable("id") Long id){
		SettlementEntity settlement = settlementService.getById(id);
	    return R.data(SettlementWrapper.build().entityVO(settlement));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:settlement:submit')")
    public R<SettlementVO> submit(@Valid @RequestBody SettlementVO settlement){
   		if(settlement.getId()==null) {
            settlement.setCreateAt(new Date());
            settlement.setIsDelete(0);
        } 
        settlement.setUpdateAt(new Date());
    	if(settlementService.saveOrUpdate(settlement)) {
    		 return R.ok(settlement);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:settlement:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(settlementService.removeByIds(Arrays.asList(ids)));
    }

}
