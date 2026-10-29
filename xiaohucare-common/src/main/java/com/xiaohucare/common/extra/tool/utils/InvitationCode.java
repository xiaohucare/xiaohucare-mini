package com.xiaohucare.common.extra.tool.utils;
import java.util.Stack;

public class InvitationCode {
	static int digits=36;
	static int addVal=10000000;
	
    public static final char[] array={'0','1','2','3','4','5','6','7','8','9','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};

    public static String _10_to_36(long number){
            Long rest=addVal+number;
            Stack<Character> stack=new Stack<Character>();
            StringBuilder result=new StringBuilder(0);
            while(rest!=0){
                stack.add(array[new Long((rest-(rest/digits)*digits)).intValue()]);
                rest=rest/digits;
            }
            for(;!stack.isEmpty();){
                result.append(stack.pop());
            }
            return result.toString();

    }

    public static long _36_to_10(String sixty_str){
        int multiple=1;
        long result=0;
        Character c;
        for(int i=0;i<sixty_str.length();i++){
            c=sixty_str.charAt(sixty_str.length()-i-1);
            result+=_36_value(c)*multiple;
            multiple=multiple*digits;
        }
        return result-addVal;
    }

    private static int _36_value(Character c){
        for(int i=0;i<array.length;i++){
            if(c==array[i]){
                return i;
            }
        }
        return -1;
    }

}
