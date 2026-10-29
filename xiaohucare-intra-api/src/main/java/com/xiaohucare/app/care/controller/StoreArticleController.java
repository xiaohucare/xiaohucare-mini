package com.xiaohucare.app.care.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.xiaohucare.app.care.entity.StoreArticleEntity;
import com.xiaohucare.app.care.service.StoreArticleService;
import com.xiaohucare.app.care.vo.StoreArticleVO;
import com.xiaohucare.app.care.wrapper.StoreArticleWrapper;
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
import lombok.extern.slf4j.Slf4j;
/**
 * 门店文章报道表
 * @author xiaohucare
 * @date 2026-04-17 22:48:48
 */
@Slf4j
@RestController
@RequestMapping("intra/care/storearticle")
public class StoreArticleController extends BaseController{
    @Autowired
    private StoreArticleService storeArticleService;
    /**
     * 列表
     */
    @Operation(summary = "列表")
    @Parameters({
                @Parameter(name = "title", description = "文章标题"),
                @Parameter(name = "category", description = "文章分类"),
                @Parameter(name = "status", description = "状态")
        })
    @GetMapping("/browse")
    @PreAuthorize("@ss.hasPermi('care:article:list')")
    public R<VPage<StoreArticleVO>> browse(@RequestParam Map<String, Object> params){
        QueryWrapper<StoreArticleEntity> queryWrapper = new QueryWrapper<>();
          
        // 文章标题模糊查询
        if (params.containsKey("title") && StringUtils.isNotBlank(params.get("title").toString())) {
            queryWrapper.like("title", params.get("title"));
        }
        
        // 文章分类精确查询
        if (params.containsKey("category") && StringUtils.isNotBlank(params.get("category").toString())) {
            queryWrapper.eq("category", params.get("category"));
        }
        
        // 状态精确查询
        if (params.containsKey("status") && StringUtils.isNotBlank(params.get("status").toString())) {
            queryWrapper.eq("status", params.get("status"));
        }
        
        // 按置顶和排序字段排序
        queryWrapper.orderByDesc("is_top");
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("create_at");
        
        IPage<StoreArticleEntity> pages = storeArticleService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(StoreArticleWrapper.build().toVPageVO(pages));
    }
    @GetMapping("/listStat")
    @PreAuthorize("@ss.hasPermi('care:article:list')")
    public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException {
        QueryWrapper<StoreArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(id) as total, sum(view_count) as total_views, sum(like_count) as total_likes");
        
        // 状态筛选
        if (params.containsKey("status") && StringUtils.isNotBlank(params.get("status").toString())) {
            queryWrapper.eq("status", params.get("status"));
        }
        
        // 分类筛选
        if (params.containsKey("category") && StringUtils.isNotBlank(params.get("category").toString())) {
            queryWrapper.eq("category", params.get("category"));
        }
        
        Map<String, Object> stat = storeArticleService.getMap(queryWrapper);
        return R.data(stat);
    }
    @Operation(summary = "导出")
    @Log(title = "文章报道管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('care:article:export')")
    @PostMapping("/toExport")
    public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        QueryWrapper<StoreArticleEntity> queryWrapper = new QueryWrapper<>();
          
        // 文章标题模糊查询
        if (params.containsKey("title") && StringUtils.isNotBlank(params.get("title").toString())) {
            queryWrapper.like("title", params.get("title"));
        }
        
        // 文章分类精确查询
        if (params.containsKey("category") && StringUtils.isNotBlank(params.get("category").toString())) {
            queryWrapper.eq("category", params.get("category"));
        }
        
        queryWrapper.orderByDesc("is_top");
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("create_at");
        
        List<StoreArticleEntity> storeArticleList = storeArticleService.list(queryWrapper);
        ExcelUtil<StoreArticleEntity> util = new ExcelUtil<>(StoreArticleEntity.class);
        util.exportExcel(response, storeArticleList, "文章报道数据");
    }
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    @PreAuthorize("@ss.hasPermi('care:article:detail')")
    public R<StoreArticleVO> detail(@PathVariable("id") Long id){
		StoreArticleEntity storeArticle = storeArticleService.getById(id);
	    return R.data(StoreArticleWrapper.build().entityVO(storeArticle));
    }
    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('care:article:submit')")
    public R<Boolean> submit(@Valid @RequestBody StoreArticleVO storeArticle){
        try {
            if(storeArticle.getId() == null) {
                storeArticle.setCreateAt(new Date());
                storeArticle.setIsDelete(0);
                // 如果是发布状态，设置发布时间
                if(storeArticle.getStatus() != null && storeArticle.getStatus() == 1) {
                    storeArticle.setPublishTime(new Date());
                }
            } else {
                // 更新时，如果从非发布状态变为发布状态，设置发布时间
                if(storeArticle.getStatus() != null && storeArticle.getStatus() == 1) {
                    StoreArticleEntity existArticle = storeArticleService.getById(storeArticle.getId());
                    if(existArticle != null && (existArticle.getStatus() == null || existArticle.getStatus() != 1)) {
                        storeArticle.setPublishTime(new Date());
                    }
                }
            }
            
            storeArticle.setUpdateAt(new Date());
            
            if(storeArticleService.saveOrUpdate(storeArticle)) {
                return R.ok(true);
            }
            return R.error("数据保存失败");
        } catch (Exception e) {
            log.error("保存文章失败", e);
            return R.error("数据保存失败：" + e.getMessage());
        }
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('care:article:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        try {
            return R.ok(storeArticleService.removeByIds(Arrays.asList(ids)));
        } catch (Exception e) {
            log.error("删除文章失败", e);
            return R.error("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新状态（发布/下架）
     */
    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermi('care:article:status')")
    public R<Boolean> updateStatus(@RequestBody Map<String, Object> params){
        try {
            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());
            
            StoreArticleEntity article = storeArticleService.getById(id);
            if(article == null) {
                return R.error("文章不存在");
            }
            
            article.setStatus(status);
            article.setUpdateAt(new Date());
            
            // 如果是发布，设置发布时间
            if(status == 1 && article.getPublishTime() == null) {
                article.setPublishTime(new Date());
            }
            
            if(storeArticleService.updateById(article)) {
                String action = status == 1 ? "发布" : "下架";
                log.info("文章{}成功: id={}, title={}", action, id, article.getTitle());
                return R.ok(true);
            }
            return R.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新文章状态失败", e);
            return R.error("状态更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取文章分类列表
     */
    @Operation(summary = "获取文章分类列表")
    @GetMapping("/categories")
    public R<List<Map<String, String>>> getCategories(){
        List<Map<String, String>> categories = new ArrayList<>();
        
        Map<String, String> news = new LinkedHashMap<>();
        news.put("value", "news");
        news.put("label", "门店新闻");
        categories.add(news);
        
        Map<String, String> activity = new LinkedHashMap<>();
        activity.put("value", "activity");
        activity.put("label", "活动报道");
        categories.add(activity);
        
        Map<String, String> knowledge = new LinkedHashMap<>();
        knowledge.put("value", "knowledge");
        knowledge.put("label", "宠物知识");
        categories.add(knowledge);
        
        Map<String, String> case_ = new LinkedHashMap<>();
        case_.put("value", "case");
        case_.put("label", "客户案例");
        categories.add(case_);
        
        Map<String, String> promotion = new LinkedHashMap<>();
        promotion.put("value", "promotion");
        promotion.put("label", "宣传文章");
        categories.add(promotion);
        
        return R.data(categories);
    }

}
