package com.xiaohucare.third.weather;

import java.util.HashMap;
import java.util.Map;

import com.xiaohucare.common.extra.utils.HttpUtils;
import com.xiaohucare.common.extra.utils.JsonUtil;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class YiketianqiBaseApi {
	private static String   Host="http://v1.yiketianqi.com/";
	private static String   free_month =Host+"free/month";
	private static String   appid ="xxxx";
	private static String   appsecret ="xxxxx";
	private static String   cityCode ="370800000000";//金乡县
	private static String   cityCode_dz ="371400000000";//德州市
	public static WeatherYkVo getFreeMonth(String appid,String  appsecret,String cityCode) throws Exception {	
		Map<String, Object> querys=new HashMap<String, Object>();
		querys.put("appid", appid);
		querys.put("appsecret", appsecret);
		querys.put("adcode", cityCode);
		querys.put("unescape", "1");
		Object reslut=HttpUtils.httpGet(free_month,querys);
		WeatherYkVo resultMapData=JsonUtil.getObjet(reslut.toString(), WeatherYkVo.class);
		return resultMapData;
	}
	public static WeatherYkVo getFreeWeek(String appid,String  appsecret,String cityCode) throws Exception {	
		Map<String, Object> querys=new HashMap<String, Object>();
		querys.put("appid", appid);
		querys.put("appsecret", appsecret);
		querys.put("adcode", cityCode);
		querys.put("unescape", "1");
		Object reslut=HttpUtils.httpGet(Host+"free/week",querys);
		WeatherYkVo resultMapData=JsonUtil.getObjet(reslut.toString(), WeatherYkVo.class);
		return resultMapData;
	}
    
}
