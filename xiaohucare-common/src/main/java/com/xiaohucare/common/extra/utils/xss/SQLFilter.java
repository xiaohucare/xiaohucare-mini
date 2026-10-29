package com.xiaohucare.common.extra.utils.xss;

import com.xiaohucare.common.exception.UtilException;
import com.xiaohucare.common.utils.StringUtils;

/**
 * SQL过滤
 * @author xiaohucare
 * @date 2017-04-01 16:16
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     * @throws Exception 
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.indexOf(keyword) != -1) {
            	if(str.contains("update")&&(str.contains("update_time")||str.contains("update_date")||str.contains("updatetime")||str.contains("updatedate"))) {
            		return str;
            	}
            	throw new UtilException("参数存在SQL注入风险");
            }
        }
        return str;
    }
}
