package com.xiaohucare.third.dahua.config;

import com.dahuatech.icc.oauth.model.v202010.OauthConfigUserPwdInfo;

public class OauthConfigUtil {
    /**
     * 获取Oauth配置信息
     *
     * @return Oauth配置信息
     */
    public static OauthConfigUserPwdInfo getOauthConfig() {
        PlatformConfig platformConfig = new PlatformConfig();//读取配置
        OauthConfigUserPwdInfo oauthConfigUserPwdInfo = new OauthConfigUserPwdInfo(
                platformConfig.getHost(),
                platformConfig.getClientId(),
                platformConfig.getClientSecret(),
                platformConfig.getUsername(),
                platformConfig.getPassword(),
                false,
                platformConfig.getHttpsPort(),
                platformConfig.getHttpPort()
        );
        oauthConfigUserPwdInfo.getHttpConfigInfo().setReadTimeout(platformConfig.getReadTimeout());
        oauthConfigUserPwdInfo.getHttpConfigInfo().setConnectionTimeout(platformConfig.getConnectionTimeout());
        return oauthConfigUserPwdInfo;
    }
}
