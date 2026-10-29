package com.xiaohucare.http.client;

import java.util.Map;

import com.xiaohucare.common.extra.utils.HttpUtils;
import com.xiaohucare.http.model.Coordinate;
import com.xiaohucare.http.model.Location;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
/**
 * 高德地图服务客户端接口
 */
public interface Amap {
	public static String HOST_URL="http://ditu.amap.com";
    /**
     * 根据经纬度获取详细地址
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
	public static  Location getLocation(String longitude, String latitude){
    	 String url = String.format(HOST_URL+"/service/regeo?longitude=%s&latitude=%s",longitude,latitude);
    	 Location data=JSONUtil.toBean(HttpUtils.httpGet(url),  Location.class);
    	 return data;
    }
    /**
     * 根据经纬度获取详细地址
     * @param coordinate 经纬度对象
     * @return
     */
    public static  Map getLocation(Coordinate coordinate){
    	String url =HOST_URL+"/service/regeo";
    	 Map data=JSONUtil.toBean(HttpUtils.httpGet(url,BeanUtil.beanToMap(coordinate)),  Map.class);
	   	 return data;
      }

}
