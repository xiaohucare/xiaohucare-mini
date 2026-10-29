package com.xiaohucare.common.extra.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.xiaohucare.common.utils.StringUtils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {
	public static JSONObject httpGet(String url, Map<String, Object> querys) {
		String str = HttpUtil.get(url, querys);
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------httpGet-------" + str);
		return null;
	}

	public static JSONObject httpGet(String url) {
		String str = httpGetStr(url);
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------httpGet-------" + str);
		return null;
	}

	public static String httpGetStr(String url) {
		return HttpUtil.get(url);
	}

	public static JSONObject httpGetJson(String url, Map<String, String> headMap, JSONObject jsonParam) {
		HttpRequest httpRequest = HttpUtil.createGet(url).addHeaders(headMap);
		httpRequest.body(JSONUtil.toJsonStr(jsonParam));
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------httpGetJson-------" + str);
		return null;
	}

	public static JSONObject httpGetJson(String url, Map<String, String> headMap, Map<String, String> jsonParam) {
		HttpRequest httpRequest = HttpUtil.createGet(url).addHeaders(headMap);
		httpRequest.body(JSONUtil.toJsonStr(jsonParam));
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------httpGetJson-------" + str);
		return null;
	}

	public static JSONObject doPostJson(String url, JSONObject jsonParam) {
		HttpRequest httpRequest = HttpUtil.createPost(url);
		httpRequest.body(JSONUtil.toJsonStr(jsonParam));
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------doPostJson-------" + str);
		return null;
	}

	public static JSONObject doPostJson(String url, Map<String, String> headMap, JSONObject jsonParam) {
		HttpRequest httpRequest = HttpUtil.createPost(url).addHeaders(headMap);
		httpRequest.body(JSONUtil.toJsonStr(jsonParam));
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------doPostJson-------" + str);
		return null;
	}

	public static JSONObject doPostJson(String url, Map<String, String> headMap, String jsonParam) {
		HttpRequest httpRequest = HttpUtil.createPost(url).addHeaders(headMap);
		httpRequest.body(jsonParam);
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------doPostJson-------" + str);
		return null;
	}

	public static JSONObject doPostJson(String url, Map<String, String> headMap, Map<String, String> querys,
			String jsonParam) {
		HttpRequest httpRequest = HttpUtil.createPost(url).addHeaders(headMap);
		httpRequest.body(jsonParam);
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------doPostJson-------" + str);
		return null;
	}

	public static JSONObject doPostForm(String url, Map<String, String> headMap, Map<String, String> jsonParam) {
		HttpRequest httpRequest = HttpUtil.createPost(url).addHeaders(headMap);
		httpRequest.formStr(jsonParam);
		String str = httpRequest.execute().body();
		if (JSONUtil.isTypeJSON(str)) {
			log.info("------httpGet-------" + str);
			return JSONUtil.parseObj(str);
		}
		log.error("------doPostJson-------" + str);
		return null;
	}

	private static String buildUrl(String hostpath, Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(hostpath);
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						String encode = URLUtil.encode(query.getValue());
						sbQuery.append(encode);
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}
		return sbUrl.toString();
	}
}
