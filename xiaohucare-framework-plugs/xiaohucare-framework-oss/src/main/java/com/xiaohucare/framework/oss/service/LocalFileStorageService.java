package com.xiaohucare.framework.oss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xiaohucare.framework.oss.config.LocalOssProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 本地文件存储服务实现
 * 
 * @author xiaohucare
 */
@Slf4j
@Service("localFileStorageService")
@ConditionalOnProperty(name = "oss.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageService extends AbstractFileStorageService {
    
    private final LocalOssProperties localOssProperties;
    
    public LocalFileStorageService(LocalOssProperties localOssProperties) {
        this.localOssProperties = localOssProperties;
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
        return uploadFile(file, fileName);
    }
    
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            String filePath = buildFilePath(localOssProperties.getPrefix(), fileName);
            File dest = getAbsoluteFile(localOssProperties.getPath(), filePath);
            file.transferTo(dest);
            return getFileUrl(filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    @Override
    public String uploadFile(MultipartFile file,String prefix, String fileName) {
    	try {
    		String filePath = buildFilePath(prefix, fileName);
    		File dest = getAbsoluteFile(localOssProperties.getPath(), filePath);
    		file.transferTo(dest);
    		return getFileUrl(filePath);
    	} catch (IOException e) {
    		throw new RuntimeException("文件上传失败", e);
    	}
    }
    
    public String uploadFile(InputStream inputStream,String prefix, String fileName, String contentType) {
        try {
            validateFileSize(inputStream);
            String filePath = buildFilePath(prefix, fileName);
            File dest = getAbsoluteFile(localOssProperties.getPath(), filePath);
            
            try (FileOutputStream out = new FileOutputStream(dest)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            
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
        return uploadFile(inputStream, localOssProperties.getPrefix(),fileName, contentType);
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            File file = new File(localOssProperties.getPath() + File.separator + realPath);
            return file.exists() && file.delete();
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getFileInfo(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            File file = new File(localOssProperties.getPath() + File.separator + realPath);
            
            if (!file.exists() || file.isDirectory()) {
                throw new RuntimeException("文件不存在");
            }
            
            String fileName = file.getName();
            return createFileInfo(filePath, fileName, file.length(), getContentType(fileName));
        } catch (Exception e) {
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
    
    @Override
    public InputStream downloadFile(String filePath) {
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            File file = new File(localOssProperties.getPath() + File.separator + realPath);
            
            if (!file.exists() || file.isDirectory()) {
                throw new RuntimeException("文件不存在");
            }
            
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    @Override
    public String getFileUrl(String filePath) {
        String baseUrl = getBaseUrl();
        return baseUrl + (baseUrl.endsWith("/") ? "" : "/") + filePath;
    }
    
    @Override
    public String getStorageType() {
        return "local";
    }
    @Override
    public String getPrefix() {
    	return localOssProperties.getPrefix();
    }
    /**
     * 获取绝对文件
     */
    private File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File parentFile=new File(uploadDir);
         if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String filePathAndName=uploadDir + File.separator + fileName;
        System.out.println(filePathAndName);
        File desc = new File(filePathAndName);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }
    
    /**
     * 获取基础URL
     */
    private String getBaseUrl() {
        if (StringUtils.hasText(localOssProperties.getDomain())) {
            return (localOssProperties.isHttps() ? "https://" : "http://") + localOssProperties.getDomain();
        }
        // 默认使用相对路径
        return "/profile";
    }
    
    /**
     * 移除URL前缀
     */
    private String removeUrlPrefix(String filePath) {
        String baseUrl = getBaseUrl();
        if (filePath.startsWith(baseUrl)) {
            return filePath.substring(baseUrl.length());
        }
        return filePath;
    }
    
    /**
     * 获取文件内容类型
     */
    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        // 简单的内容类型映射
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "doc":
            case "docx":
                return "application/msword";
            case "xls":
            case "xlsx":
                return "application/vnd.ms-excel";
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            default:
                return "application/octet-stream";
        }
    }
}