package com.xiaohucare.common.extra.tool.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.xiaohucare.common.utils.StringUtils;

/**
 * @author xiaohucare
 * @version 1.0.0
 *
 */
public class MoneyFormatUtils {
	/**
	 * 
	 * 数字转中文
	 * 
	 * @param list
	 * 
	 * @return
	 */
	private static String[] hanArr = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	private static String[] unitArrs = { "", "拾", "佰", "仟" };

	private static String[] unit = { "", "万", "亿", "兆" };

	private static boolean lin = false;

	public final static String DEFAULT_MAONEY_FORMATTER = "###,##0.00";

	public final static String DEFAULT_PERCENT_FORMATTER = "#.00";

	public static String format(String formatter, double money){
		DecimalFormat decimalFormat = new DecimalFormat(formatter);
		return decimalFormat.format(money);
	}
	
	public static String newformat(String formatter, double money){
		DecimalFormat newformat = (DecimalFormat)DecimalFormat.getInstance();
		newformat.setGroupingUsed(false);
		newformat.setMaximumFractionDigits(2);
		newformat.setMinimumFractionDigits(2);
		return String.format(formatter, newformat.format(money));
	}
	/**
	 * 将 money int 换算成 元
	 * @param money
	 * @return
	 */
	public static double formatIntToDouble(int money){
		double tempPrice=((double)money)/100;
		return tempPrice;
	}
	public static double formatBigDecimalToDouble(BigDecimal money){
		return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static BigDecimal formatBigDecimal(BigDecimal money){
		return money.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal formatBigDecimal4(BigDecimal money){
		return money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 换成分
	 * @param money
	 * @return
	 */
	public static int formatBigDecimalToInt(BigDecimal money){
		Double tempMoney= money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		tempMoney=tempMoney*100;
		return tempMoney.intValue();
	}
	/**
	 * 转化成字符 元角分
	 * @param money
	 * @return
	 */
	public static String formatBigDecimalToDoubleStr(BigDecimal money){
		DecimalFormat decimalFormat = new DecimalFormat(MoneyFormatUtils.DEFAULT_PERCENT_FORMATTER);
		String s = decimalFormat.format(money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return s;
	}
	public static double formatToDouble(Double money){
		BigDecimal result=new BigDecimal(money);
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static String formatToDoubleStr(Double money){
		BigDecimal result=new BigDecimal(money);
		return formatBigDecimalToDoubleStr(result);
	}
	
	public static double getFee(double money,double rate){
		double fee=Math.round(money*rate*100)*0.01d;
		return fee;
	}
	public static double getFee(int money,double rate){
		double fee=Math.round(money*rate*100)*0.01d;
		return fee;
	}
	/**
	 * 
	 * @param money 分哟
	 * @param rate 百分比
	 * @return
	 */
	public static int getMoney(int money,double rate){
		Double tempMoney=Math.round(money*rate*100)*0.01d;
		return tempMoney.intValue();
	}
	public static int getMoney(long money,double rate){
		Double tempMoney=Math.round(money*rate*100)*0.01d;
		return tempMoney.intValue();
	}
	public static Double getAbs(double money){
		return Math.abs(money);
	}
	public static double getFee(BigDecimal money,double rate){
        BigDecimal rateBig = new BigDecimal(Double.valueOf(rate));
		BigDecimal fee=money.multiply(rateBig);
		return fee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 分转元 
	 * @param money 分
	 * @return
	 */
	public static BigDecimal getBigDecimalFee(int money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		return rateBig.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 分转元 
	 * @param money 分
	 * @return
	 */
	public static BigDecimal getBigDecimalFee(long money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		return rateBig.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * int Money 除以100
	 * @param money 分
	 * @return
	 */
	public static int getIntDivide100(int money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		BigDecimal divide = rateBig.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		return divide.intValue();
	}
	
	/**
	 * 除法
	 * @return
	 */
	/**
	 * 除法 a/b 默认精度 小数点后 4 位
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getDivide(BigDecimal a,BigDecimal b,int scale){
		BigDecimal result = a.divide(b,scale,BigDecimal.ROUND_HALF_UP);
		return result;
	}
	/**
	 * 除法 a/b 默认精度 小数点后 4 位
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getDivide(BigDecimal a,BigDecimal b){
		BigDecimal result = a.divide(b,4,BigDecimal.ROUND_HALF_UP);
		return result;
	}
	public static BigDecimal getDivide(BigDecimal a,int b){
		BigDecimal result = a.divide(new BigDecimal(b),4,BigDecimal.ROUND_HALF_UP);
		return result;
	}
	/**
	 * a*b 乘法 默认精度 4
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getMultiply(BigDecimal a,BigDecimal b,int scale){
		BigDecimal result = a.multiply(b);
		return result.setScale(scale,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * a*b 乘法 默认精度 4 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getMultiply(BigDecimal a,BigDecimal b){
		BigDecimal result = a.multiply(b);
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(BigDecimal a,int b){
		BigDecimal result = a.multiply(new BigDecimal(b));
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(BigDecimal a,double b){
		BigDecimal result = a.multiply(new BigDecimal(b));
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(double a,double b){
		BigDecimal result =new BigDecimal(a*b);
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(int a,int b){
		BigDecimal result = new BigDecimal(a*b);
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * double *100 换算成分 存储
	 * @param money
	 * @return
	 */
	public static int formatDoubleToInt(double money){
		// 乘以 100 转换为分，并进行四舍五入
        return (int) Math.round(money * 100);
//		 Double tempPrice=money*100;
//		return tempPrice.intValue();
	}
	 /**
     * 将 BigDecimal 金额（元）转换为分（整数）
     * @param yuan 金额（单位：元，最多保留两位小数）
     * @return 分（整数）
     * @throws IllegalArgumentException 如果金额的小数位数超过两位或为负数
     */
    public static int convertYuanToFen(BigDecimal yuan) {
        if (yuan == null) {
            throw new IllegalArgumentException("金额不能为 null");
        }
        if (yuan.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("金额不能为负数");
        }
        // 确保两位小数（自动补零或四舍五入）
        BigDecimal scaled = yuan.setScale(2, RoundingMode.HALF_UP);
        // 元转分：乘以 100 并转为整数
        return scaled.multiply(new BigDecimal("100")).intValueExact();
    }
	public static String percentformat(String formatter, double number){
		DecimalFormat nf = new DecimalFormat(formatter);
		return nf.format(number);
	}
	public static byte intToByte(int x) {
		return (byte) x;
	}
	public static int byteToInt(byte b) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}
	
	public static String getBinaryString24AND(int... is) {
		if (is == null || is.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i : is) {
			sb.append(StringUtils.reverse(getBinaryString24(i)));
		}
		return sb.toString();
	}
	public static int getIntBySubString(String s, int begin, int end) {
		if (s.length() < end) {
			return 0;
		}
		return Integer.valueOf(StringUtils.reverse(s.substring(begin, end)), 2);
	}
	public static int getIntByBinaryString(String s) {
		return Integer.valueOf(s, 2);
	}
	public static String getBinaryString(Integer num){
		String bs = Integer.toBinaryString(num);
		return bs;
	}
	public static String getBinaryString24(Integer num){
			String bs = Integer.toBinaryString(num);
			int length = bs.length();
			if(length < 24){
				StringBuilder sb = new StringBuilder();
				int dis = 24 - length;
				for(int i = 0 ; i < dis; i ++){
					sb.append("0");
				}
				sb.append(bs);
			return sb.toString();
		}
	 return bs.substring(0, 24);
	}	
	
	public static double formatLongToDouble(long money){
		double tempPrice=((double)money)/100;
		return tempPrice;
	}
	/**
	 * 处理4为基数转换（个，十，百，千）
	 * 
	 * @param number
	 * @param top
	 * @return
	 */
	private static String getStr(String number, boolean top) {
		String result = "";
		lin = false;
		// number = Integer.valueOf(number) + "";
		int numLen = number.length();
		for (int i = 0; i < numLen; i++) {
			int num = number.charAt(i) - 48;
			if (i != 0 && num == 0 && num == (number.charAt(i - 1) - 48)) {
				if (i == numLen - 1) {
					lin = true;
					if (!"".equals(result)) {
						result = result.substring(0, result.length() - 1);
					}
				}
				continue;
			} else if (i == 0 && num == 0 && top) {
				continue;
			}
			if (num != 0 || i != numLen - 1) {
				result += hanArr[num];
			}
			if (num != 0) {
				result += unitArrs[numLen - 1 - i];
			}

		}
		if (Long.valueOf(number) >= 10 && Long.valueOf(number) < 20) {
			result = result.substring(1);
		}
		return result;
	}

	/**
	 * 循环取4字基数并加单位（万，亿，兆）
	 * 
	 * @param number
	 * @return
	 */
	public static String toHanStr(BigDecimal money) {
		String result = "";
		String numStr = money.longValue() + "";
		int numLen = numStr.length();
		int num = numLen / 4;
		int n = numLen % 4;
		if (n != 0) {
			result = getStr(numStr.substring(0, n), lin) + unit[num];
			if (lin) {
				result += hanArr[0];
			}
			numStr = numStr.substring(n);
		}
		for (int i = 0; i < num && Long.valueOf(numStr) != 0; i++) {
			result += getStr(numStr.substring(i * 4, (i + 1) * 4), lin);
			if (Long.valueOf(numStr.substring(i * 4, (i + 1) * 4)) != 0) {
				result += unit[num - i - 1];
				if (lin) {
					result += hanArr[0];
				}
			}
		}
		if (hanArr[0].equals(result.substring(result.length() - 1))) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}
