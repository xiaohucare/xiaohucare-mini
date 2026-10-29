package com.xiaohucare.framework.config;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.xiaohucare.common.constant.Constants;

/**
 * Redis使用FastJson序列化
 * 
 * @author xiaohucare
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
    private static final Logger log = LoggerFactory.getLogger(FastJson2JsonRedisSerializer.class);
    
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        try {
            return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            log.error("FastJSON序列化异常: {}", e.getMessage(), e);
            throw new SerializationException("FastJSON序列化异常", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        
        try {
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
        } catch (Exception e) {
            log.error("FastJSON反序列化异常: {}", e.getMessage(), e);
            throw new SerializationException("FastJSON反序列化异常", e);
        }
    }
}