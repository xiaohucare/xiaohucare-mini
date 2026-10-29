package com.xiaohucare.framework.oss.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xiaohucare.framework.oss.config.RustFsProperties;

import cn.hutool.core.util.StrUtil;
import okhttp3.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RustFS文件存储服务实现
 * 
 * @author xiaohucare
 */
@Service("rustFsFileStorageService")
@ConditionalOnProperty(name = "oss.type", havingValue = "rustfs")
public class RustFsFileStorageService extends AbstractFileStorageService {
    
    private final RustFsProperties rustFsProperties;
    private final OkHttpClient okHttpClient;
    
    public RustFsFileStorageService(RustFsProperties rustFsProperties) {
        this.rustFsProperties = rustFsProperties;
        // 初始化OkHttpClient
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(rustFsProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(rustFsProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(rustFsProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
    @Override
  	public Object getStsToken() {
  		return null;
  	}
    @Override
    public String uploadFile(MultipartFile file) {
        validateFileSize(file);
        String originalFilename = file.getOriginalFilename();
        String fileName = generateFileName(originalFilename);
        try {
            return uploadFile(file.getInputStream(), fileName, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            return uploadFile(file.getInputStream(), fileName, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    @Override
    public String uploadFile(MultipartFile file,String prefix, String fileName) {
    	try {
    		return uploadFile(file.getInputStream(), fileName, file.getContentType());
    	} catch (IOException e) {
    		throw new RuntimeException("文件上传失败", e);
    	}
    }
    public String uploadFile(InputStream inputStream,String prefix,  String fileName, String contentType) {
        try {
            validateFileSize(inputStream);
            String filePath = buildFilePath(prefix, fileName);
            
            // 读取文件内容（Java 8兼容方式）
            byte[] fileContent = new byte[inputStream.available()];
            inputStream.read(fileContent);
            String base64Content = Base64.getEncoder().encodeToString(fileContent);
            // 构建请求体
            String requestBody = buildRequestBody("upload", filePath, base64Content, contentType);
            // 发送请求
            String response = sendRequest(requestBody);
            // 解析响应，假设响应返回文件URL或路径
            return getFileUrl(filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
    @Override
    public String uploadFile(InputStream inputStream, String fileName, String contentType) {
    	return uploadFile(inputStream,rustFsProperties.getPrefix(),fileName, contentType);
    }
    @Override
    public boolean deleteFile(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            
            // 构建请求体
            String requestBody = buildRequestBody("delete", realPath, null, null);
            
            // 发送请求
            String response = sendRequest(requestBody);
            
            // 解析响应，假设成功返回true
            return true;
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getFileInfo(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            
            // 构建请求体
            String requestBody = buildRequestBody("info", realPath, null, null);
            
            // 发送请求
            String response = sendRequest(requestBody);
            
            // 解析响应，构建文件信息
            Map<String, Object> fileInfo = parseFileInfoResponse(response);
            fileInfo.put("path", filePath);
            fileInfo.put("storageType", getStorageType());
            
            return fileInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
    
    @Override
    public InputStream downloadFile(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            
            // 构建请求体
            String requestBody = buildRequestBody("download", realPath, null, null);
            
            // 发送请求
            String response = sendRequest(requestBody);
            
            // 解析响应，假设返回base64编码的文件内容
            byte[] fileContent = Base64.getDecoder().decode(response);
            
            return new java.io.ByteArrayInputStream(fileContent);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    @Override
    public String getFileUrl(String filePath) {
        if (StrUtil.isNotEmpty(rustFsProperties.getDomain())) {
            String protocol = rustFsProperties.isHttps() ? "https://" : "http://";
            return protocol + rustFsProperties.getDomain() + "/" + filePath;
        }
        
        // 默认返回相对路径
        return rustFsProperties.getApiUrl() + (rustFsProperties.getApiUrl().endsWith("/") ? "" : "/") + "file/" + filePath;
    }
    
    @Override
    public String getStorageType() {
        return "rustfs";
    }
    @Override
    public String getPrefix() {
    	return rustFsProperties.getPrefix();
    }
    /**
     * 构建请求体
     */
    private String buildRequestBody(String action, String filePath, String content, String contentType) {
        // 这里根据RustFS的API要求构建请求体
        // 假设RustFS接受JSON格式的请求
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"action\":\"").append(action).append("\",");
        sb.append("\"filePath\":\"").append(filePath).append("\",");
        if (StrUtil.isNotEmpty(content)) {
            sb.append("\"content\":\"").append(content).append("\",");
        }
        if (StrUtil.isNotEmpty(contentType)) {
            sb.append("\"contentType\":\"").append(contentType).append("\",");
        }
        // 添加认证信息
        sb.append("\"appId\":\"").append(rustFsProperties.getAppId()).append("\",");
        sb.append("\"appSecret\":\"").append(rustFsProperties.getAppSecret()).append("\"");
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 发送HTTP请求
     */
    private String sendRequest(String requestBody) throws IOException {
        RequestBody body = RequestBody.create(
                requestBody, 
                MediaType.parse("application/json; charset=utf-8"));
        
        Request request = new Request.Builder()
                .url(rustFsProperties.getApiUrl())
                .post(body)
                .build();
        
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("RustFS请求失败: " + response.code());
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RuntimeException("RustFS响应为空");
            }
            return responseBody.string();
        }
    }
    
    /**
     * 解析文件信息响应
     */
    private Map<String, Object> parseFileInfoResponse(String response) {
        // 这里根据RustFS的响应格式进行解析
        // 简化实现，实际需要根据真实响应格式解析
        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("name", response); // 实际应用中需要解析JSON
        fileInfo.put("size", 0L);
        fileInfo.put("type", "application/octet-stream");
        fileInfo.put("ext", "");
        fileInfo.put("createTime", System.currentTimeMillis());
        
        return fileInfo;
    }
    
    /**
     * 移除URL前缀
     */
    private String removeUrlPrefix(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }
        
        // 移除协议和域名部分
        if (filePath.contains("://")) {
            int startIndex = filePath.indexOf("://") + 3;
            int endIndex = filePath.indexOf('/', startIndex);
            if (endIndex > 0) {
                return filePath.substring(endIndex + 1);
            }
        }
        
        // 移除API URL前缀
        if (filePath.startsWith(rustFsProperties.getApiUrl())) {
            String relativePath = filePath.substring(rustFsProperties.getApiUrl().length());
            // 移除可能的"file/"前缀
            if (relativePath.startsWith("/file/")) {
                return relativePath.substring(6);
            }
            return relativePath.replaceFirst("^/", "");
        }
        
        return filePath;
    }
}