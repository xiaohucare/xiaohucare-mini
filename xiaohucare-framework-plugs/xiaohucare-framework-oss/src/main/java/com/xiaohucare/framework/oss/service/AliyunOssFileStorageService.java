package com.xiaohucare.framework.oss.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.credentials.Client;
import com.aliyun.credentials.models.Config;
import com.aliyun.sts20150401.models.AssumeRoleRequest;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials;
import com.xiaohucare.framework.oss.config.AliyunOssProperties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云OSS文件存储服务实现
 * 
 * @author xiaohucare
 */
@Slf4j
@ConditionalOnProperty(name = "oss.type", havingValue = "aliyun")
@Service("aliyunOssFileStorageService")
public class AliyunOssFileStorageService extends AbstractFileStorageService {
    
    private final AliyunOssProperties aliyunOssProperties;
    private final OSS ossClient;
    
    public AliyunOssFileStorageService(AliyunOssProperties aliyunOssProperties) {
        this.aliyunOssProperties = aliyunOssProperties;
        // 只有在配置了有效的AccessKey时才初始化OSS客户端
        if (StrUtil.isNotEmpty(aliyunOssProperties.getAccessKeyId()) && 
            StrUtil.isNotEmpty(aliyunOssProperties.getAccessKeySecret())) {
            this.ossClient = new OSSClientBuilder().build(
                    aliyunOssProperties.getEndpoint(),
                    aliyunOssProperties.getAccessKeyId(),
                    aliyunOssProperties.getAccessKeySecret());
        } else {
            this.ossClient = null;
        }
    }
    
    @Override
	public Object getStsToken() {
        try {
            // 使用RAM账号的AccessKey调用STS服务
            String accessKeyId = aliyunOssProperties.getAccessKeyId();
            String accessKeySecret = aliyunOssProperties.getAccessKeySecret();
            
            // 配置认证信息
            Config authConfig = new Config();
            authConfig.type = "access_key";
            authConfig.accessKeyId = accessKeyId;
            authConfig.accessKeySecret = accessKeySecret;
            Client credentialClient = new Client(authConfig);
            
            // 创建STS客户端配置
            com.aliyun.teaopenapi.models.Config stsConfig = new com.aliyun.teaopenapi.models.Config();
            stsConfig.setEndpoint("sts.aliyuncs.com");
            stsConfig.setCredential(credentialClient);
            
            // 创建STS客户端
            com.aliyun.sts20150401.Client stsClient = new com.aliyun.sts20150401.Client(stsConfig);
            
            // 构建请求
            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setRoleArn(aliyunOssProperties.getRoleArn());
            request.setRoleSessionName("oss-upload-session");
            request.setDurationSeconds(3600L);  // 有效期1小时
            
            // 调用STS服务
            AssumeRoleResponse response = stsClient.assumeRole(request);
            AssumeRoleResponseBodyCredentials credentials = response.getBody().getCredentials();
            
            // 封装返回结果
            StsTokenResult result = new StsTokenResult();
            result.setAccessKeyId(credentials.getAccessKeyId());
            result.setAccessKeySecret(credentials.getAccessKeySecret());
            result.setSecurityToken(credentials.getSecurityToken());
            result.setExpiration(credentials.getExpiration());
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取STS临时凭证失败: " + e.getMessage(), e);
        }
	}

