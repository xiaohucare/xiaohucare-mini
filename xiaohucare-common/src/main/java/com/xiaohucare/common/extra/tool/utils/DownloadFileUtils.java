package com.xiaohucare.common.extra.tool.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.xiaohucare.common.utils.file.FileUtils;

public class DownloadFileUtils {
	
	/**
	 * 获取 MIME 类型
	 */
	public static String getContentType(String fileName) {
		try {
			String contentType = Files.probeContentType(java.nio.file.Paths.get(fileName));
			return contentType != null ? contentType : "application/octet-stream";
		} catch (Exception e) {
			return "application/octet-stream";
		}
	}
	
	public static  void saveToFileAndToZip(String zippath,String destUrl,String name,String zipPathAndName) throws IOException {
        saveToFiles(destUrl, zippath+name);
        ZipUtils.doCompress(zippath, zipPathAndName);
    }
	public static  void saveToFilesToZip(String zippath,String zipPathAndName) throws IOException {
		ZipUtils.doCompress(zippath, zipPathAndName);
	}
	
	public static  File saveToFiles(String destUrl,String pathAndName) throws IOException {
        final int BUFFER_SIZE = 4096;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpconn = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0; 
        File storeFile = new File(pathAndName);
		if (!storeFile.getParentFile().exists()) {
			storeFile.getParentFile().mkdirs();
		}
        System.out.println("file download url is "+destUrl+",path is "+pathAndName+"---------------");
        url = new URL(destUrl);
        httpconn = (HttpURLConnection) url.openConnection();
        httpconn.setRequestProperty("Host", "daliyuapp.cn");
        httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");
        httpconn.setRequestProperty("Cookie", "PHPSESSID=1d888d20bba26e676811ebf7f0cd109b");
        try{
            httpconn.setReadTimeout(100000); // 设置读取文件超时时间
            httpconn.connect();
            String type = httpconn.getContentType();
            int statuscode = httpconn.getResponseCode(); // 建立链接后先查询url的状态码
            if(statuscode == 200&&type.contains("application/")){ //链接正常。并得到了返回信息
                bis = new BufferedInputStream(httpconn.getInputStream()); // 建立管道，准备读取数据流
                fos = new FileOutputStream(storeFile);
                System.out.println("it is to receive link[" + destUrl + "]content/n be save file is:[" + storeFile + "]");
                while ((size = bis.read(buf)) != -1){ // 保存文件到本地磁盘
                    fos.write(buf, 0, size);
                }
                fos.close();
                bis.close();
                httpconn.disconnect();
                System.out.println("saveToFiles file is "+storeFile.getName()+" size is "+storeFile.length());
            }else if(statuscode == 200&&type.contains("image/")){ //链接正常。并得到了返回信息
                bis = new BufferedInputStream(httpconn.getInputStream()); // 建立管道，准备读取数据流
                fos = new FileOutputStream(storeFile);
                System.out.println("it is to receive link[" + destUrl + "]content/n be save file is:[" + storeFile+ "]");
                while ((size = bis.read(buf)) != -1){ // 保存文件到本地磁盘
                    fos.write(buf, 0, size);
                }
                System.out.println("file download url is "+destUrl+",path is"+pathAndName+"  "+new Date());
                fos.close();
                bis.close();
                httpconn.disconnect();
                System.out.println("saveToFiles file is "+storeFile.getName()+" size is "+storeFile.length());
            }else if(statuscode == 200&&type.contains("video/")){ //链接正常。并得到了返回信息
                bis = new BufferedInputStream(httpconn.getInputStream()); // 建立管道，准备读取数据流
                fos = new FileOutputStream(storeFile);
                System.out.println("it is to receive link[" + destUrl + "]content/n be save file is:[" + storeFile+ "]");
                while ((size = bis.read(buf)) != -1){ // 保存文件到本地磁盘
                    fos.write(buf, 0, size);
                }
                System.out.println("file download url is "+destUrl+",path is"+pathAndName+"  "+new Date());
                fos.close();
                bis.close();
                httpconn.disconnect();
                System.out.println("saveToFiles file is "+storeFile.getName()+" size is "+storeFile.length());
            }else{
            	  System.out.println("--下载失败----");
                return null;
            }
        }catch(Exception e){
            System.out.println(destUrl+" this url connection timeout ");
            System.out.println("------"+e.toString());
            return null;
        }
        return storeFile;
    }
	public static  byte[]  saveToFile(String destUrl) throws IOException {
		HttpURLConnection httpconn = null;
		ByteArrayOutputStream output=null;
		URL url = null;
		url = new URL(destUrl);
		httpconn = (HttpURLConnection) url.openConnection();
		httpconn.setRequestProperty("Host", "daliyuapp.com");
		httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");
		httpconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
		httpconn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
		httpconn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		httpconn.setRequestProperty("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
		httpconn.setRequestProperty("Keep-Alive", "300");
		httpconn.setRequestProperty("Connection", "Keep-Alive");
		try{
			httpconn.setReadTimeout(100000); // 设置读取文件超时时间
			httpconn.connect();
			String type = httpconn.getContentType();
			int statuscode = httpconn.getResponseCode(); // 建立链接后先查询url的状态码
			if(statuscode == 200&&(type.contains("application/")||type.contains("image/"))){ //链接正常。并得到了返回信息
				InputStream input=httpconn.getInputStream();
				output = new ByteArrayOutputStream();
				System.out.println(destUrl+" this url connection ok  ");
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
				}
				output.flush();
				httpconn.disconnect();
			}else{
				 System.out.println("--下载失败----");
				return null;
			}
		}catch(Exception e){
			System.out.println(destUrl+" this url connection timeout ");
			System.out.println(e.toString());
			return null;
		}
		return output.toByteArray();
	}
	public static  File saveToImageFiles(String destUrl,String pathAndName) throws IOException {
        final int BUFFER_SIZE = 4096;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpconn = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;  
        File storeFile = new File(pathAndName);
		if (!storeFile.getParentFile().exists()) {
			storeFile.getParentFile().mkdirs();
		}
        System.out.println("file download url is "+destUrl+",path is "+pathAndName+"---------------");
        // connection
        url = new URL(destUrl);
        httpconn = (HttpURLConnection) url.openConnection();
       // httpconn.setRequestProperty("Host", "yishiyihua.com");
        httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");
        httpconn.setRequestProperty("Accept", "image/webp,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
        httpconn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
        httpconn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        httpconn.setRequestProperty("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
        httpconn.setRequestProperty("Keep-Alive", "300");
        httpconn.setRequestProperty("Connection", "Keep-Alive");
        try{
            httpconn.setReadTimeout(40000); // 设置读取文件超时时间
            httpconn.connect();
            String type = httpconn.getContentType();
            int statuscode = httpconn.getResponseCode(); // 建立链接后先查询url的状态码
            if(statuscode == 200&&type.contains("image/")){ //链接正常。并得到了返回信息
                bis = new BufferedInputStream(httpconn.getInputStream()); // 建立管道，准备读取数据流
                fos = new FileOutputStream(storeFile);
                System.out.println("it is to receive link[" + destUrl + "]content/n be save file is:[" + storeFile
                        + "]");
                
                while ((size = bis.read(buf)) != -1){ // 保存文件到本地磁盘
                    fos.write(buf, 0, size);
                }
                System.out.println("file download url is "+destUrl+",path is"+pathAndName+"  "+new Date());
                fos.close();
                bis.close();
                httpconn.disconnect();
                System.out.println("saveToFiles file is "+storeFile.getName()+" size is "+storeFile.length());
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println(destUrl+" this url connection timeout ");
            System.out.println(e.toString());
            return null;
        }
        return storeFile;
    }
	
	public static  ByteArrayInputStream saveToFiles(String destUrl) throws IOException {
        HttpURLConnection httpconn = null;
        ByteArrayInputStream byteArrayInputStream=null;
        ByteArrayOutputStream output=null;
        URL url = null;
        url = new URL(destUrl);
        httpconn = (HttpURLConnection) url.openConnection();
        httpconn.setRequestProperty("Host", "yishiyihua.com");
        httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");
        httpconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
        httpconn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
        httpconn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        httpconn.setRequestProperty("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
        httpconn.setRequestProperty("Keep-Alive", "300");
        httpconn.setRequestProperty("Connection", "Keep-Alive");
        try{
            httpconn.setReadTimeout(40000); // 设置读取文件超时时间
            httpconn.connect();
            String type = httpconn.getContentType();
            int statuscode = httpconn.getResponseCode(); // 建立链接后先查询url的状态码
            System.out.println(statuscode);
            if(statuscode == 200&&type.contains("image/")){ //链接正常。并得到了返回信息
                InputStream input=httpconn.getInputStream();
                output = new ByteArrayOutputStream();
                System.out.println(destUrl+" this url connection ok  ");
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.flush();
                byteArrayInputStream = new ByteArrayInputStream(output.toByteArray());  
                httpconn.disconnect();
            }else if(statuscode == 200&&type.contains("application/")){ //链接正常。并得到了返回信息
            	  InputStream input=httpconn.getInputStream();
                  output = new ByteArrayOutputStream();
                  System.out.println(destUrl+" this url connection ok  ");
                  byte[] buffer = new byte[4096];
                  int n = 0;
                  while (-1 != (n = input.read(buffer))) {
                      output.write(buffer, 0, n);
                  }
                  output.flush();
                  byteArrayInputStream = new ByteArrayInputStream(output.toByteArray());  
                  httpconn.disconnect();
            }else if(statuscode == 200&&type.contains("video/")){ //链接正常。并得到了返回信息
          	  InputStream input=httpconn.getInputStream();
              output = new ByteArrayOutputStream();
              System.out.println(destUrl+" this url connection ok  ");
              byte[] buffer = new byte[4096];
              int n = 0;
              while (-1 != (n = input.read(buffer))) {
                  output.write(buffer, 0, n);
              }
              output.flush();
              byteArrayInputStream = new ByteArrayInputStream(output.toByteArray());  
              httpconn.disconnect();
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println(destUrl+" this url connection timeout ");
            System.out.println(e.toString());
            return null;
        }
        return byteArrayInputStream;
    }
	
	public static  byte[] saveToByte(String destUrl) throws IOException {
        HttpURLConnection httpconn = null;
        byte[] byteArray=null;
        ByteArrayOutputStream output=null;
        URL url = null;
        url = new URL(destUrl);
        httpconn = (HttpURLConnection) url.openConnection();
        httpconn.setRequestProperty("Host", "yishiyihua.com");
        httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");
        httpconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
        httpconn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
        httpconn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        httpconn.setRequestProperty("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
        httpconn.setRequestProperty("Keep-Alive", "300");
        httpconn.setRequestProperty("Connection", "Keep-Alive");
        try{
            httpconn.setReadTimeout(40000); // 设置读取文件超时时间
            httpconn.connect();
            String type = httpconn.getContentType();
            int statuscode = httpconn.getResponseCode(); // 建立链接后先查询url的状态码
            if(statuscode == 200){ //链接正常。并得到了返回信息
                InputStream input=httpconn.getInputStream();
                output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.flush();
                byteArray = output.toByteArray();  
                httpconn.disconnect();
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println(destUrl+" this url connection timeout ");
            System.out.println(e.toString());
            return null;
        }
        return byteArray;
    }
    /**
     * 下载http文件流
     *
     * @param urlStr
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    public static void downloadHttpFile(String urlStr, HttpServletRequest request, HttpServletResponse response, String fileName) {
        ServletOutputStream out = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = inputStreamToByte(inputStream);
            // 下载
            out = response.getOutputStream();
            long contentLength = getData.length;
            setResponse(fileName, contentLength, request, response);
            out.write(getData);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("下载失败!");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //File、FileInputStream 转换为byte数组
    public static byte[] inputStreamToByte(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("文件转换失败!");
        }
    }
    /**
     * @param fileName
     * @param contentLength
     * @param request
     * @param response
     * @return
     */
    public static void setResponse(String fileName, long contentLength, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType(getContentType("0.jpg"));
            boolean isPreview = "preview".equalsIgnoreCase(request.getParameter("source"));
            response.addHeader("Content-Disposition", (!isPreview ? "attachment; " : "") + "filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Accept-Ranges", "bytes");
            String range = request.getHeader("Range");
            if (range == null) {
                response.setHeader("Content-Length", String.valueOf(contentLength));
            } else {
                response.setStatus(206);
                long requestStart = 0L;
                long requestEnd = 0L;
                String[] ranges = range.split("=");
                if (ranges.length > 1) {
                    String[] rangeDatas = ranges[1].split("-");
                    requestStart = Long.parseLong(rangeDatas[0]);
                    if (rangeDatas.length > 1) {
                        requestEnd = Long.parseLong(rangeDatas[1]);
                    }
                }
                long length = 0L;
                if (requestEnd > 0L) {
                    length = requestEnd - requestStart + 1L;
                    response.setHeader("Content-Length", String.valueOf(length));
                    response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
                } else {
                    length = contentLength - requestStart;
                    response.setHeader("Content-Length", String.valueOf(length));
                    response.setHeader("Content-Range", "bytes " + requestStart + "-" + (contentLength - 1L) + "/" + contentLength);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("response响应失败!");
        }
    }

}
