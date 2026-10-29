package com.xiaohucare.framework.aspectj;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.DataSource;

/**
 * 多数据源处理
 *
 * 说明：升级到 Spring Boot 3 + dynamic-datasource-spring-boot3-starter 后，
 * 切换数据源使用 baomidou 的 DynamicDataSourceContextHolder，
 * 通过数据源名称（master/slave）路由到 starter 自动装配的数据源。
 *
 * @author xiaohucare
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.xiaohucare.framework.annotation.DataSource)"
            + "|| @within(com.xiaohucare.framework.annotation.DataSource)")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isNotNull(dataSource))
        {
            // 数据源名称需与 application-druid.yml 中 spring.datasource.dynamic.datasource.* 保持一致
            // DataSourceType 枚举值 MASTER/SLAVE 转换为小写 master/slave
            DynamicDataSourceContextHolder.push(dataSource.value().name().toLowerCase());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.poll();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource))
        {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
