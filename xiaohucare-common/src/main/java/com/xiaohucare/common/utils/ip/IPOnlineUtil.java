package com.xiaohucare.common.utils.ip;

import com.xiaohucare.common.extra.utils.HttpUtils;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class IPOnlineUtil {
	// IP地址查询
	public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
	public static final String IP_API_URL = "http://ip-api.com/json/";
	// 未知地址
	public static final String UNKNOWN = "XX XX";

	public static IpCityVo getNetIPAPIByIP(String ip) {
		IpCityVo ipCityVo=new IpCityVo();
		if(!IpUtils.isValidIp(ip)) {
			ipCityVo.setTips("格式化不对");
			return ipCityVo;
		}
		// 内网不查询
		if (IpUtils.internalIp(ip)) {
			ipCityVo.setTips("内网IP");
			return ipCityVo;
		}
		try {
			JSONObject obj = HttpUtils.httpGet(IP_API_URL+ip+"?lang=zh-CN");
			String country = obj.getStr("country");
			String regionName = obj.getStr("regionName");
			String city = obj.getStr("city");
			String addr = obj.getStr("regionName");
			String isp = obj.getStr("isp");
			String tips=String.format("%s%s%s%s", country, city, addr,isp);
			ipCityVo.setCountry(country);
			ipCityVo.setProv(regionName);
			ipCityVo.setCity(city);
			ipCityVo.setAddr(addr);
			ipCityVo.setRegion(addr);
			ipCityVo.setIsp(isp);
			ipCityVo.setTips(tips);
			ipCityVo.setStatus(0);
			return ipCityVo;
			
		} catch (Exception e) {
			log.error("获取地理位置异常 {}", ip);
		}
		ipCityVo.setTips("查询异常-未知");
		return ipCityVo;
	}
	public static IpCityVo getNetRealAddressByIP(String ip) {
		IpCityVo ipCityVo=new IpCityVo();
		if(!IpUtils.isValidIp(ip)) {
			ipCityVo.setTips("格式化不对");
			return ipCityVo;
		}
		// 内网不查询
		if (IpUtils.internalIp(ip)) {
			ipCityVo.setTips("内网IP");
			return ipCityVo;
		}
		try {
			JSONObject obj = HttpUtils.httpGet(IP_URL+"?ip=" + ip + "&json=true");
			String pro = obj.getStr("pro");
			String city = obj.getStr("city");
			String region = obj.getStr("region");
			String addr = obj.getStr("addr");
			ipCityVo.setCity(city);
			ipCityVo.setProv(pro);
			ipCityVo.setAddr(addr);
			ipCityVo.setRegion(region);;
			ipCityVo.setTips(addr);
			ipCityVo.setStatus(0);
			return ipCityVo;
		} catch (Exception e) {
			log.error("获取地理位置异常 {}", ip);
		}
		ipCityVo.setTips("查询异常-未知");
		return ipCityVo;
	}
}
