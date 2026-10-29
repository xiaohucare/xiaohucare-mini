package com.xiaohucare.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import org.springframework.util.AntPathMatcher;

import com.xiaohucare.common.constant.Constants;
import com.xiaohucare.common.core.text.StrFormatter;

/**
 * 字符串工具类
 * 
 * @author xiaohucare
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线 */
    private static final char SEPARATOR = '_';

    /** 星号 */
    private static final char ASTERISK = '*';

    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 替换指定字符串的指定区间内字符为"*"
     *
     * @param str 字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude 结束位置（不包含）
     * @return 替换后的字符串
     */
    public static String hide(CharSequence str, int startInclude, int endExclude)
    {
        if (isEmpty(str))
        {
            return NULLSTR;
        }
        final int strLength = str.length();
        if (startInclude > strLength)
        {
            return NULLSTR;
        }
        if (endExclude > strLength)
        {
            endExclude = strLength;
        }
        if (startInclude > endExclude)
        {
            // 如果起始位置大于结束位置，不替换
            return NULLSTR;
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++)
        {
            if (i >= startInclude && i < endExclude)
            {
                chars[i] = ASTERISK;
            }
            else
            {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 在字符串中查找第一个出现的 `open` 和最后一个出现的 `close` 之间的子字符串
     * 
     * @param str 要截取的字符串
     * @param open 起始字符串
     * @param close 结束字符串
     * @return 截取结果
     */
    public static String substringBetweenLast(final String str, final String open, final String close)
    {
        if (isEmpty(str) || isEmpty(open) || isEmpty(close))
        {
            return NULLSTR;
        }
        final int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND)
        {
            final int end = str.lastIndexOf(close);
            if (end != INDEX_NOT_FOUND)
            {
                return str.substring(start + open.length(), end);
            }
        }
        return NULLSTR;
    }

    /**
     * 判断是否为空，并且不是空白字符
     * 
     * @param str 要判断的value
     * @return 结果
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 是否为http(s)://开头
     * 
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link)
    {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * 字符串转set
     * 
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static final Set<String> str2Set(String str, String sep)
    {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转list
     * 
     * @param str 字符串
     * @param sep 分隔符
     * @return list集合
     */
    public static final List<String> str2List(String str, String sep)
    {
        return str2List(str, sep, true, false);
    }

    /**
     * 字符串转list
     * 
     * @param str 字符串
     * @param sep 分隔符
     * @param filterBlank 过滤纯空白
     * @param trim 去掉首尾空白
     * @return list集合
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim)
    {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str))
        {
            return list;
        }

        // 过滤空白字符串
        if (filterBlank && StringUtils.isBlank(str))
        {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split)
        {
            if (filterBlank && StringUtils.isBlank(string))
            {
                continue;
            }
            if (trim)
            {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array 给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (isEmpty(collection) || isEmpty(array))
        {
            return false;
        }
        else
        {
            for (String str : array)
            {
                if (collection.contains(str))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串同时串忽略大小写
     *
     * @param cs 指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences)
    {
        if (isEmpty(cs) || isEmpty(searchCharSequences))
        {
            return false;
        }
        for (CharSequence testStr : searchCharSequences)
        {
            if (containsIgnoreCase(cs, testStr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     * 
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     * 
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty())
        {
            // 没必要转换
            return "";
        }
        else if (!name.contains("_"))
        {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty())
            {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法
     * 例如：user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     * 
     * @param str 指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置: 
     * ? 表示单个字符; 
     * * 表示一层路径内的任意字符串，不可跨层级; 
     * ** 表示任意层路径;
     * 
     * @param pattern 匹配规则
     * @param url 需要匹配的url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     * 
     * @param num 数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     * 
     * @param s 原始字符串
     * @param size 字符串指定长度
     * @param c 用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
    public static boolean isNotNull2(Object obj) {
    	return !isNullOrEmpty(obj);
    }
    /**
	 * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串
	 * 
	 * @param listString
	 * @param split      连接的字符串，正则表达式
	 * @return
	 */
	public static List<String> splitToList(final String listString, final String split) {
		List<String> result = new ArrayList<String>();
		if (listString == null || listString == "") {
			return result;
		}
		if (split == null || split.length() == 0) {
			return result;
		}
		String tmpListString = listString.trim();

		String[] array = tmpListString.split(split);
		for (String str : array) {
			str = str.trim();
			if (!"".equals(str)) {
				result.add(str);
			}
		}
		return result;
	}
	public static String splitArrayToStr(final String[] strList, final String split) {
		String tmpListStr = "";
		for (String str : strList) {
			if (strList[0].equals(str)) {
				tmpListStr = str;
			} else {
				tmpListStr = tmpListStr + split + str;
			}
		}
		return tmpListStr;
	}

	public static List<Long> splitToLongList(final String listString, final String split) {
		List<Long> result = new ArrayList<Long>();
		if (listString == null || listString == "") {
			return result;
		}
		if (split == null || split.length() == 0) {
			return result;
		}
		String tmpListString = listString.trim();

		String[] array = tmpListString.split(split);
		for (String str : array) {
			str = str.trim();
			if (!"".equals(str)) {
				result.add(Long.valueOf(str));
			}
		}
		return result;
	}

	public static String listToString(List<String> listStrs, final String split) {
		if (listStrs == null || listStrs.size() == 0) {
			return "";
		}
		if (split == null || split.length() == 0) {
			return "";
		}
		String tmpListString = "";
		for (String str : listStrs) {
			str = str.trim();
			if (!"".equals(str)) {
				tmpListString = tmpListString + split + str;
			}
		}
		if (tmpListString.length() > 1) {
			tmpListString = tmpListString.substring(1);
		}
		return tmpListString;
	}

	public static String listIntToString(List<Integer> listStrs, final String split) {
		if (listStrs == null || listStrs.size() == 0) {
			return "";
		}
		if (split == null || split.length() == 0) {
			return "";
		}
		String tmpListString = "";
		for (Integer str : listStrs) {
			tmpListString = tmpListString + split + str;
		}
		if (tmpListString.length() > 1) {
			tmpListString = tmpListString.substring(1);
		}
		return tmpListString;
	}

	
	/**
	 * 将JSON字符串或对象统一转化为 Map<String, Object>
	 * <p>
	 * 支持以下输入类型：
	 * <ul>
	 *   <li>JSON 字符串（如 {"key":"value"}）</li>
	 *   <li>已经是 Map 类型的对象，直接强转返回</li>
	 *   <li>任意 Java 对象，通过 fastjson2 序列化后再解析为 Map</li>
	 * </ul>
	 *
	 * @param jsonStr JSON 字符串
	 * @return Map<String, Object>，解析失败时返回空 Map
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (isEmpty(jsonStr)) {
			return map;
		}
		try {
			JSONObject jsonObject = JSON.parseObject(jsonStr.trim());
			if (jsonObject != null) {
				map.putAll(jsonObject);
			}
		} catch (Exception e) {
			// 解析失败时返回空 Map
		}
		return map;
	}

	/**
	 * 将任意对象统一转化为 Map<String, Object>
	 * <p>
	 * 支持以下输入类型：
	 * <ul>
	 *   <li>null，返回空 Map</li>
	 *   <li>JSON 字符串（如 {"key":"value"}），直接解析</li>
	 *   <li>已经是 Map 类型的对象，直接强转返回</li>
	 *   <li>任意 Java 对象，通过 fastjson2 序列化后再解析为 Map</li>
	 * </ul>
	 *
	 * @param obj 任意对象
	 * @return Map<String, Object>，转换失败时返回空 Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseObj2Map(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj == null) {
			return map;
		}
		// 已经是 Map，直接返回
		if (obj instanceof Map) {
			return (Map<String, Object>) obj;
		}
		// 字符串类型，尝试 JSON 解析
		if (obj instanceof String) {
			return parseJSON2Map((String) obj);
		}
		// 其他对象：先序列化为 JSON 字符串再解析
		try {
			String jsonStr = JSON.toJSONString(obj);
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			if (jsonObject != null) {
				map.putAll(jsonObject);
			}
		} catch (Exception e) {
			// 转换失败时返回空 Map
		}
		return map;
	}
	public static boolean isNullOrEmptyStr(String obj) {
		if (obj == null)
			return true;
		if (obj.length() == 0)
			return true;
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;
		return false;
	}
	
	public static String fomartStringNoNumber(String orgStr) {
		// 去除字符串中的数字
		return orgStr.replaceAll("\\d+", "");
	}

	public static String fomartStringNoOther(String orgStr) {
		if (StringUtils.isEmpty(orgStr)) {
			return "";
		}
		if (orgStr.trim().isEmpty()) {
			return orgStr;
		}
		// 去除字符串中的数字
		return orgStr.replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "");
	}
	public static String hideIdCard(String certiNumber) {
		if (certiNumber == null || certiNumber.length() < 5)
			return "";
		certiNumber = certiNumber.substring(0, 3) + "****"
				+ certiNumber.substring(certiNumber.length() - 4, certiNumber.length());
		return certiNumber;
	}

	public static String hideName(String name) {
		if (name == null) {
			return "";
		}
		if (name.length() > 2) {
			String abc = name.substring(1, name.length() - 1);
			name = name.replace(abc, "****");
			return name;
		} else if (name.length() == 2) {
			String a = name.substring(0, 1);
			String b = name.substring(1, 2);
			name = a + "****" + b;
			return name;
		} else {
			name = name + "****";
			return name;
		}
	}
}