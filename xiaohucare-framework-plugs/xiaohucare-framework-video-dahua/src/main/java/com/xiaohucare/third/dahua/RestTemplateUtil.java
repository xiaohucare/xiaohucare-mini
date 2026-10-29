package com.xiaohucare.third.dahua;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-07 15:40
 * @Description:
 */
@Slf4j
public class RestTemplateUtil {

    /**
     * 跳过证书效验的sslcontext
     * @return
     * @throws Exception
     */
    private static SSLContext createIgnoreVerifySSL() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (Exception e) {
            log.info("RestTemplateUtil,createIgnoreVerifySSL,exception:{}",e.getMessage());
            e.printStackTrace();
        }
        return sc;
    }

    /**
     * 构造RestTemplate
     * @return
     */
    public static RestTemplate getRestTemplate() {
        // 使用 SimpleClientHttpRequestFactory（基于 JDK 原生 HttpURLConnection）
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 超时设置
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        
        // 配置 HTTPS 跳过证书验证
        try {
            SSLContext sslContext = createIgnoreVerifySSL();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            log.warn("RestTemplateUtil, 配置HTTPS跳过证书验证失败: {}", e.getMessage());
        }
        
        RestTemplate restTemplate = new RestTemplate(factory);
        // 解决中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
