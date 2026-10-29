package com.xiaohucare.framework.oss.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xiaohucare.framework.oss.config.HuaweiOssProperties;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.ObsObject;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.ObjectMetadata;

import cn.hutool.core.util.StrUtil;

/**
 * 华为云OSS文件存储服务实现
 * 
 * @author xiaohucare
 */
@ConditionalOnProperty(name = "oss.type", havingValue = "huawei")
@Service("huaweiOssFileStorageService")
public class HuaweiOssFileStorageService extends AbstractFileStorageService {
    
    private final HuaweiOssProperties huaweiOssProperties;
    private final ObsClient obsClient;
    
    public HuaweiOssFileStorageService(HuaweiOssProperties huaweiOssProperties) {
        this.huaweiOssProperties = huaweiOssProperties;
        // 只有在配置了有效的AccessKey时才初始化OBS客户端
        if (StrUtil.isNotEmpty(huaweiOssProperties.getAccessKeyId()) && 
            StrUtil.isNotEmpty(huaweiOssProperties.getAccessKeySecret())) {
            this.obsClient = new ObsClient(
                    huaweiOssProperties.getAccessKeyId(),
                    huaweiOssProperties.getAccessKeySecret(),
                    huaweiOssProperties.getEndpoint());
        } else {
            this.obsClient = null;
        }
    }
    @Override
  	public Object getStsToken() {
  		return null;
  	}
    @Override
    public String uploadFile(MultipartFile file) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
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
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        try {
            return uploadFile(file.getInputStream(), fileName, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    @Override
    public String uploadFile(MultipartFile file,String prefix, String fileName) {
    	if (obsClient == null) {
    		throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
    	}
    	try {
    		return uploadFile(file.getInputStream(), fileName, file.getContentType());
    	} catch (IOException e) {
    		throw new RuntimeException("文件上传失败", e);
    	}
    }
    public String uploadFile(InputStream inputStream,String prefix,  String fileName, String contentType) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        try {
            validateFileSize(inputStream);
            String filePath = buildFilePath(prefix, fileName);
            
            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    huaweiOssProperties.getBucketName(), 
                    filePath, 
                    inputStream);
            
            // 设置文件类型
            ObjectMetadata metadata = new ObjectMetadata();
            if (StrUtil.isNotEmpty(contentType)) {
                metadata.setContentType(contentType);
            }
            putObjectRequest.setMetadata(metadata);
            
            // 上传文件
            obsClient.putObject(putObjectRequest);
            
            return getFileUrl(filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        } catch (ObsException e) {
            throw new RuntimeException("华为云OSS上传失败: " + e.getErrorMessage(), e);
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
    	return uploadFile(inputStream,huaweiOssProperties.getPrefix(),fileName,contentType);
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            obsClient.deleteObject(huaweiOssProperties.getBucketName(), realPath);
            return true;
        } catch (ObsException e) {
            throw new RuntimeException("文件删除失败: " + e.getErrorMessage(), e);
        }
    }
    
    @Override
    public Map<String, Object> getFileInfo(String filePath) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            ObjectMetadata metadata = obsClient.getObjectMetadata(
                    huaweiOssProperties.getBucketName(), 
                    realPath);
            
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("path", filePath);
            fileInfo.put("name", realPath.substring(realPath.lastIndexOf('/') + 1));
            fileInfo.put("size", metadata.getContentLength());
            fileInfo.put("type", metadata.getContentType());
            fileInfo.put("ext", getFileExtension(realPath));
            fileInfo.put("storageType", getStorageType());
            fileInfo.put("createTime", metadata.getLastModified().getTime());
            
            // 添加华为云OSS特有信息
            fileInfo.put("etag", metadata.getEtag());
            fileInfo.put("bucket", huaweiOssProperties.getBucketName());
            
            return fileInfo;
        } catch (ObsException e) {
            throw new RuntimeException("获取文件信息失败: " + e.getErrorMessage(), e);
        }
    }
    
    @Override
    public InputStream downloadFile(String filePath) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            ObsObject obsObject = obsClient.getObject(
                    huaweiOssProperties.getBucketName(), 
                    realPath);
            return obsObject.getObjectContent();
        } catch (ObsException e) {
            throw new RuntimeException("文件下载失败: " + e.getErrorMessage(), e);
        }
    }
    
    @Override
    public String getFileUrl(String filePath) {
        if (obsClient == null) {
            throw new RuntimeException("华为云OSS客户端未正确初始化，请检查配置");
        }
        if (StrUtil.isNotEmpty(huaweiOssProperties.getDomain())) {
            String protocol = huaweiOssProperties.isHttps() ? "https://" : "http://";
            return protocol + huaweiOssProperties.getDomain() + "/" + filePath;
        }
        
        // 生成带签名的临时URL（有效期10年）
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365 * 10));
        TemporarySignatureRequest request = new TemporarySignatureRequest();
        request.setMethod(HttpMethodEnum.GET);
        request.setExpires(expiration.getTime() / 1000);
        request.setBucketName(huaweiOssProperties.getBucketName());
        request.setObjectKey(filePath);
        
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        return response.getSignedUrl();
    }
    
    @Override
    public String getStorageType() {
        return "huawei";
    }
    @Override
    public String getPrefix() {
    	return huaweiOssProperties.getPrefix();
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
        
        // 移除bucket域名
        String bucketDomain = huaweiOssProperties.getBucketName() + "." + huaweiOssProperties.getEndpoint().replaceFirst("https?://", "");
        if (filePath.contains(bucketDomain)) {
            int startIndex = filePath.indexOf(bucketDomain) + bucketDomain.length();
            if (startIndex < filePath.length()) {
                return filePath.substring(startIndex).replaceFirst("^/", "");
            }
        }
        
        return filePath;
    }
}