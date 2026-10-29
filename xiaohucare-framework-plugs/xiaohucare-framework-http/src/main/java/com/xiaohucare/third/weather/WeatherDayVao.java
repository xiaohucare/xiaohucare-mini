package com.xiaohucare.third.weather;

import lombok.Data;

@Data
public class WeatherDayVao {
	String date;
	String wea;
	String wea_img;
	String wea_day;
	String wea_day_img;
	String wea_night;
	String wea_night_img;
	String tem_day;//白天温度(高温)
	String tem_night;//白天温度(低温)
	String win;
	String win_speed;//风力等级
	String humidity;//湿度
	String tem1;//高温
	String tem2;//低温
	
	String rain_pcpn;//降雨量数值
	String visibility;//能见度
	String pressure;//气压hPa
	String air;//空气质量
	String air_level;//空气质量等级
	String rain;//降雨概率
}
