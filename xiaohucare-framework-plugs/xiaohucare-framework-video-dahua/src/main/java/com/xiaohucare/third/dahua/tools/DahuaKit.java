package com.xiaohucare.third.dahua.tools;

import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.IccTokenResponse;
import com.xiaohucare.third.dahua.OauthApi;
import com.xiaohucare.third.dahua.api.video.realTimePreview.RealTimePreviewApi;
import com.xiaohucare.third.dahua.model.video.realTimePreview.HlsUrlRequest;
import com.xiaohucare.third.dahua.model.video.realTimePreview.HlsUrlResponse;

public class DahuaKit {

	/**
	 * 平台内网地址到对外播放地址的映射，由部署环境提供：
	 * 系统属性 dahua.rtsp.internal-host / dahua.rtsp.public-host，
	 * 或环境变量 DAHUA_RTSP_INTERNAL_HOST / DAHUA_RTSP_PUBLIC_HOST。
	 * 未配置时不做替换（直接使用平台返回的地址）。
	 */
	private static final String RTSP_INTERNAL_HOST = firstNonEmpty(
			System.getProperty("dahua.rtsp.internal-host"), System.getenv("DAHUA_RTSP_INTERNAL_HOST"));
	private static final String RTSP_PUBLIC_HOST = firstNonEmpty(
			System.getProperty("dahua.rtsp.public-host"), System.getenv("DAHUA_RTSP_PUBLIC_HOST"));

	private static String firstNonEmpty(String a, String b) {
		if (a != null && !a.isEmpty()) {
			return a;
		}
		return (b == null || b.isEmpty()) ? null : b;
	}

	public static String getShowUrlByCode(IccTokenResponse.IccToken iccToken,
			 RealTimePreviewApi realTimePreviewApi,
			String deviceCode) {
		if(deviceCode==null) {
			return null;
		}
		if(!deviceCode.contains("$")) {
			deviceCode=deviceCode+"$1$0$0";
		}
		HlsUrlRequest.Data hData = new HlsUrlRequest.Data();
		hData.setChannelId(deviceCode);
		hData.setStreamType("1");
		hData.setType("hls");
		HlsUrlRequest hlsUrlRequest=new HlsUrlRequest();
		hlsUrlRequest.setData(hData);
		HlsUrlResponse hlsUrlResponse=realTimePreviewApi.getHlsUrl(hlsUrlRequest);
		String rtspUrl=null;
		if(hlsUrlResponse!=null&&hlsUrlResponse.getData()!=null&&hlsUrlResponse.getData().getUrl()!=null
				&&iccToken!=null&&iccToken.getAccess_token()!=null) {
			try {
				rtspUrl = hlsUrlResponse.getData().getUrl() + "?token=" + iccToken.getAccess_token();
				// 内网地址无法被外部播放器访问时，按部署环境配置替换为对外地址
				if (RTSP_INTERNAL_HOST != null && RTSP_PUBLIC_HOST != null && rtspUrl.contains(RTSP_INTERNAL_HOST)) {
					rtspUrl = rtspUrl.replace(RTSP_INTERNAL_HOST, RTSP_PUBLIC_HOST);
				}
			} catch (Exception e) {
				// 地址拼接失败时保持返回 null，由调用方降级处理
			}
		}
		return rtspUrl;
	}
	public static String getShowUrlByCode(
			String deviceCode) {
		  OauthApi auApi=new OauthApi();
	  	  IccTokenResponse.IccToken iccToken=null;
	  	  RealTimePreviewApi realTimePreviewApi = new RealTimePreviewApi();
	  	  try {
				iccToken= auApi.getTokenInfo();
				return getShowUrlByCode(iccToken, realTimePreviewApi, deviceCode);
			} catch (ClientException e) {
			}
		   return null;
	}
}
