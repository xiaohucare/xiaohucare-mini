package com.xiaohucare.plat.web.controller.system;
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
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;
import com.xiaohucare.framework.oss.utils.OssUtils;
import com.xiaohucare.framework.system.domain.SysOss;
import com.xiaohucare.framework.system.domain.vo.SysOssVO;
import com.xiaohucare.framework.system.service.SysOssService;
import com.xiaohucare.framework.system.wrapper.SysOssWrapper;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
/**
 * oss管理
 * @author xiaohucare
 * @date 2026-05-18 12:35:39
 */
@RestController
@RequestMapping("system/sysoss")
public class SysOssController extends BaseController{
    @Autowired
    private SysOssService sysOssService;
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:sysoss:list')")
    public R<VPage<SysOssVO>> list(@RequestParam Map<String, Object> params){
        QueryWrapper<SysOss> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SysOss.class);
        queryWrapper.orderByDesc("id");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        IPage<SysOss> pages = sysOssService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(SysOssWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('system:sysoss:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params)throws ParseException{
         QueryWrapper<SysOss> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("name"), SysOss.class);
         queryWrapper.select("count(id) as totalCount ");
         queryWrapper.like(StringUtils.isNotNull(params.get("name")),"name", params.get("name"));
        if(params.containsKey("createDate")) {
            queryWrapper.between("create_at",
                    DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
                    DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString()))
              );   
        }
        Map<String, Object>  stat=sysOssService.getMap(queryWrapper);
        return R.data(stat);
    }
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:sysoss:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<SysOss> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				SysOss.class);
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		 List<SysOss> sysOssList = sysOssService.list(queryWrapper);
		ExcelUtil<SysOss> util = new ExcelUtil<>(SysOss.class);
		util.exportExcel(response, sysOssList, "参数数据");
	}
    /**
     * 信息
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('system:sysoss:detail')")
    public R<SysOssVO> detail(@PathVariable("id") Long id){
		SysOss sysOss = sysOssService.getById(id);
		// 获取文件路径
		String filePath = sysOss.getOssId();
		// 兼容旧数据:如果路径不包含 upload/ 前缀,自动添加
		if (StrUtil.isNotEmpty(filePath) && !filePath.startsWith("upload/")) {
			filePath = "upload/" + filePath;
		}
		// 计算过期时间
		Integer expireSeconds=60*60*24;
		// 生成预览URL
		String previewUrl = OssUtils.getFileUrl(filePath, expireSeconds);
		SysOssVO sysOssVO= SysOssWrapper.build().entityVO(sysOss);
		sysOssVO.setPreviewUrl(previewUrl);
	    return R.data(sysOssVO);
    }
     /**
     * 保存&更新
     */
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('system:sysoss:submit')")
    public R<SysOssVO> submit(@Valid @RequestBody SysOssVO sysOss){
   		if(sysOss.getId()==null) {
            sysOss.setCreateAt(new Date());
            sysOss.setIsDelete(0);
        } 
    	if(sysOssService.saveOrUpdate(sysOss)) {
    		 return R.ok(sysOss);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('system:sysoss:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(sysOssService.removeByIds(Arrays.asList(ids)));
    }

}
