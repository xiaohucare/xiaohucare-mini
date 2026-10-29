package com.xiaohucare.framework.oss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.xiaohucare.framework.oss.constant.OSSConstants;
import com.xiaohucare.framework.oss.enums.StorageType;
import com.xiaohucare.framework.oss.service.IFileStorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * OSS核心配置类
 * 
 * @author xiaohucare
 */
@Slf4j
@Configuration
public class OssConfiguration {
    
    @Autowired
    private Environment environment;
    
    /**
     * 获取当前配置的存储类型
     * 
     * @return 存储类型
     */
    @Bean
    public String currentStorageType() {
        String type = environment.getProperty(OSSConstants.CURRENT_STORAGE_TYPE, "local");
        return type;
    }
    
    /**
     * 提供默认的文件存储服务
     * 根据配置动态选择具体的实现
     * 
     * @param localService 本地存储服务
     * @param aliyunService 阿里云OSS服务
     * @param huaweiService 华为云OSS服务
     * @param rustFsService RustFS服务
     * @return 文件存储服务
     */
    @Bean("fileStorageService")
    public IFileStorageService fileStorageService(
            IFileStorageService localService, 
            IFileStorageService aliyunService, 
            IFileStorageService huaweiService, 
            IFileStorageService rustFsService) {
        String storageType = currentStorageType();
        log.info("=======fileStorageService======"+storageType);
        switch (StorageType.getByCode(storageType)) {
            case ALIYUN:
                return aliyunService;
            case HUAWEI:
                return huaweiService;
            case RUSTFS:
                return rustFsService;
            case LOCAL:
            default:
                return localService;
        }
    }
}