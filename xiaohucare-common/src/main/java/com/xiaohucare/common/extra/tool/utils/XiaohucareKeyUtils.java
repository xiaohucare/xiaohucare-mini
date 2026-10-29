package com.xiaohucare.common.extra.tool.utils;

public class XiaohucareKeyUtils {
    /**
     * 生成key
     *
     * @param prefix     前缀
     * @param className  类名
     * @param methodName 方法名
     * @return prefix className.methodName
     */
    public static String genKey(String prefix,String userId, String className, String methodName) {
        return prefix + "userId_" +
        		userId +
                "_" +
                className +
                "." +
                methodName;
    }
    public static final String toGenQDBCode(int enTakeOrderId,String name) {
 		 String defaultStr="QDB000";
 		 String tempOrg=RC4Util.zeroPadString(""+enTakeOrderId,6,defaultStr);
 		 String str = RC4Util.encry_RC4_string(tempOrg, tempOrg+name+"QDB");  
 		 return RC4Util.appMdLine(str.toUpperCase(),4);
 	}
    public static final String toGenQDBCode(long enTakeOrderId,String name) {
    	String defaultStr="QDB000";
    	String tempOrg=RC4Util.zeroPadString(""+enTakeOrderId,6,defaultStr);
    	String str = RC4Util.encry_RC4_string(tempOrg, tempOrg+name+"QDB");  
    	return RC4Util.appMdLine(str.toUpperCase(),4);
    }
}
