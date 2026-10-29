package com.xiaohucare.common.utils.ip;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import com.xiaohucare.common.utils.StringUtils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IPOfflineUtil {
	// 未知地址
	public static final String UNKNOWN = "XX XX";
	private static final byte[] dbPathBytes;
	static {
		try (InputStream is = IPOfflineUtil.class.getResourceAsStream("/ip2region/ip2region.xdb")) {
			dbPathBytes = IOUtils.readFully(is, is.available());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static String getRealAddressByIP(String ip) {
		return getRegion(ip).getTips();
	}
	public static IpCityVo getRegion(String ip) {
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
		Searcher searcher = null;
		try {
			searcher = Searcher.newWithBuffer(dbPathBytes);
			String region = searcher.search(ip);
			System.out.println(region);
			if (StringUtils.isNotBlank(region)) {
				ipCityVo.setStatus(0);
				String[] allArr = region.split("\\|");//.replace("|0", "").replace("0|", "").split("\\|");
				if(allArr.length>0) {
					if ("内网ip".equalsIgnoreCase(allArr[0])) {
						ipCityVo.setTips("内网IP");
						return ipCityVo;
					}else if ("中国".equals(allArr[0])) {
						ipCityVo.setCountry("中国");
						if(allArr.length>2) {
							ipCityVo.setProv(allArr[2]);
						}
						if(allArr.length>3) {
							ipCityVo.setCity(allArr[3]);
						}
						if(allArr.length>4) {
							ipCityVo.setIsp(allArr[4]);
						}
					}else {
						ipCityVo.setCountry(allArr[0]);
						if(allArr.length>2) {
							ipCityVo.setProv(allArr[2]);
						}
						if(allArr.length>3) {
							ipCityVo.setCity(allArr[3]);
						}
						if(allArr.length>4) {
							ipCityVo.setIsp(allArr[4]);
						}
					}
				}
			}
			ipCityVo.setTips(region);
			return ipCityVo;
		} catch (Exception e) {
			log.error("failed to search({}): {}\n", ip, e.getMessage(), e);
		} finally {
			try {
				searcher.close();
			} catch (IOException e) {
			}
		}
		ipCityVo.setTips("查询异常-未知");
		return ipCityVo;
	}
	public static String getRegionCity(String ip) {
		IpCityVo region = getRegion(ip);
		return region.getCity();
	}

	/**
	 * 解析城市信息，国内显示城市名，国外显示国家名
	 */
	private static String getCityInfo(String regionInfo) {
		if (StringUtils.isNotBlank(regionInfo)) {
			String[] cityArr = regionInfo.replace("|0", "").replace("0|", "").split("\\|");
			System.out.println(JSONUtil.toJsonPrettyStr(cityArr));
			if (cityArr.length > 0) {
				if ("内网ip".equalsIgnoreCase(cityArr[0])) {
					return "内网IP";
				}
				if ("中国".equals(cityArr[0])) {
					return cityArr[1];
				}
				return cityArr[0];
			}
		}
		return "未知IP";
	}
}
