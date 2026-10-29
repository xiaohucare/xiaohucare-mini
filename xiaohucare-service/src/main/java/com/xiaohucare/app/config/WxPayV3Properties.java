package com.xiaohucare.app.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.xiaohucare.common.utils.StringUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付 v3 配置，对应 application.yml 中 wx.pay 前缀。
 * <p>
 * 支持多商户（多 appId）：wx.pay.apps 可配置多套商户参数，按 appId 路由；
 * 顶层字段作为默认配置，appId 为空或未命中 apps 时兜底使用。
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayV3Properties {

	/** 默认商户 appId */
	private String appId;
	/** 默认商户号 */
	private String mchId;
	/** 商户 APIv2 密钥 */
	private String mchKey;
	/** 商户 APIv3 密钥 */
	private String apiV3Key;
	/** apiclient_cert.p12 路径 */
	private String keyPath;
	/** apiclient_cert.pem 路径 */
	private String privateCertPath;
	/** apiclient_key.pem 路径 */
	private String privateKeyPath;
	/** pub_key.pem（微信支付平台公钥）路径 */
	private String publicKeyPath;
	/** 微信支付平台公钥ID */
	private String publicKeyId;
	/** 商户 API 证书序列号 */
	private String certSerialNo;
	/** 异步通知地址（必须 https） */
	private String notifyUrl;

	/** 多商户配置（按 appId 路由），未配置时使用顶层默认字段 */
	private List<WxPayAppConfig> apps;

	/**
	 * 缓存的 WxPayService（按 appId 隔离，首次调用懒加载验签器）。
	 * 排除 Lombok 生成 getter/setter，避免与下面的自定义方法冲突。
	 */
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private final Map<String, WxPayService> serviceCache = new ConcurrentHashMap<>();

	/**
	 * 获取指定 appId 对应的 WxPayService；appId 为空或未配置时使用默认配置。
	 */
	public WxPayService getWxPayService(String appId) {
		return serviceCache.computeIfAbsent(normalizeAppId(appId), this::buildWxPayService);
	}

	/** 空 appId 归一化为默认 appId，避免 ConcurrentHashMap 的 null key 限制 */
	private String normalizeAppId(String appId) {
		if (StringUtils.isEmpty(appId)) {
			return StringUtils.isEmpty(this.appId) ? "default" : this.appId;
		}
		return appId;
	}

	/** 按 appId 构建 WxPayService；appId 未命中 apps 时用默认配置 */
	private WxPayService buildWxPayService(String key) {
		WxPayAppConfig app = getAppConfig(key);
		WxPayConfig config = new WxPayConfig();
		config.setAppId(app.getAppId());
		config.setMchId(app.getMchId());
		config.setMchKey(app.getMchKey());
		config.setNotifyUrl(app.getNotifyUrl());
		config.setApiV3Key(app.getApiV3Key());
		config.setPrivateCertPath(app.getPrivateCertPath());
		config.setPrivateKeyPath(app.getPrivateKeyPath());
		config.setPublicKeyPath(app.getPublicKeyPath());
		config.setCertSerialNo(app.getCertSerialNo());
		config.setKeyPath(app.getKeyPath());
		config.setPublicKeyId(app.getPublicKeyId());
		WxPayService service = new WxPayServiceImpl();
		service.setConfig(config);
		return service;
	}

	/**
	 * 获取指定 appId 的商户配置；appId 未命中 apps 时返回默认配置（顶层字段）。
	 */
	public WxPayAppConfig getAppConfig(String appId) {
		if (!StringUtils.isEmpty(appId) && apps != null) {
			for (WxPayAppConfig app : apps) {
				if (appId.equals(app.getAppId())) {
					return app;
				}
			}
		}
		return toDefaultConfig(appId);
	}

	/** 默认配置（顶层字段）组装 */
	private WxPayAppConfig toDefaultConfig(String appId) {
		WxPayAppConfig app = new WxPayAppConfig();
		app.setAppId(StringUtils.isEmpty(appId) || "default".equals(appId) ? this.appId : appId);
		app.setMchId(mchId);
		app.setMchKey(mchKey);
		app.setApiV3Key(apiV3Key);
		app.setKeyPath(keyPath);
		app.setPrivateCertPath(privateCertPath);
		app.setPrivateKeyPath(privateKeyPath);
		app.setPublicKeyPath(publicKeyPath);
		app.setPublicKeyId(publicKeyId);
		app.setCertSerialNo(certSerialNo);
		app.setNotifyUrl(notifyUrl);
		return app;
	}

	/** 全部已配置的 appId（默认 + apps，去重） */
	public List<String> getAppIds() {
		List<String> ids = new ArrayList<>();
		if (!StringUtils.isEmpty(appId) && !ids.contains(appId)) {
			ids.add(appId);
		}
		if (apps != null) {
			for (WxPayAppConfig app : apps) {
				if (!StringUtils.isEmpty(app.getAppId()) && !ids.contains(app.getAppId())) {
					ids.add(app.getAppId());
				}
			}
		}
		return ids;
	}

	/** 指定 appId 的商户号（未配置时用默认） */
	public String getMchId(String appId) {
		return getAppConfig(appId).getMchId();
	}

	/** 指定 appId 的 APIv2 密钥（未配置时用默认） */
	public String getMchKey(String appId) {
		return getAppConfig(appId).getMchKey();
	}

	/** 指定 appId 的 APIv3 密钥（未配置时用默认） */
	public String getApiV3Key(String appId) {
		return getAppConfig(appId).getApiV3Key();
	}

	/** 指定 appId 的通知地址（未配置时用默认） */
	public String getNotifyUrl(String appId) {
		return getAppConfig(appId).getNotifyUrl();
	}

	/** 多商户配置项（与顶层字段一一对应） */
	@Data
	public static class WxPayAppConfig {
		/** 商户 appId */
		private String appId;
		/** 商户号 */
		private String mchId;
		/** 商户 APIv2 密钥 */
		private String mchKey;
		/** 商户 APIv3 密钥 */
		private String apiV3Key;
		/** apiclient_cert.p12 路径 */
		private String keyPath;
		/** apiclient_cert.pem 路径 */
		private String privateCertPath;
		/** apiclient_key.pem 路径 */
		private String privateKeyPath;
		/** pub_key.pem（微信支付平台公钥）路径 */
		private String publicKeyPath;
		/** 微信支付平台公钥ID */
		private String publicKeyId;
		/** 商户 API 证书序列号 */
		private String certSerialNo;
		/** 异步通知地址（必须 https） */
		private String notifyUrl;
	}
}
