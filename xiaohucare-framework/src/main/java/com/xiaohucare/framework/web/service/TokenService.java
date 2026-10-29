package com.xiaohucare.framework.web.service;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xiaohucare.common.constant.CacheConstants;
import com.xiaohucare.common.constant.Constants;
import com.xiaohucare.common.core.redis.RedisUtil;
import com.xiaohucare.common.utils.ServletUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.common.utils.ip.IPOfflineUtil;
import com.xiaohucare.common.utils.ip.IPOnlineUtil;
import com.xiaohucare.common.utils.ip.IpCityVo;
import com.xiaohucare.common.utils.ip.IpUtils;
import com.xiaohucare.common.utils.uuid.IdUtils;
import com.xiaohucare.framework.core.domain.model.LoginUser;

import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * token验证处理
 * 
 * @author xiaohucare
 */
@Component
public class TokenService {
	private static final Logger log = LoggerFactory.getLogger(TokenService.class);

	// 令牌自定义标识
	@Value("${token.header:Authorization}")
	private String header;

	// 令牌秘钥（无默认值：必须由配置/环境变量提供，避免公开仓库泄露后可伪造 token）
	@Value("${token.secret}")
	private String secret;

	// 令牌有效期（默认30分钟）
	@Value("${token.expireTime:30}")
	private int expireTime;

	/** HMAC-SHA256 密钥最小字节长度 */
	private static final int MIN_SECRET_BYTES = 32;

	/**
	 * 启动时校验令牌密钥，避免使用弱密钥/默认密钥上线。
	 */
	@PostConstruct
	public void validateTokenSecret() {
		if (secret == null || secret.trim().isEmpty()) {
			throw new IllegalStateException(
					"token.secret 未配置，请通过环境变量 XIAOHUCARE_TOKEN_SECRET 注入令牌密钥（生成示例：openssl rand -base64 48）");
		}
		if (secret.getBytes(java.nio.charset.StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
			throw new IllegalStateException("token.secret 长度没有满足 HMAC-SHA256 要求（至少 32 字节），请重新生成足够长的密钥");
		}
	}

	protected static final long MILLIS_SECOND = 1000;

	protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

	private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

//    @Autowired
//    private RedisCache redisCache;
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 获取用户身份信息
	 * 
	 * @return 用户信息
	 */
	public LoginUser getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String token = getToken(request);
		if (StringUtils.isNotEmpty(token)) {
			try {
				Claims claims = parseToken(token);
				// 解析对应的权限以及用户信息
				String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
				String userKey = getTokenKey(uuid);
				LoginUser user = null;// new LoginUser();
				Object userJson = redisUtil.get(userKey);
				if (userJson != null) {
					log.debug("userJson---:" + userJson);
					user = JSONUtil.toBean(userJson.toString(), LoginUser.class);
				}
				log.debug("user---:" + JSONUtil.toJsonStr(user));
//                LoginUser user = redisCache.getCacheObject(userKey);
				return user;
			} catch (Exception e) {
				log.error("获取用户信息异常'{}'", e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 设置用户身份信息
	 */
	public void setLoginUser(LoginUser loginUser) {
		if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 删除用户身份信息
	 */
	public void delLoginUser(String token) {
		if (StringUtils.isNotEmpty(token)) {
			String userKey = getTokenKey(token);
			redisUtil.del(userKey);
		}
	}

	/**
	 * 创建令牌
	 * 
	 * @param loginUser 用户信息
	 * @return 令牌
	 */
	public String createToken(LoginUser loginUser) {
		String token = IdUtils.fastUUID();
		loginUser.setToken(token);
		setUserAgent(loginUser);
		refreshToken(loginUser);

		Map<String, Object> claims = new HashMap<>();
		claims.put(Constants.LOGIN_USER_KEY, token);
		claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
		return createToken(claims);
	}

	/**
	 * 验证令牌有效期，相差不足20分钟，自动刷新缓存
	 * 
	 * @param loginUser 登录信息
	 * @return 令牌
	 */
	public void verifyToken(LoginUser loginUser) {
		long expireTime = loginUser.getExpireTime();
		long currentTime = System.currentTimeMillis();
		if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 刷新令牌有效期
	 * 
	 * @param loginUser 登录信息
	 */
//	public void refreshToken(LoginUser loginUser) {
//		loginUser.setLoginTime(System.currentTimeMillis());
//		loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
//		// 根据uuid将loginUser缓存
//		String userKey = getTokenKey(loginUser.getToken());
//		redisUtil.set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
//	}
	  /**
     * 刷新令牌有效期
     * 
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        log.debug("loginUser---:"+JSONUtil.toJsonStr(loginUser));
        redisUtil.set(userKey, JSONUtil.toJsonStr(loginUser), expireTime*60*60);
//      redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
	/**
	 * 设置用户代理信息
	 * 
	 * @param loginUser 登录信息
	 */
	public void setUserAgent(LoginUser loginUser) {
		UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		String ip = IpUtils.getIpAddr();
		loginUser.setIpaddr(ip);
		 String address=null;
//       String address = AddressUtils.getRealAddressByIP(ip);
	       IpCityVo ipCityVo=IPOfflineUtil.getRegion(ip);
	       if(ipCityVo==null||(ipCityVo.getStatus()!=null&&ipCityVo.getStatus()!=0)) {
	    	   ipCityVo=IPOnlineUtil.getNetIPAPIByIP(ip);
          }
	       address=ipCityVo.getTips();
	       log.debug("address---:"+JSONUtil.toJsonStr(ipCityVo));
		loginUser.setLoginLocation(address);
		loginUser.setBrowser(userAgent.getBrowser().getName());
		loginUser.setOs(userAgent.getOperatingSystem().getName());
	}

	/**
	 * 从数据声明生成令牌
	 *
	 * @param claims 数据声明
	 * @return 令牌
	 */
	private String createToken(Map<String, Object> claims) {
		// 使用新的 JJWT API
		String token = Jwts.builder()
				.claims(claims)
				.signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
				.compact();
		return token;
	}

	/**
	 * 从令牌中获取数据声明
	 *
	 * @param token 令牌
	 * @return 数据声明
	 */
	private Claims parseToken(String token) {
		// 使用新的 JJWT API
		return Jwts.parser()
				.verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * 从令牌中获取用户名
	 *
	 * @param token 令牌
	 * @return 用户名
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.getSubject();
	}

	/**
	 * 获取请求token
	 *
	 * @param request
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(header);
		if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
			token = token.replace(Constants.TOKEN_PREFIX, "");
		}
		return token;
	}

	private String getTokenKey(String uuid) {
		return CacheConstants.LOGIN_TOKEN_KEY + uuid;
	}
}