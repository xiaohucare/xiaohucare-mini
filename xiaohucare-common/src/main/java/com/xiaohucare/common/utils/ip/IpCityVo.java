package com.xiaohucare.common.utils.ip;
import lombok.Data;
@Data
public class IpCityVo {
	String continent;//亚洲",//大洲
	String owner;//中国联通",//所属机构
	String country;//中国",//国家
	String lng;//120.208335",//经度
	String adcode;//330100",//行政编码
	String prov;//浙江省",//省份
	String city;//杭州市",//城市
	String region;//区",//地区
	String addr;//点
	String timezone;//UTC+8",//时区
	String isp;//中国联通",//运营商
	String accuracy;//城市",//精度
	String source;//数据挖掘",//采集方式
	String asnumber;//4837",//自治域编码
	String areacode;//CN",//国家编码
	String zipcode;//310002",//邮编
	String radius;//129.2092",//定位半径
	String lat;//30.255611"//纬度
	String tips;//备注
	
	Integer status=-1;
}
