package com.xiaohucare.common.core.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.xiaohucare.common.config.XiaohucareConfig;

/**
 * spring redis 工具类
 *
 * @author xiaohucare
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    
    @Autowired
    public RedisTemplate redisTemplate;
    
    /**
     * 项目名称前缀
     */
    private static String projectNamePrefix = "";

    /**
     * 初始化项目名称前缀
     */
    @Autowired
    public void setXiaohucareConfig(XiaohucareConfig xiaohucareConfig) {
        if (xiaohucareConfig != null && xiaohucareConfig.getName() != null) {
            projectNamePrefix = xiaohucareConfig.getName() + ":";
        }
    }

    /**
     * 添加项目名称前缀到key
     * 
     * @param key 原始key
     * @return 添加前缀后的key
     */
    private String addProjectNamePrefix(final String key) {
        return projectNamePrefix + key;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        try {
            redisTemplate.opsForValue().set(addProjectNamePrefix(key), value);
        } catch (Exception e) {
            log.error("设置缓存对象异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        try {
            redisTemplate.opsForValue().set(addProjectNamePrefix(key), value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("设置缓存对象异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        try {
            return redisTemplate.expire(addProjectNamePrefix(key), timeout, unit);
        } catch (Exception e) {
            log.error("设置缓存过期时间异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key)
    {
        try {
            return redisTemplate.getExpire(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("获取缓存过期时间异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        try {
            return redisTemplate.hasKey(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("判断缓存键是否存在异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        try {
            ValueOperations<String, T> operation = redisTemplate.opsForValue();
            return operation.get(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("获取缓存对象异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        try {
            return redisTemplate.delete(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("删除缓存对象异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        try {
            return redisTemplate.delete(collection) > 0;
        } catch (Exception e) {
            log.error("删除缓存对象集合异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        try {
            Long count = redisTemplate.opsForList().rightPushAll(addProjectNamePrefix(key), dataList);
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("设置缓存List异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        try {
            return redisTemplate.opsForList().range(addProjectNamePrefix(key), 0, -1);
        } catch (Exception e) {
            log.error("获取缓存List异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        try {
            BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(addProjectNamePrefix(key));
            Iterator<T> it = dataSet.iterator();
            while (it.hasNext())
            {
                setOperation.add(it.next());
            }
            return setOperation;
        } catch (Exception e) {
            log.error("设置缓存Set异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        try {
            return redisTemplate.opsForSet().members(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("获取缓存Set异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        try {
            if (dataMap != null) {
                redisTemplate.opsForHash().putAll(addProjectNamePrefix(key), dataMap);
            }
        } catch (Exception e) {
            log.error("设置缓存Map异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        try {
            return redisTemplate.opsForHash().entries(addProjectNamePrefix(key));
        } catch (Exception e) {
            log.error("获取缓存Map异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        try {
            redisTemplate.opsForHash().put(addProjectNamePrefix(key), hKey, value);
        } catch (Exception e) {
            log.error("设置缓存Map值异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        try {
            HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
            return opsForHash.get(addProjectNamePrefix(key), hKey);
        } catch (Exception e) {
            log.error("获取缓存Map值异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        try {
            return redisTemplate.opsForHash().multiGet(addProjectNamePrefix(key), hKeys);
        } catch (Exception e) {
            log.error("获取缓存Map多个值异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        try {
            return redisTemplate.opsForHash().delete(addProjectNamePrefix(key), hKey) > 0;
        } catch (Exception e) {
            log.error("删除缓存Map值异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        try {
            return redisTemplate.keys(addProjectNamePrefix(pattern));
        } catch (Exception e) {
            log.error("获取缓存键列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}