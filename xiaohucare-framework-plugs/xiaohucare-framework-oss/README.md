# 智碳EMS OSS模块

## 项目介绍
智碳EMS OSS模块是一个统一的文件存储服务，支持多种存储方式，包括本地文件系统、阿里云OSS、华为云OSS和RustFS。通过简单的配置，用户可以轻松切换不同的存储后端，而不需要修改业务代码。

## 功能特点
- 统一的API接口，支持不同存储方式的无缝切换
- 支持本地文件系统存储
- 支持阿里云OSS存储
- 支持华为云OSS存储
- 支持RustFS存储
- 提供便捷的工具类，方便调用

## 目录结构
```
xiaohucare-ems-oss
├── src/main/java/com/xiaohucare/ems/oss
│   ├── constant/        # 常量类
│   ├── config/          # 配置类
│   ├── enums/           # 枚举类
│   ├── service/         # 服务接口和实现
│   └── utils/           # 工具类
├── src/main/resources   # 资源文件
└── pom.xml              # Maven配置文件
```

## 核心组件

### 1. 接口定义
- `IFileStorageService` - 定义文件操作的核心接口

### 2. 存储实现
- `LocalFileStorageService` - 本地文件系统实现
- `AliyunOssFileStorageService` - 阿里云OSS实现
- `HuaweiOssFileStorageService` - 华为云OSS实现
- `RustFsFileStorageService` - RustFS实现

### 3. 配置类
- `BaseOssProperties` - 基础配置抽象类
- `LocalOssProperties` - 本地存储配置
- `AliyunOssProperties` - 阿里云OSS配置
- `HuaweiOssProperties` - 华为云OSS配置
- `RustFsProperties` - RustFS配置
- `OssConfiguration` - 核心配置类，负责初始化和管理存储服务

### 4. 工具类
- `OssUtils` - 提供静态方法，方便调用文件存储服务

## 使用方法

### 1. 添加依赖
在需要使用文件存储服务的模块中添加依赖：
```xml
<dependency>
    <groupId>com.xiaohucare.app.ems</groupId>
    <artifactId>xiaohucare-ems-oss</artifactId>
    <version>3.17.0</version>
</dependency>
```

### 2. 配置存储方式
在`application.yml`或`application.properties`中配置所需的存储方式，例如：
```yaml
# 使用本地存储
oss:
  type: local
  local:
    enabled: true
    path: /data/oss/local
    prefix: upload
```

### 3. 注入服务或使用工具类

**方式一：注入IFileStorageService接口**
```java
@Service
public class YourService {
    
    @Autowired
    private IFileStorageService fileStorageService;
    
    public String uploadFile(MultipartFile file) {
        return fileStorageService.uploadFile(file);
    }
}
```

**方式二：使用OssUtils工具类**
```java
@Service
public class YourService {
    
    public String uploadFile(MultipartFile file) {
        return OssUtils.uploadFile(file);
    }
}
```

## 配置说明

### 本地存储配置
```yaml
oss:
  local:
    enabled: true             # 是否启用
    path: /data/oss/local     # 本地存储路径
    domain:                   # 访问域名（可选）
    https: false              # 是否使用https
    prefix: upload            # 存储路径前缀
```

### 阿里云OSS配置
```yaml
oss:
  aliyun:
    enabled: false              # 是否启用
    endpoint: oss-cn-hangzhou.aliyuncs.com  # 地域节点
    accessKeyId: your_access_key_id         # AccessKey ID
    accessKeySecret: your_access_key_secret # AccessKey Secret
    bucketName: your_bucket_name            # Bucket名称
    domain:                                 # 访问域名（可选）
    https: true                             # 是否使用https
    prefix: upload                          # 存储路径前缀
```

### 华为云OSS配置
```yaml
oss:
  huawei:
    enabled: false              # 是否启用
    endpoint: obs.cn-east-3.myhuaweicloud.com # 地域节点
    accessKeyId: your_access_key_id           # AccessKey ID
    accessKeySecret: your_access_key_secret   # AccessKey Secret
    bucketName: your_bucket_name              # 桶名称
    region: cn-east-3                         # 区域
    domain:                                   # 访问域名（可选）
    https: true                               # 是否使用https
    prefix: upload                            # 存储路径前缀
```

### RustFS配置
```yaml
oss:
  rustfs:
    enabled: false             # 是否启用
    apiUrl: http://localhost:8080/api  # API地址
    appId: your_app_id                  # 应用ID
    appSecret: your_app_secret          # 应用密钥
    timeout: 30000                      # 超时时间（毫秒）
    domain:                             # 访问域名（可选）
    https: false                        # 是否使用https
    prefix: upload                      # 存储路径前缀
```

## 切换存储方式
只需修改配置文件中的`oss.type`参数即可切换不同的存储方式：
```yaml
# 使用阿里云OSS
oss:
  type: aliyun
  # ...其他配置
```

支持的存储类型：`local`, `aliyun`, `huawei`, `rustfs`

## 注意事项
1. 使用云存储服务时，请确保配置的AccessKey和Secret具有足够的权限
2. 本地存储时，请确保应用程序对存储路径有读写权限
3. 切换存储方式后，已存储的文件不会自动迁移，需要手动处理