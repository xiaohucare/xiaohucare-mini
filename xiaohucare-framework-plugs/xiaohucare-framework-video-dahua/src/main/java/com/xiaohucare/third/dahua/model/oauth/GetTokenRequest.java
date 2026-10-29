package com.xiaohucare.third.dahua.model.oauth;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-07 16:25
 * @Description:
 */
@Data
public class GetTokenRequest {
    /**
     * 授权类型，固定值:password
     */
    private String grant_type = "password";
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 凭证id
     */
    private String client_id;
    /**
     * 凭证id
     */
    private String client_secret;
    /**
     * 公钥
     */
    private String public_key;
    /**
     * 动态验证码
     */
    private String code;
    /**
     * 是否启用动态验证码功能
     */
    private Integer verifyCodeFlag;

}