    /**
     * STS临时凭证返回结果
     */
    @Data
    public static class StsTokenResult {
        private String accessKeyId;
        private String accessKeySecret;
        private String securityToken;
        private String expiration;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
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
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        try {
            return uploadFile(file.getInputStream(), fileName, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    @Override
    public String uploadFile(MultipartFile file,String prefix, String fileName) {
    	if (ossClient == null) {
    		throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
    	}
    	try {
    		return uploadFile(file.getInputStream(),prefix,fileName, file.getContentType());
    	} catch (IOException e) {
    		throw new RuntimeException("文件上传失败", e);
    	}
    }
    public String uploadFile(InputStream inputStream,String prefix, String fileName, String contentType) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        try {
            validateFileSize(inputStream);
            String filePath = buildFilePath(prefix, fileName);
            
            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    aliyunOssProperties.getBucketName(), 
                    filePath, 
                    inputStream);
            
            // 设置文件类型
            ObjectMetadata metadata = new ObjectMetadata();
            if (StrUtil.isNotEmpty(contentType)) {
                metadata.setContentType(contentType);
            }
            putObjectRequest.setMetadata(metadata);
            
            // 上传文件
            ossClient.putObject(putObjectRequest);
            
            return getFileUrl(filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        } catch (Exception e) {
            throw new RuntimeException("阿里云OSS上传失败", e);
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
    	return uploadFile(inputStream,aliyunOssProperties.getPrefix(),fileName, contentType);
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            ossClient.deleteObject(aliyunOssProperties.getBucketName(), realPath);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getFileInfo(String filePath) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            ObjectMetadata metadata = ossClient.getObjectMetadata(
                    aliyunOssProperties.getBucketName(), 
                    realPath);
            
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("path", filePath);
            fileInfo.put("name", realPath.substring(realPath.lastIndexOf('/') + 1));
            fileInfo.put("size", metadata.getContentLength());
            fileInfo.put("type", metadata.getContentType());
            fileInfo.put("ext", getFileExtension(realPath));
            fileInfo.put("storageType", getStorageType());
            fileInfo.put("createTime", metadata.getLastModified().getTime());
            
            // 添加阿里云OSS特有信息
            fileInfo.put("etag", metadata.getETag());
            fileInfo.put("bucket", aliyunOssProperties.getBucketName());
            
            return fileInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
    
    @Override
    public InputStream downloadFile(String filePath) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        try {
            // 移除URL前缀
            String realPath = removeUrlPrefix(filePath);
            OSSObject ossObject = ossClient.getObject(
                    aliyunOssProperties.getBucketName(), 
                    realPath);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
	@Override
	public String getFileUrl(String filePath) {
		if (ossClient == null) {
			throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
		}
		if (StrUtil.isEmpty(filePath)) {
			return filePath;
		}
		// 如果配置了自定义域名，直接返回（不需要签名）
		if (StrUtil.isNotEmpty(aliyunOssProperties.getDomain())) {
			String protocol = aliyunOssProperties.isHttps() ? "https://" : "http://";
			return protocol + aliyunOssProperties.getDomain() + "/" + filePath;
		}
		// 移除URL前缀获取真实路径
		String realPath = removeUrlPrefix(filePath);
		String protocol = aliyunOssProperties.isHttps() ? "https://" : "http://";
		return protocol + aliyunOssProperties.getDomain() + "/" + realPath;
	}
    
    @Override
    public String getFileUrl(String filePath, int expireSeconds) {
        if (ossClient == null) {
            throw new RuntimeException("阿里云OSS客户端未正确初始化，请检查配置");
        }
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }
        // 移除URL前缀获取真实路径
        String realPath = removeUrlPrefix(filePath);
        // 生成带签名的临时URL
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireSeconds));
        URL url = ossClient.generatePresignedUrl(
                aliyunOssProperties.getBucketName(), 
                realPath, 
                expiration);
        
        return url.toString();
    }
    
    @Override
    public String getStorageType() {
        return "aliyun";
    }
    @Override
    public String getPrefix() {
    	return aliyunOssProperties.getPrefix();
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
        String bucketDomain = aliyunOssProperties.getBucketName() + "." + aliyunOssProperties.getEndpoint();
        if (filePath.contains(bucketDomain)) {
            int startIndex = filePath.indexOf(bucketDomain) + bucketDomain.length();
            if (startIndex < filePath.length()) {
                return filePath.substring(startIndex).replaceFirst("^/", "");
            }
        }
        
        return filePath;
    }

	
}