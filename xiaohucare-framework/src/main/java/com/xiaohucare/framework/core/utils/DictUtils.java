package com.xiaohucare.framework.core.utils;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.xiaohucare.common.constant.CacheConstants;
import com.xiaohucare.common.core.redis.RedisUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.common.utils.spring.SpringUtils;
import com.xiaohucare.framework.system.domain.SysDictData;

/**
 * 字典工具类
 * 
 * @author xiaohucare
 */
public class DictUtils
{
    private static final Logger log = LoggerFactory.getLogger(DictUtils.class);
    
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     * 
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisUtil.class).set(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     * 
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key)
    {
        try {
            Object cacheObject = SpringUtils.getBean(RedisUtil.class).get(getCacheKey(key));
            if (StringUtils.isNotNull(cacheObject))
            {
                // 检查对象类型并进行适当转换
                if (cacheObject instanceof List) {
                    // 如果已经是List类型，直接返回
                    return (List<SysDictData>) cacheObject;
                } else if (cacheObject instanceof JSONArray) {
                    // JSONArray类型转换为List
                    JSONArray arrayCache = (JSONArray) cacheObject;
                    return arrayCache.toList(SysDictData.class);
                } else {
                    // 其他类型（如String），尝试解析为JSON数组
                    String jsonString;
                    if (cacheObject instanceof String) {
                        jsonString = (String) cacheObject;
                    } else {
                        jsonString = JSON.toJSONString(cacheObject);
                    }
                    
                    // 检查是否是简单的字符串（如"6"），如果是则返回空列表
                    if (jsonString != null && jsonString.length() > 0 && 
                        !jsonString.startsWith("[") && !jsonString.startsWith("{")) {
                        log.warn("Redis中存储的字典数据格式不正确，期望JSON数组但得到简单字符串: {}", jsonString);
                        return null;
                    }
                    
                    // 尝试解析为JSON数组
                    try {
                        return JSONArray.parseArray(jsonString, SysDictData.class);
                    } catch (Exception e) {
                        log.error("解析字典缓存JSON失败: {}, 数据: {}", e.getMessage(), jsonString);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取字典缓存异常，key: {}, 异常信息: {}", key, e.getMessage(), e);
            // 如果缓存数据损坏，删除缓存并返回null，让系统重新从数据库加载
            removeDictCache(key);
        }
        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     * 
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        if (StringUtils.isEmpty(dictValue))
        {
            return StringUtils.EMPTY;
        }
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     * 
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        if (StringUtils.isEmpty(dictLabel))
        {
            return StringUtils.EMPTY;
        }
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     * 
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictValue))
        {
            for (SysDictData dict : datas)
            {
                for (String value : dictValue.split(separator))
                {
                    if (value.equals(dict.getDictValue()))
                    {
                        propertyString.append(dict.getDictLabel()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictValue.equals(dict.getDictValue()))
                {
                    return dict.getDictLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     * 
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictLabel))
        {
            for (SysDictData dict : datas)
            {
                for (String label : dictLabel.split(separator))
                {
                    if (label.equals(dict.getDictLabel()))
                    {
                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictLabel.equals(dict.getDictLabel()))
                {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型获取字典所有值
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictValues(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictValue()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 根据字典类型获取字典所有标签
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictLabels(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictLabel()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 删除指定字典缓存
     * 
     * @param key 字典键
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisUtil.class).del(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Set<Object> keys = SpringUtils.getBean(RedisUtil.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            SpringUtils.getBean(RedisUtil.class).del(keys.toArray(new String[0]));
        }
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
