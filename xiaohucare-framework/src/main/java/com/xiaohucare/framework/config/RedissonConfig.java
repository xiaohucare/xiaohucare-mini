//package com.xiaohucare.framework.config;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.TimeZone;
//
//import org.redisson.client.codec.StringCodec;
//import org.redisson.codec.CompositeCodec;
//import org.redisson.codec.TypedJsonJacksonCodec;
//import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.xiaohucare.common.core.redis.KeyPrefixHandler;
//import com.xiaohucare.common.utils.spring.SpringUtils;
//import com.xiaohucare.framework.config.properties.RedissonProperties;
//import com.xiaohucare.framework.core.exception.RedisExceptionHandler;
//
//import cn.hutool.core.util.ObjectUtil;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * redis配置
// *
// * @author Lion Li
// */
//@Slf4j
//@AutoConfiguration
//@EnableConfigurationProperties(RedissonProperties.class)
//public class RedissonConfig {
//
//    @Autowired
//    private RedissonProperties redissonProperties;
//
//    @Bean
//    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
//        return config -> {
//            JavaTimeModule javaTimeModule = new JavaTimeModule();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//            ObjectMapper om = new ObjectMapper();
//            om.registerModule(javaTimeModule);
//            om.setTimeZone(TimeZone.getDefault());
//            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//            // 指定序列化输入的类型，类必须是非final修饰的。序列化时将对象全类名一起保存下来
//            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
////            LoggerFactory.useSlf4jLogging(true);
////            FuryCodec furyCodec = new FuryCodec();
////            CompositeCodec codec = new CompositeCodec(StringCodec.INSTANCE, furyCodec, furyCodec);
//            TypedJsonJacksonCodec jsonCodec = new TypedJsonJacksonCodec(Object.class, om);
//            // 组合序列化 key 使用 String 内容使用通用 json 格式
//            CompositeCodec codec = new CompositeCodec(StringCodec.INSTANCE, jsonCodec, jsonCodec);
//            config.setThreads(redissonProperties.getThreads())
//                .setNettyThreads(redissonProperties.getNettyThreads())
//                // 缓存 Lua 脚本 减少网络传输(redisson 大部分的功能都是基于 Lua 脚本实现)
//                .setUseScriptCache(true)
//                .setCodec(codec);
//            // 虚拟线程支持（需要 Java 21+ 和 Spring 6.1+）
//            // if (SpringUtils.isVirtual()) {
//            //     config.setNettyExecutor(new VirtualThreadTaskExecutor("redisson-"));
//            // }
//            RedissonProperties.SingleServerConfig singleServerConfig = redissonProperties.getSingleServerConfig();
//            if (ObjectUtil.isNotNull(singleServerConfig)) {
//                // 使用单机模式
//                config.useSingleServer()
//                    //设置redis key前缀
//                    .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
//                    // Redis 连接地址
//                    .setAddress("redis://" + redissonProperties.getHost() + ":" + redissonProperties.getPort())
//                    // Redis 数据库索引
//                    .setDatabase(redissonProperties.getDatabase())
//                    // Redis 密码（如果有）
//                    .setPassword(redissonProperties.getPassword())
//                    // 连接超时时间
//                    .setTimeout(singleServerConfig.getTimeout())
//                    // 客户端名称
//                    .setClientName(singleServerConfig.getClientName())
//                    // 连接空闲超时
//                    .setIdleConnectionTimeout(singleServerConfig.getIdleConnectionTimeout())
//                    // 发布和订阅连接池大小
//                    .setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
//                    // 最小空闲连接数
//                    .setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
//                    // 连接池大小
//                    .setConnectionPoolSize(singleServerConfig.getConnectionPoolSize());
//            }
//            // 集群配置方式 参考下方注释
//            RedissonProperties.ClusterServersConfig clusterServersConfig = redissonProperties.getClusterServersConfig();
//            if (ObjectUtil.isNotNull(clusterServersConfig)) {
//                config.useClusterServers()
//                    //设置redis key前缀
//                    .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
//                    .setTimeout(clusterServersConfig.getTimeout())
//                    .setClientName(clusterServersConfig.getClientName())
//                    .setIdleConnectionTimeout(clusterServersConfig.getIdleConnectionTimeout())
//                    .setSubscriptionConnectionPoolSize(clusterServersConfig.getSubscriptionConnectionPoolSize())
//                    .setMasterConnectionMinimumIdleSize(clusterServersConfig.getMasterConnectionMinimumIdleSize())
//                    .setMasterConnectionPoolSize(clusterServersConfig.getMasterConnectionPoolSize())
//                    .setSlaveConnectionMinimumIdleSize(clusterServersConfig.getSlaveConnectionMinimumIdleSize())
//                    .setSlaveConnectionPoolSize(clusterServersConfig.getSlaveConnectionPoolSize())
//                    .setReadMode(clusterServersConfig.getReadMode())
//                    .setSubscriptionMode(clusterServersConfig.getSubscriptionMode());
//            }
//            log.info("初始化 redis 配置");
//        };
//    }
//
//    /**
//     * 异常处理器
//     */
//    @Bean
//    public RedisExceptionHandler redisExceptionHandler() {
//        return new RedisExceptionHandler();
//    }
//
//    /**
//     * redis集群配置 yml
//     *
//     * --- # redis 集群配置(单机与集群只能开启一个另一个需要注释掉)
//     * spring.data:
//     *   redis:
//     *     cluster:
//     *       nodes:
//     *         - 192.168.0.100:6379
//     *         - 192.168.0.101:6379
//     *         - 192.168.0.102:6379
//     *     # 密码
//     *     password:
//     *     # 连接超时时间
//     *     timeout: 10s
//     *     # 是否开启ssl
//     *     ssl.enabled: false
//     *
//     * redisson:
//     *   # 线程池数量
//     *   threads: 16
//     *   # Netty线程池数量
//     *   nettyThreads: 32
//     *   # 集群配置
//     *   clusterServersConfig:
//     *     # 客户端名称
//     *     clientName: ${ruoyi.name}
//     *     # master最小空闲连接数
//     *     masterConnectionMinimumIdleSize: 32
//     *     # master连接池大小
//     *     masterConnectionPoolSize: 64
//     *     # slave最小空闲连接数
//     *     slaveConnectionMinimumIdleSize: 32
//     *     # slave连接池大小
//     *     slaveConnectionPoolSize: 64
//     *     # 连接空闲超时，单位：毫秒
//     *     idleConnectionTimeout: 10000
//     *     # 命令等待超时，单位：毫秒
//     *     timeout: 3000
//     *     # 发布和订阅连接池大小
//     *     subscriptionConnectionPoolSize: 50
//     *     # 读取模式
//     *     readMode: "SLAVE"
//     *     # 订阅模式
//     *     subscriptionMode: "MASTER"
//     */
//
//}
