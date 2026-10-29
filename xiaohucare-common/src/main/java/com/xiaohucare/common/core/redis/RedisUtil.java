package com.xiaohucare.common.core.redis;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.xiaohucare.common.config.XiaohucareConfig;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisUtil {
	
	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    
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
    
    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Set<Object> keys(final String pattern)
    {
        String prefixedPattern = getKeyWithPrefix(pattern);
        return redisTemplate.keys(prefixedPattern);
    }
    
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            if(time>0){
                redisTemplate.expire(prefixedKey, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean expireAt(String key,Date dateTime){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.expireAt(prefixedKey, dateTime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.getExpire(prefixedKey,TimeUnit.SECONDS);
    }
    
    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.hasKey(prefixedKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                String prefixedKey = getKeyWithPrefix(key[0]);
                redisTemplate.delete(prefixedKey);
            }else{
                List<String> prefixedKeys = Stream.of(key).map(this::getKeyWithPrefix).collect(Collectors.toList());
//                redisTemplate.delete(prefixedKeys);
                for(String prefixedKey:prefixedKeys) {
                	redisTemplate.delete(prefixedKey);
                }
            }
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
    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        if(key==null) return null;
        String prefixedKey = getKeyWithPrefix(key);
        log.debug("=====prefixedKey====="+prefixedKey);
        return redisTemplate.opsForValue().get(prefixedKey);
    }
    public Object getNoPre(String key){
    	if(key==null) return null;
    	log.debug("=====getNoPre====="+key);
    	return redisTemplate.opsForValue().get(key);
    }
 
    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForValue().set(prefixedKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            if(time>0){
                redisTemplate.opsForValue().set(prefixedKey, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean set(String key,Object value,long time, TimeUnit unit){
    	try {
            String prefixedKey = getKeyWithPrefix(key);
    		if(time>0){
    			redisTemplate.opsForValue().set(prefixedKey, value, time,unit);
    		}else{
    			set(key, value);
    		}
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public boolean set(String key,Object value,Date dateTime){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            if(dateTime!=null){
                redisTemplate.opsForValue().set(prefixedKey, value);
                redisTemplate.expireAt(prefixedKey, dateTime);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForValue().increment(prefixedKey, delta);
    }
 
    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForValue().increment(prefixedKey, -delta);
    }
 
    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForHash().get(prefixedKey, item);
    }
 
    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForHash().entries(prefixedKey);
    }
 
    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForHash().putAll(prefixedKey, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForHash().putAll(prefixedKey, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForHash().put(prefixedKey, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value,long time) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForHash().put(prefixedKey, item, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        String prefixedKey = getKeyWithPrefix(key);
        redisTemplate.opsForHash().delete(prefixedKey,item);
    }
 
    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForHash().hasKey(prefixedKey, item);
    }
 
    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item,double by){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForHash().increment(prefixedKey, item, by);
    }
 
    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item,double by){
        String prefixedKey = getKeyWithPrefix(key);
        return redisTemplate.opsForHash().increment(prefixedKey, item,-by);
    }
 
    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForSet().members(prefixedKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,Object value){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForSet().isMember(prefixedKey, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForSet().add(prefixedKey, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
 
    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key,long time,Object...values) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            Long count = redisTemplate.opsForSet().add(prefixedKey, values);
            if(time>0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
 
    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForSet().size(prefixedKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
 
    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            Long count = redisTemplate.opsForSet().remove(prefixedKey, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================
 
    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForList().range(prefixedKey, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForList().size(prefixedKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
 
    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            String prefixedKey = getKeyWithPrefix(key);
            return redisTemplate.opsForList().index(prefixedKey, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForList().rightPush(prefixedKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForList().rightPush(prefixedKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForList().rightPushAll(prefixedKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForList().rightPushAll(prefixedKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index,Object value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            redisTemplate.opsForList().set(prefixedKey, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            String prefixedKey = getKeyWithPrefix(key);
            Long remove = redisTemplate.opsForList().remove(prefixedKey, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}