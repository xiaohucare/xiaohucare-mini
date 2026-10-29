package com.xiaohucare.common.extra.tool.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.xiaohucare.common.extra.utils.DateUtil;

/**
 * 
 * 随机数工具类
 *
 */
public class RandomUtil {
	
	public static List<Integer> randomSerial(int limit) {
		List<Integer> list = new ArrayList<Integer>(limit);
	    for (int ix = 0; ix < limit; ++ix){
	        list.add(ix);
	    }
	    Collections.shuffle(list, new Random());
		return list;
	}
	/**
	 * 生成 [min, max] 范围内的随机整数
	 * @param min 最小值（包含）
	 * @param max 最大值（包含）
	 * @return 范围内的随机整数
	 * @throws IllegalArgumentException 如果 min > max
	 */
	public static int randomSerial(int min, int max) {
	    if (min > max) {
	        throw new IllegalArgumentException("min must be less than or equal to max");
	    }
	    return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	public static final String getRandomByLength(int length) {
		if (0 == length) {
			return "";
		}
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		int realLength = uuid.length();
		if (realLength >= length) {
			return uuid.substring(0, length);
		}
		String tempString = "";
		String randomString = uuid;
		while (realLength < length) {
			tempString = "" + Math.random();
			randomString += tempString.substring(2);
			realLength = randomString.length();
		}
		return randomString.substring(0, length);
	}
	public static final String randomPreDay(int length) {
		if (0 == length) {
			return "";
		}
		String dayStr=DateUtil.format(new Date(), "yyMMdd");
		String tempString = "";
		String randomString = "";
		int realLength = 0;
		while (realLength < length) {
			tempString = "" + Math.random();
			randomString += tempString.substring(2);
			realLength = randomString.length();
		}
		return dayStr+randomString.substring(0, length);
	}
	
	public static long getRandom(){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong());
		return result;
	}
	public static long getRandom(long max){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong())%(max+1);
		return result;
	}
	public static long getRandom(long start,long end){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong())%(end-start+1)+start;
		return result;
	}
	public static double getRandom(double start,double end){
		double result = 0d;
		Random random=new Random();
		result=Math.abs(random.nextDouble())*(end-start)+start;
		return result;
	}
	public static String getRandomPassword(int passLength) {
		Random rd = new Random(); // 创建随机对象
		String n = ""; // 保存随机数
		int rdGet; // 取得随机数
		do {
			if (rd.nextInt() % 2 == 1) {
				rdGet = Math.abs(rd.nextInt()) % 10 + 48; // 产生48到57的随机数(0-9的键位值)
			} else {
				rdGet = Math.abs(rd.nextInt()) % 26 + 97; // 产生97到122的随机数(a-z的键位值)
			}
			char num1 = (char) rdGet; // int转换char
			String dd = Character.toString(num1);
			n += dd;
		} while (n.length() < passLength);// 设定长度，此处假定长度小于8
		return n;
	}

}
