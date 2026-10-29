package com.xiaohucare.common.extra.utils;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;

import cn.hutool.json.JSONUtil;

public class JsonUtil {

	public static String getJsonByObj(JSONObject reslut) {
		return JSONUtil.toJsonStr(reslut);
	}
	public static String getJsonByObj(Object reslut) {
		return JSONUtil.toJsonStr(reslut);
	}
	public static <T> T getObjet(String str, Class<T> class1) {
		return JSONUtil.toBean(str, class1);
	}
	  /**
     * 将JSON数据转换为ListBean集合
     *
     * @param <T>
     * @param json  JSON数组数据
     * @param calzz 待转换的Bean类型 --LinkedCaseInsensitiveMap
     * @return
     */
    public static <T> List<T> getListBean(String json, Class<T> calzz) {
        return JSONUtil.toList(json, calzz);
    }
    /**
     * 将JSON数据转换为List集合
     *
     * @param <T>
     * @param json  JSON数组数据
     * @param calzz 待转换的Bean类型 --LinkedCaseInsensitiveMap
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getList(String json, Class calzz) {
        return getListBean(json, calzz);
    }
    public static String getJsonDefaultByObj(Object bean) {
        return JSONUtil.toJsonStr(bean);
    }

    public static String getGsonByObj(Object bean) {
        return GsonUtils.toJsonString(bean);
    }
}
