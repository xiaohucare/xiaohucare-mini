package com.xiaohucare.common.extra.tool.utils;

public class RC4Util {
	public static final String toGenDLYCode(int enTakeOrderId,String orderNo) {
		 String defaultStr="DLY000";
		 String tempOrg=zeroPadString(""+enTakeOrderId,6,defaultStr);
		 String str = encry_RC4_string(tempOrg, tempOrg+orderNo+"DLY");  
		 return appMdLine(str.toUpperCase(),4);
	}
	public static final String toGenShareCode(String time,String genCode) { 
		 String defaultStr="PM";
		 String tempOrg=zeroPadString(""+time,6,defaultStr);
		 String str = encry_RC4_string(tempOrg, tempOrg+genCode);  
		 return appMdLine(str.toUpperCase(),4);
	}
	public static final String toGenLotteryCode(String enTakeOrderId,String orderNo) {
		String defaultStr="LTT000";
		String tempOrg=zeroPadString(""+enTakeOrderId,6,defaultStr);
		String str = encry_RC4_string(tempOrg, tempOrg+orderNo+"LTT");  
		return appMdLine(str.toUpperCase(),4);
	}
	public static final String toGenCineCode(String orderNo,String index) {
		String defaultStr="CT0000";
		String tempOrg=zeroPadString(""+orderNo,6,defaultStr);
		System.out.println(tempOrg);
		String str = encry_RC4_string(orderNo, tempOrg+index+"CT");  
		return appMdLine(str.toUpperCase(),4);
	}
	
	public static final String zeroPadString(String string, int length,String defaultStr)
	  {
	    if ((string == null) || (string.length() > length)) {
	      return string;
	    }
	    char[] zeroArray = defaultStr.toCharArray();
	    StringBuffer buf = new StringBuffer(length);
	    buf.append(zeroArray, 0, length - string.length()).append(string);
	    return buf.toString();
	 }
	public static String appMdLineAll(String orgStr ,int minLength) {
		orgStr=orgStr.replace("-", "");
		return appMdLine(orgStr.toUpperCase(),minLength);
	}
	
	public static String appMdLine(String orgStr ,int minLength) {
		StringBuffer tempStr = new StringBuffer();
		if(minLength<1||minLength>=orgStr.length()) {
			return orgStr;
		}
	    //循环遍历数组
	    for (int i = 0; i < orgStr.length(); i++) {
	    	if(i%minLength==(minLength-1)&&i<orgStr.length()-1) {
	    		tempStr.append(orgStr.charAt(i) + "-");
	    	}else {
	    		tempStr.append(orgStr.charAt(i));
	    	}
	    }
	    return tempStr.toString();
	}
	
    public static String decry_RC4(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    public static String decry_RC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static byte[] encry_RC4_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes();
        return RC4Base(b_data, key);
    }

    public static String encry_RC4_string(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encry_RC4_byte(data, key)));
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}