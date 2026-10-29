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
import com.xiaohucare.app.care.wms.entity.BatchEntity;
import com.xiaohucare.app.care.wms.service.BatchService;
import com.xiaohucare.app.care.wms.vo.BatchVO;
import com.xiaohucare.app.care.wms.wrapper.BatchWrapper;
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
 * 库存批次表
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
@RestController
@RequestMapping("intra/wms/batch")
public class BatchController extends BaseController{
    @Autowired
    private BatchService batchService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('wms:batch:list')")
    public R<VPage<BatchVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<BatchEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), BatchEntity.class);
        
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<BatchEntity> pages = batchService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(BatchWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('wms:batch:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<BatchEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), BatchEntity.class);
         
         queryWrapper.select("count(id) as totalCount");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=batchService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('wms:batch:export')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<BatchEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				BatchEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<BatchEntity> batchList = batchService.list(queryWrapper);
		ExcelUtil<BatchEntity> util = new ExcelUtil<>(BatchEntity.class);
		util.exportExcel(response, batchList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('wms:batch:query')")
    public R<BatchVO> detail(@PathVariable("id") Long id){
		BatchEntity batch = batchService.getById(id);
	    return R.data(BatchWrapper.build().entityVO(batch));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasAnyPermi('wms:batch:add,wms:batch:edit')")
    public R<BatchVO> submit(@Valid @RequestBody BatchVO batch){
   		if(batch.getId()==null) {
            batch.setCreateAt(new Date());
            batch.setIsDelete(0);
        } 
        batch.setUpdateAt(new Date());
    	if(batchService.saveOrUpdate(batch)) {
    		 return R.ok(batch);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('wms:batch:remove')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(batchService.removeByIds(Arrays.asList(ids)));
    }

}
