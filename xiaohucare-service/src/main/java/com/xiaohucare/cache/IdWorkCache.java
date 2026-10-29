package com.xiaohucare.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.xiaohucare.common.extra.tool.utils.DateUtils;
import com.xiaohucare.common.extra.tool.utils.RandomUtil;


@Component
public class IdWorkCache{
	public static  String  ID_WORK_TYPE_QR_CARD_ORDER_NO="ID_WORK_TYPE_QR_CARD_ORDER_NO";//订单号生产
	public static  String  ID_WORK_TYPE_COMPANY_SN="ID_WORK_TYPE_COMPANY_SN";//订单号生产
	public static  String  ID_WORK_TYPE_HALL_SN="ID_WORK_TYPE_HALL_SN";//订单号生产
	public static  String  ID_WORK_TYPE_HALL_ORDER_SN="ID_WORK_TYPE_HALL_ORDER_SN";//订单号生产
	public static  String  ID_WORK_TYPE_ART_SN="ID_WORK_TYPE_ART_SN";//订单号生产
	public static  String  ID_WORK_TYPE_ART_WORK_SN="ID_WORK_TYPE_ART_WORK_SN";
	public static  String  ID_WORK_TYPE_ART_MUSEUM_SN="ID_WORK_TYPE_ART_MUSEUM_SN";
	public static  String  ID_WORK_TYPE_ART_BASE_SN="ID_WORK_TYPE_ART_BASE_SN";//订单号生产
	public static  String  ID_WORK_TYPE_HISTORY_SN="ID_WORK_TYPE_HISTORY_SN";//订单号生产
	public static  String  ID_WORK_TYPE_ART_BASE_BOOK_ORDER_SN="ID_WORK_TYPE_ART_BASE_BOOK_ORDER_SN";//订单号生产
	public static  String  ID_WORK_TYPE_ACTIVITY_ITEM_SN="ID_WORK_TYPE_ACTIVITY_ITEM_SN";//
	public static  String  ID_WORK_TYPE_SHOP_PRODUCT_SN="ID_WORK_TYPE_SHOP_PRODUCT_SN";//
	public static  String  ID_WORK_TYPE_IDENTIFY_SN="ID_WORK_TYPE_IDENTIFY_SN";//
	public static  String  ID_WORK_TYPE_ZB_SN="ID_WORK_TYPE_ZB_SN";//
	public static  String  ID_WORK_TYPE_TOWN_ITEM_SN="ID_WORK_TYPE_TOWN_ITEM_SN";//
	public static  String  ID_WORK_TYPE_TOWN_VILLAGET_SN="ID_WORK_TYPE_TOWN_VILLAGET_SN";//
	

	public static  String  ID_WORK_TYPE_SHOP_BID_SN="ID_WORK_TYPE_SHOP_BID_SN";
	public static  String  ID_WORK_TYPE_BIG_SN="ID_WORK_TYPE_BIG_SN";
	public static  String  ID_WORK_TYPE_BIG_PB_SN="ID_WORK_TYPE_BIG_PB_SN";
	public static  String  ID_WORK_TYPE_PAY_SN="ID_WORK_TYPE_PAY_SN";
	
	public static  String  ID_WORK_TYPE_PET_SN="ID_WORK_TYPE_PET_SN";
	public static  String  ID_WORK_TYPE_PZ_SN="ID_WORK_TYPE_PZ_SN";
	public static  String  ID_WORK_TYPE_TRY_STORE_SN="ID_WORK_TYPE_TRY_STORE_SN";
	public static  String  ID_WORK_TYPE_REPAIR_SN="ID_WORK_TYPE_REPAIR_SN";
	
	public static  String  ID_WORK_TYPE_TAX_ORDER_SN="ID_WORK_TYPE_TAX_ORDER_SN";
	
	public static  String  ID_WORK_TYPE_AI_CAR_ORDER_SN="ID_WORK_TYPE_AI_CAR_ORDER_SN";
	public static  String  ID_WORK_TYPE_LUYU_APPID_SN="ID_WORK_TYPE_LUYU_APPID_SN";
	
	public static  String  ID_WORK_TYPE_LOTTERY_SN="ID_WORK_TYPE_LOTTERY_SN";
	
	
	public Map<String, Integer> idWorkMap=new HashMap<String, Integer>();
	public Date currentDate=new Date();
	/**
	 * 获取顺序变量
	 * @param idType key 存储
	 * @return
	 */
	public  int getIdWorkByIdType(String idType) {
	   Integer idTemp=idWorkMap.get(idType);
	   if(idTemp==null){
		   idTemp=1000;
	   }else{
		   if(DateUtils.isTodayDate(currentDate)){
			   idTemp=idTemp+1;
		   }else{
			   //重新设置日期 重置为1
			   currentDate=new Date();
			   idTemp=1000;
		   } 
	   }
	   idWorkMap.put(idType, idTemp);
		return idTemp;
	}
	/**
	 * 
	 * @param prefixStr
	 * @param idType
	 * @param endStr
	 * @return
	 */
	public String getId(String prefixStr,String idType,String endStr){
		return prefixStr+getIdWorkByIdType(idType)+endStr;
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdDayEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+DateUtils.formatTodayDate()+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdDayHHMMEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+DateUtils.formatTodayTime()+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	
	
}
