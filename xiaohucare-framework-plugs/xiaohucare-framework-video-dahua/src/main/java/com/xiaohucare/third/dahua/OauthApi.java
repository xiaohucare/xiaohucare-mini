package com.xiaohucare.third.dahua;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.handle.TokenHandlerProcessor;
import com.dahuatech.icc.oauth.http.IccTokenResponse;
import com.dahuatech.icc.oauth.model.v202010.OauthConfigUserPwdInfo;
import com.dahuatech.icc.oauth.utils.HttpUtils;
import com.xiaohucare.third.dahua.config.OauthConfigUtil;
import com.xiaohucare.third.dahua.config.PlatformConfig;
import com.xiaohucare.third.dahua.model.oauth.GetTokenRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-07 13:57
 * @Description: 鉴权
 */
@Slf4j
@Data
public class OauthApi {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private boolean isHttp;

    public OauthApi() {
        loadSdkProperties();
    }

    /**
     * 从配置文件iccSdk.Properties里面读取配置信息
     */
    public void loadSdkProperties(){
    	 PlatformConfig platformConfig = new PlatformConfig();//从系统属性/环境变量读取配置
    	 platformConfig.requireConfigured();
    	 host =platformConfig.getHost();
         port =Integer.valueOf(platformConfig.getHttpPort());
         username =platformConfig.getUsername();
         password =platformConfig.getPassword();
         clientId = platformConfig.getClientId();
         clientSecret =platformConfig.getClientSecret();
         isHttp =true;
    }

    /**
     * 获取公钥(用于调认证接口时对密码进行RSA加密)
     * 原生代码调用，不使用sdk
     * @return 公钥
     * @throws Exception
     */
    public String getPublicKey() {
        String url;
        if(!isHttp){
            port = port!=null?port:443;
            url="https://"+host+":"+port;
        }else{
            port = port!=null?port:83;
            url="http://"+host+":"+port;
        }
        url+="/evo-apigw/evo-oauth/1.0.0/oauth/public-key";
        log.info("OauthDemo,getPublicKey,url:{}",url);
        RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("OauthDemo,getPublicKey,response:{}",response.getBody());
        Map data = (Map) com.alibaba.fastjson.JSON.parseObject(response.getBody(), new TypeReference<Map<String, Object>>(){}).get("data");
        String publicKey = data.get("publicKey").toString();
        log.debug("OauthDemo,getPublicKey,publicKey loaded");
        return publicKey;
    }

    /**
     * 认证(获取token)
     * 原生代码调用，不使用sdk
     * @return
     */
    public String getToken(){
        String token=null;
        try {
            String url;
            if(!isHttp){
                port = port!=null?port:443;
                url="https://"+host+":"+port;
            }else{
                port = port!=null?port:83;
                url="http://"+host+":"+port;
            }
            url += "/evo-apigw/evo-oauth/1.0.0/oauth/extend/token";
            GetTokenRequest getTokenRequest = new GetTokenRequest();
            String publicKey = getPublicKey();
            getTokenRequest.setGrant_type("password");
            getTokenRequest.setUsername(username);
            getTokenRequest.setPassword(RSAUtil.encrypt(password, publicKey));
            getTokenRequest.setClient_id(clientId);
            getTokenRequest.setClient_secret(clientSecret);
            getTokenRequest.setPublic_key(publicKey);
            log.info("OauthDemo,getToken,url:{}", url);
            // 注意：请求体包含 client_secret 与加密后的密码，响应体包含 access_token，
            // 均不得写入日志，仅记录调用结果。
            RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, getTokenRequest, String.class);
            log.info("OauthDemo,getToken,httpStatus:{}", response.getStatusCode());
            Map data = (Map) com.alibaba.fastjson.JSON.parseObject(response.getBody(), new TypeReference<Map<String, Object>>(){}).get("data");
            token = data.get("access_token").toString();
            String tokenType = data.get("token_type").toString();
            token = tokenType + ' ' + token;
            log.info("OauthDemo,getToken,token generated");
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return token;
    }

    /**
     * 通过sdk获取token
     * @param host 平台ip
     * @param clientId
     * @param clientSecret
     * @param username
     * @param password
     * @param isHttp
     * @param port
     * @return
     * @throws ClientException
     */
    public IccTokenResponse.IccToken getTokenInfo(String host,String clientId,String clientSecret,String username,String password,boolean isHttp,String port) throws ClientException {
        OauthConfigUserPwdInfo oauthConfigUserPwdInfo = OauthConfigUtil.getOauthConfig();
        IccTokenResponse.IccToken tokenInfo =TokenHandlerProcessor.getInstance().oauth(oauthConfigUserPwdInfo);
        return tokenInfo;
    }

    /**
     * 获取token信息
     *
     * @return token信息
     * @throws ClientException 客户端异常
     */
    public IccTokenResponse.IccToken getTokenInfo() throws ClientException {
        return HttpUtils.getToken(OauthConfigUtil.getOauthConfig());
    }
}
