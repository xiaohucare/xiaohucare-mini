package com.xiaohucare.third.weather;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WeatherYkVo {
	Integer nums;
	String cityid;
	String city;
	Date update_time;
	List<WeatherDayVao> data;
}
