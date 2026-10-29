package com.xiaohucare.common.extra.tool.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xiaohucare.common.utils.file.FileUtils;

public class WordFilter {

    private static Log log = LogFactory.getLog(WordFilter.class);
    private static Set<String> keywordsForNickname = new HashSet<String>();
    private static Set<String> keywordsForComment = new HashSet<String>();
    private static File commentFilter;
    private static File nicknameFilter;
    private static String path;
    static{
    	path = FileUtils.getConfigBasePath()+File.separator+"data"+File.separator+"filter"+File.separator;
    }
    public WordFilter() {}
    public WordFilter(String path) {
    	this.path = path;
    }
   public static void init() throws IOException {
        String filterForNicknamePath = path + "filterForNickname.txt";
        log.info("webapp path =====================>   :" + filterForNicknamePath);
        nicknameFilter = new File(filterForNicknamePath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(nicknameFilter), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        Set<String> forNickname = new HashSet<String>();
        String keyword = null;
        String key = null;
        while ((keyword = br.readLine()) != null) {
            if (!keyword.trim().isEmpty()) {
                if (keyword.trim().indexOf("|1") > -1) {
                    key = keyword.trim().substring(0, keyword.trim().indexOf("|1"));
                } else {
                    key = keyword.trim();
                }
                forNickname.add(key);
            }
        }
        keywordsForNickname = forNickname;
        
        String filterForCommentPath = path + "filterForComment.txt";
        log.info("webapp path =====================>   :" + filterForCommentPath);
        commentFilter = new File(filterForCommentPath);
        InputStreamReader cisr = new InputStreamReader(new FileInputStream(commentFilter), "UTF-8");
        BufferedReader cbr = new BufferedReader(cisr);
        Set<String> forComment = new HashSet<String>();
        keyword = null;
        key = null;
        while ((keyword = cbr.readLine()) != null) {
            if (!keyword.trim().isEmpty()) {
                if (keyword.trim().indexOf("|1") > -1) {
                    key = keyword.trim().substring(0, keyword.trim().indexOf("|1"));
                } else {
                    key = keyword.trim();
                }
                forComment.add(key);
            }
        }
        keywordsForComment = forComment;
    }
    public static boolean checkNameWord(String word) throws IOException {
        if (keywordsForNickname.isEmpty()) {
            init();
        }
        for (String key : keywordsForNickname) {
            if (word.contains(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检测是否有敏感词
     *
     * @param word
     * @param isReplace 是否替换敏感词
     * @return
     * @throws IOException
     */
    public static String checkNameWord(String word, boolean isReplace) throws IOException {
        if (keywordsForNickname.isEmpty()) {
            init();
        }
        for (String key : keywordsForNickname) {
            if (word.contains(key)) {
                if (isReplace) {
                    word = word.replaceAll(key, "**");
                }
            }
        }
        return word;
    }
    public static boolean checkCommentWord(String word) throws IOException {
    	if (keywordsForComment.isEmpty()) {
    		init();
    	}
    	for (String key : keywordsForComment) {
    		if (word.contains(key)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * 检测是否有敏感词
     *
     * @param word
     * @param isReplace 是否替换敏感词
     * @return
     * @throws IOException
     */
    public  static String checkCommentWord(String word, boolean isReplace) throws IOException {
    	if (keywordsForComment.isEmpty()) {
    		init();
    	}
    	for (String key : keywordsForComment) {
    		if (word.contains(key)) {
    			if (isReplace) {
    				word = word.replaceAll(key, "**");
    			}
    		}
    	}
    	return word;
    }
}	
