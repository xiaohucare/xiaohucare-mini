package com.xiaohucare.app.website.controller;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.StoreArticleEntity;
import com.xiaohucare.app.care.service.StoreArticleService;
import com.xiaohucare.app.care.vo.StoreArticleVO;
import com.xiaohucare.app.care.wrapper.StoreArticleWrapper;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("web/article/")
public class WebArticleController extends BaseController{
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
    /**
     * 信息
     */
    @Operation(summary = "信息")
    @GetMapping("/detail/{id}")
    public R<StoreArticleVO> detail(@PathVariable("id") Long id){
		StoreArticleEntity storeArticle = storeArticleService.getById(id);
	    return R.data(StoreArticleWrapper.build().entityVO(storeArticle));
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
