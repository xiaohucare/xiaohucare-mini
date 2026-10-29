package com.xiaohucare.plat.web.controller.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohucare.common.config.XiaohucareConfig;
import com.xiaohucare.common.constant.CacheConstants;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.AjaxResult;
import com.xiaohucare.framework.system.domain.SysCache;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 缓存监控
 * 
 * @author xiaohucare
 */
@Tag(name = "缓存监控")
@RestController
@RequestMapping("/monitor/cache")
public class CacheController
{
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private XiaohucareConfig xiaohucareConfig;
    /**
     * 获取带项目名称前缀的key
     * @param key 原始key
     * @return 带前缀的key
     */
    private String getKeyWithPrefix(String key) {
        String projectName = xiaohucareConfig.getName();
        if (projectName == null || projectName.trim().isEmpty()) {
            projectName = "xiaohucare";
        }
        return projectName + ":" + key;
    }
    private List<SysCache> caches;
    
    @PostConstruct
    public void init() {
        caches = new ArrayList<SysCache>();
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.LOGIN_TOKEN_KEY), "用户信息"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.SYS_CONFIG_KEY), "配置信息"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.SYS_DICT_KEY), "数据字典"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.CAPTCHA_CODE_KEY), "验证码"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.REPEAT_SUBMIT_KEY), "防重提交"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.RATE_LIMIT_KEY), "限流处理"));
        caches.add(new SysCache(getKeyWithPrefix(CacheConstants.PWD_ERR_CNT_KEY), "密码错误次数"));
    }

    @Operation(summary = "获取缓存监控信息")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    @Operation(summary = "获取缓存名称列表")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public AjaxResult cache()
    {
        return AjaxResult.success(caches);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName)
    {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        return AjaxResult.success(new TreeSet<>(cacheKeys));
    }

    @Operation(summary = "获取缓存值")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCache sysCache = new SysCache(cacheName, cacheKey, cacheValue);
        return AjaxResult.success(sysCache);
    }

    @Operation(summary = "根据缓存名称删除缓存")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName)
    {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.success();
    }
    
    @Operation(summary = "根据缓存名称删除缓存")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey)
    {
        redisTemplate.delete(cacheKey);
        return AjaxResult.success();
    }

    @Operation(summary = "删除所有缓存")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll()
    {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.success();
    }
}
