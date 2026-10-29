package com.xiaohucare.third.dahua.config;

/**
 * 平台信息配置
 *
 * <p>凭证与地址不内置任何真实值，统一从系统属性（优先）或环境变量读取：
 * <ul>
 * <li>dahua.client-id / DAHUA_CLIENT_ID</li>
 * <li>dahua.client-secret / DAHUA_CLIENT_SECRET</li>
 * <li>dahua.username / DAHUA_USERNAME</li>
 * <li>dahua.password / DAHUA_PASSWORD</li>
 * <li>dahua.host / DAHUA_HOST（大华 ICC 平台地址）</li>
 * <li>dahua.https-port / DAHUA_HTTPS_PORT，dahua.http-port / DAHUA_HTTP_PORT</li>
 * <li>dahua.subscribe.host / DAHUA_SUBSCRIBE_HOST，dahua.subscribe.port / DAHUA_SUBSCRIBE_PORT
 * （事件订阅回调地址，默认 127.0.0.1:8003）</li>
 * </ul>
 */
public class PlatformConfig {
    /** 凭证ID自定义，配合申请凭证流程申请 */
    private String clientId = readConfig("dahua.client-id", "DAHUA_CLIENT_ID", "");
    /**
     * 凭证密钥，申请方式参考
     * https://open-icc.dahuatech.com/iccdoc/enterprisebase/5.0.15/wiki/common/quickstart.html
     */
    private String clientSecret = readConfig("dahua.client-secret", "DAHUA_CLIENT_SECRET", "");
    /** 平台登录用户名 */
    private String username = readConfig("dahua.username", "DAHUA_USERNAME", "");
    /** 平台登录密码 */
    private String password = readConfig("dahua.password", "DAHUA_PASSWORD", "");
    /** 大华 ICC 平台地址（内网/公网地址均由部署环境提供） */
    private String host = readConfig("dahua.host", "DAHUA_HOST", "");
    /** https 默认端口 443 */
    private String httpsPort = readConfig("dahua.https-port", "DAHUA_HTTPS_PORT", "443");
    /** http 默认端口 80（需运维中心开启 http 调试模式后才支持） */
    private String httpPort = readConfig("dahua.http-port", "DAHUA_HTTP_PORT", "80");
    /** 连接超时 */
    private Long connectionTimeout = -1l;
    /** 读取超时 */
    private Long readTimeout = -1l;

    /**
     * 优先读系统属性，其次读环境变量，最后回退默认值。
     */
    private static String readConfig(String sysPropKey, String envKey, String defaultValue) {
        String value = System.getProperty(sysPropKey);
        if (value == null || value.isEmpty()) {
            value = System.getenv(envKey);
        }
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    /**
     * 事件订阅回调主机（本服务对大华平台可达的地址），由部署环境提供，不再内置任何真实内网 IP。
     */
    public static String subscribeHost() {
        return readConfig("dahua.subscribe.host", "DAHUA_SUBSCRIBE_HOST", "127.0.0.1");
    }

    /**
     * 事件订阅回调端口，默认 8003。
     */
    public static String subscribePort() {
        return readConfig("dahua.subscribe.port", "DAHUA_SUBSCRIBE_PORT", "8003");
    }

    /**
     * 订阅方标识（大华侧要求 host_port 形式）。
     */
    public static String subscribeName() {
        return subscribeHost() + "_" + subscribePort();
    }

    /**
     * 事件回调完整地址。
     */
    public static String subscribeUrl() {
        return "http://" + subscribeHost() + ":" + subscribePort() + "/receiveMsg";
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(String httpsPort) {
        this.httpsPort = httpsPort;
    }

    public String getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Long readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * 校验必填凭证是否已配置，未配置时给出明确提示，避免带着空凭证去请求平台。
     */
    public void requireConfigured() {
        if (host == null || host.isEmpty()) {
            throw new IllegalStateException("大华 ICC 平台地址未配置，请设置系统属性 dahua.host 或环境变量 DAHUA_HOST");
        }
        if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
            throw new IllegalStateException("大华 ICC 开放平台凭证未配置，请设置 dahua.client-id/dahua.client-secret 或对应环境变量");
        }
    }
}
