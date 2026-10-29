package com.xiaohucare.app.config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * Jackson ObjectMapper 配置
 * 支持多种日期格式解析
 */
@Configuration
public class JacksonConfig {

    // 支持的日期格式列表
    private static final String[] DATE_PATTERNS = {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/MM/dd"
    };

    // 统一时区：与 JDBC url 的 serverTimezone=GMT+8 保持一致，避免 Date 序列化/反序列化偏移一天
    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 创建自定义模块
        SimpleModule customModule = new SimpleModule("CustomDateModule");
        
        // LocalDateTime 序列化/反序列化
        customModule.addSerializer(LocalDateTime.class, 
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        customModule.addDeserializer(LocalDateTime.class, new StdDeserializer<LocalDateTime>(LocalDateTime.class) {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                try {
                    if (value.contains("T")) {
                        return LocalDateTime.parse(value);
                    }
                    for (String pattern : DATE_PATTERNS) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            sdf.setTimeZone(DEFAULT_TIME_ZONE);
                            Date date = sdf.parse(value);
                            return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        } catch (ParseException ignored) {}
                    }
                } catch (Exception ignored) {}
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });
        
        // LocalDate 序列化/反序列化
        customModule.addSerializer(LocalDate.class, 
            new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        customModule.addDeserializer(LocalDate.class, new StdDeserializer<LocalDate>(LocalDate.class) {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                try {
                    if (value.contains("T")) {
                        return LocalDate.parse(value.substring(0, 10));
                    }
                    for (String pattern : DATE_PATTERNS) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            sdf.setTimeZone(DEFAULT_TIME_ZONE);
                            Date date = sdf.parse(value);
                            return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        } catch (ParseException ignored) {}
                    }
                } catch (Exception ignored) {}
                return LocalDate.parse(value.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });
        
        // LocalTime 序列化/反序列化
        customModule.addSerializer(LocalTime.class, 
            new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        customModule.addDeserializer(LocalTime.class, new StdDeserializer<LocalTime>(LocalTime.class) {
            @Override
            public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                try {
                    if (value.contains("T")) {
                        return LocalTime.parse(value.substring(11, 19));
                    }
                    for (String pattern : DATE_PATTERNS) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            sdf.setTimeZone(DEFAULT_TIME_ZONE);
                            Date date = sdf.parse(value);
                            return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
                        } catch (ParseException ignored) {}
                    }
                } catch (Exception ignored) {}
                return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        });
        
        // Date 反序列化 - 支持多种格式
        customModule.addDeserializer(Date.class, new StdDeserializer<Date>(Date.class) {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                for (String pattern : DATE_PATTERNS) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        sdf.setLenient(false);
                        sdf.setTimeZone(DEFAULT_TIME_ZONE);
                        return sdf.parse(value);
                    } catch (ParseException ignored) {}
                }
                // 默认尝试
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(DEFAULT_TIME_ZONE);
                    return sdf.parse(value);
                } catch (ParseException e) {
                    throw new IOException("Unable to parse date: " + value);
                }
            }
        });
        
        // 注册模块
        objectMapper.registerModule(customModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 统一时区：@JsonFormat 默认按 UTC 序列化 Date，会导致日期字段比数据库少一天
        objectMapper.setTimeZone(DEFAULT_TIME_ZONE);
        // 忽略未知字段（前端可能提交多余字段，避免 UnrecognizedPropertyException）
        // 注意：该 Bean 存在时，Spring Boot 的 spring.jackson.* 配置不会生效
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        return objectMapper;
    }
}
