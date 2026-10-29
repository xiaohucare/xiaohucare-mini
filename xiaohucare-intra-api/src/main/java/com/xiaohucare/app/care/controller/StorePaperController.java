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
import com.xiaohucare.app.care.entity.StorePaperEntity;
import com.xiaohucare.app.care.service.StorePaperService;
import com.xiaohucare.app.care.vo.StorePaperVO;
import com.xiaohucare.app.care.wrapper.StorePaperWrapper;
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
 * 企业公报
 * @author xiaohucare
 * @date 2026-04-15 19:01:57
 */
@RestController
@RequestMapping("intra/care/storepaper")
public class StorePaperController extends BaseController{
    @Autowired
    private StorePaperService storePaperService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
		        @Parameter(name = "name", description = "标题"),
		})
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:paper:list')")
    public R<VPage<StorePaperVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<StorePaperEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), StorePaperEntity.class);
         
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<StorePaperEntity> pages = storePaperService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(StorePaperWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:paper:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<StorePaperEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), StorePaperEntity.class);
        
         queryWrapper.select("count(id) as totalCount");
           
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=storePaperService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:paper:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<StorePaperEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				StorePaperEntity.class);
		queryWrapper.orderByDesc("id");
          
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<StorePaperEntity> storePaperList = storePaperService.list(queryWrapper);
		ExcelUtil<StorePaperEntity> util = new ExcelUtil<>(StorePaperEntity.class);
		util.exportExcel(response, storePaperList, "参数数据");
	}
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:paper:detail')")
    public R<StorePaperVO> detail(@PathVariable("id") Integer id){
		StorePaperEntity storePaper = storePaperService.getById(id);
	    return R.data(StorePaperWrapper.build().entityVO(storePaper));
    }
     /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:paper:submit')")
    public R<StorePaperVO> submit(@Valid @RequestBody StorePaperVO storePaper){
   		if(storePaper.getId()==null) {
            storePaper.setCreateAt(new Date());
            storePaper.setIsDelete(0);
        } 
        storePaper.setUpdateAt(new Date());
    	if(storePaperService.saveOrUpdate(storePaper)) {
    		 return R.ok(storePaper);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:paper:delete')")
    public R<Boolean> delete(@RequestBody Integer[] ids){
        return R.ok(storePaperService.removeByIds(Arrays.asList(ids)));
    }

}
