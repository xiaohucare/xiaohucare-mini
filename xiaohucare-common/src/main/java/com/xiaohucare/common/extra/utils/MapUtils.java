package com.xiaohucare.common.extra.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.xiaohucare.common.utils.StringUtils;
/**
 * 获取map中值的工具类,自动进行类型转换
 *
 * @author DT_panda
 */
public class MapUtils {
	 public static final String EMPTY = "";
	    public static String getString(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null)
	            return null;
	        if (value == EMPTY)
	        	return null;
	        return value.toString();
	    }
	    public static Integer getInteger(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null||value=="")
	            return 0;
	        if (value instanceof Integer)
	            return (Integer) value;
	        if (value instanceof String) {
	        	if(StringUtils.isNotNull(value)) {
	        		return Integer.valueOf((String) value);
	        	}else {
	        		 return null;
	        	}
	        }
	        //Date 不支持变成为date类型
	        if (value instanceof Date)
	            throw new ClassCastException();
	        if (value instanceof Number)
	            return ((Number) value).intValue();
	        throw new ClassCastException();
	    }

	    public static Long getLong(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null||value=="")
	            return 0L;
	        if (value instanceof Long)
	            return (Long) value;
	        if (value instanceof Number)
	            return ((Number) value).longValue();
	        if (value instanceof String)
	            return Long.valueOf((String) value);
	        if (value instanceof Date) {
	            return (((Date) value).getTime());
	        }
	        if (value instanceof java.sql.Time) {
	            return ((java.sql.Time) value).getTime();
	        }
	        if (value instanceof Timestamp) {
	            return ((Timestamp) value).getTime();
	        }

	        throw new ClassCastException();
	    }

	    public static Double getDouble(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null)
	            return null;
	        if (value instanceof Double)
	            return (Double) value;
	        if (value instanceof Number)
	            return ((Number) value).doubleValue();
	        if (value instanceof String)
	            return Double.valueOf((String) value);
	        throw new ClassCastException();
	    }

	    public static BigDecimal getBigDecimal(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null)
	            return null;
	        if (value instanceof BigDecimal)
	            return (BigDecimal) value;
	        if (value instanceof Integer)
	            return new BigDecimal((Integer) value);
	        if (value instanceof Short)
	            return new BigDecimal((Short) value);
	        if (value instanceof Byte)
	            return new BigDecimal((Byte) value);
	        if (value instanceof Long)
	            return new BigDecimal((Long) value);
	        if (value instanceof Float)
	            return new BigDecimal((Float) value);
	        if (value instanceof Double)
	            return new BigDecimal((Double) value);
	        if (value instanceof Date) {
	            return new BigDecimal(((Date) value).getTime());
	        }
	        if (value instanceof java.sql.Time) {
	            return new BigDecimal(((java.sql.Time) value).getTime());
	        }
	        if (value instanceof Timestamp) {
	            return new BigDecimal(((Timestamp) value).getTime());
	        }
	        if (value instanceof String) {
	            if (!StringUtils.isNullOrEmpty((String) value))
	                return new BigDecimal((String) value);
	            else
	                return null;
	        }
	        throw new ClassCastException();
	    }


	    /**
	     * 将map中key为likeKey的value前后加上字符'%'，用于like查询
	     *
	     * @param map
	     * @param likeKey
	     */
	    public static void toLikeValue(Map<String, Object> map, String... likeKey) {
	        if (ArrayUtils.isEmpty(likeKey))
	            return;
	        for (String key : likeKey) {
	            if (map.containsKey(key))
	                map.put(key, "%" + map.get(key) + "%");
	        }
	    }

	    /**
	     * 获取日期
	     *
	     * @param key
	     * @param map
	     * @return
	     */
	    public static Date getDate(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null)
	            return null;
	        else {
	            if (value instanceof Date) {
	                return (Date) value;
	            } else if (value instanceof Timestamp) {
	                return new Date(((Timestamp) value).getTime());
	            }
	        }
	        return null;
	    }

	    /**
	     * 获取日期
	     *
	     * @param key
	     * @param map
	     * @return
	     */
	    public static java.util.Date getTimestamp(String key, Map<String, Object> map) {
	        if (map == null || key == null)
	            throw new IllegalArgumentException();
	        if (!map.containsKey(key))
	            return null;
	        Object value = map.get(key);
	        if (value == null)
	            return null;
	        else {
	            if (value instanceof Date) {
	                return (Date) value;
	            } else if (value instanceof Timestamp) {
	                Timestamp ts = (Timestamp) value;
	                return ts;
	            }
	        }
	        return null;
	    }

	    /**
	     * 如果value不为空 ，则放到map中
	     *
	     * @param map
	     * @param key
	     * @param value
	     */
	    public static void putIfValueNotNull(Map<String, Object> map, String key, Object value) {
	        if (value != null)
	            map.put(key, value);
	    }

	    /**
	     * 如果value不为空 ，则放到map中
	     *
	     * @param map
	     * @param key
	     * @param value
	     */
	    public static void putIfValueNotEmpty(Map<String, Object> map, String key, String value) {
	        if (!StringUtils.isNullOrEmpty(value))
	            map.put(key, value);
	    }

	    public static String convertMap2Xml(Map<Object, Object> paraMap) {
	        StringBuffer xmlStr = new StringBuffer();
	        if (paraMap != null) {
	            xmlStr.append("<xml>");
	            Set<Object> keySet = paraMap.keySet();
	            Iterator<Object> keyIte = keySet.iterator();
	            while (keyIte.hasNext()) {
	                String key = (String) keyIte.next();
	                String val = String.valueOf(paraMap.get(key));
	                xmlStr.append("<");
	                xmlStr.append(key);
	                xmlStr.append(">");
	                xmlStr.append(val);
	                xmlStr.append("</");
	                xmlStr.append(key);
	                xmlStr.append(">");
	            }
	            xmlStr.append("</xml>");
	        }
	        return xmlStr.toString();
	    }
	    public static HashMap<String, Object>  put(String key, Object value) {
	    	HashMap<String, Object> map=new HashMap<String, Object>(10);
	    	map.put(key, value);
	    	return map;
	    }
}

