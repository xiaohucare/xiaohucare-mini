package com.xiaohucare.third.dahua.api.video.realTimePreview;

import org.springframework.util.StringUtils;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.model.v202010.OauthConfigUserPwdInfo;
import com.dahuatech.icc.oauth.utils.HttpUtils;
import com.xiaohucare.third.dahua.config.OauthConfigUtil;
import com.xiaohucare.third.dahua.model.video.realTimePreview.HlsUrlRequest;
import com.xiaohucare.third.dahua.model.video.realTimePreview.HlsUrlResponse;
import com.xiaohucare.third.dahua.model.video.realTimePreview.RtspUrlRequest;
import com.xiaohucare.third.dahua.model.video.realTimePreview.RtspUrlResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-02 09:35
 * @Description: 实时预览
 */
@Slf4j
public class RealTimePreviewApi {
    /**
     * 获取RTSP流地址
     * @param rtspUrlRequest
     * @return
     */
    public RtspUrlResponse getRtspUrl(RtspUrlRequest rtspUrlRequest){
        RtspUrlResponse response=null;
        OauthConfigUserPwdInfo config = OauthConfigUtil.getOauthConfig();
        try {
            log.info("RealTimePreviewDemo,getRtspUrl,request:{}", JSONUtil.toJsonStr(rtspUrlRequest));
            response = HttpUtils.executeJson("/evo-apigw/admin/API/MTS/Video/StartVideo", rtspUrlRequest,null, Method.POST , config, RtspUrlResponse.class);
            log.info("RealTimePreviewDemo,getRtspUrl,response:{}", JSONUtil.toJsonStr(response));
        } catch (ClientException e) {
            log.error(e.getErrMsg(), e);
        }
        if(!response.getCode().equals("1000")) {
            log.info("获取rtsp流地址失败:{}",response.getErrMsg());
        }
        return response;
    }

    /**
     * 获取HLS、FLV、RTMP流地址
     */
    public HlsUrlResponse getHlsUrl(HlsUrlRequest hlsUrlRequest){
        HlsUrlResponse response=null;
        OauthConfigUserPwdInfo config = OauthConfigUtil.getOauthConfig();
        try {
            log.info("RealTimePreviewDemo,getHlsUrl,request:{}", JSONUtil.toJsonStr(hlsUrlRequest));
            response = HttpUtils.executeJson("/evo-apigw/admin/API/video/stream/realtime", hlsUrlRequest,null, Method.POST , config, HlsUrlResponse.class);
            log.info("RealTimePreviewDemo,getHlsUrl,response:{}", JSONUtil.toJsonStr(response));
        } catch (ClientException e) {
            log.error(e.getErrMsg(), e);
        }
        if(!response.getCode().equals("1000")) {
            log.info("获取流地址失败:{}",response.getErrMsg());
        }
        return response;
    }

    /**
     * 获取hls拼接地址
     * @param protocol 协议类型，http、https
     * @param ip 平台ip
     * @param port hls流端口
     * @param isCascade 是否是级联设备
     * @param channelId 通道id
     * @param streamType 码流类型
     * @param token 认证接口返回的access_token
     * @return
     */
    public String getJointHlsUrl(String protocol,String ip,String port,Boolean isCascade,String channelId,String streamType,String token){
        StringBuilder hlsUrl = new StringBuilder();
        String deviceCode = channelId.substring(0,channelId.indexOf("$"));
        if(isCascade){
            deviceCode.replace("@","%40");
        }
        String channelSeq = channelId.substring(channelId.lastIndexOf("$")+1);
        hlsUrl.append(protocol+"://"+ip+":"+port+"/live/cameraid/"+deviceCode+"%24"+channelSeq+"/substream/"+streamType+".m3u8");
        if(!StringUtils.isEmpty(token)){
            hlsUrl.append("?token="+token);
        }
        log.info("拼接的hls流地址:{}",hlsUrl);
        return hlsUrl.toString();
    }

    /**
     * 获取flv拼接地址
     * @param protocol 协议类型，http、https、ws、wss
     * @param ip 平台ip
     * @param port flv流端口
     * @param isCascade 是否是级联设备
     * @param channelId 通道id
     * @param streamType 码流类型：1-主码流，2-辅码流
     * @param token 认证接口返回的access_token
     * @return
     */
    public String getJointFlvUrl(String protocol,String ip,String port,Boolean isCascade,String channelId, String streamType,String token){
        StringBuilder flvUrl = new StringBuilder();
        String deviceCode = channelId.substring(0,channelId.indexOf("$"));
        if(isCascade){
            deviceCode.replace("@","%40");
        }
        String channelSeq = channelId.substring(channelId.lastIndexOf("$")+1);
        flvUrl.append(protocol+"://"+ip+":"+port+"/live/cameraid/"+deviceCode+"%24"+channelSeq+"/substream/"+streamType+".flv");
        if(!StringUtils.isEmpty(token)){
            flvUrl.append("?token="+token);
        }
        log.info("拼接的flv流地址:{}",flvUrl);
        return flvUrl.toString();
    }

    /**
     * 获取rtmp拼接地址
     * @param ip 平台ip
     * @param port flv流端口
     * @param isCascade 是否是级联设备
     * @param channelId 通道id
     * @param streamType 码流类型
     * @param token 认证接口返回的access_token
     * @return
     */
    public String getJointRtmpUrl(String ip,String port,Boolean isCascade, String channelId,String streamType,String token){
        StringBuilder rtmpUrl = new StringBuilder();
        String deviceCode = channelId.substring(0,channelId.indexOf("$"));
        if(isCascade){
            deviceCode = deviceCode.replace("@","%40");
        }
        String channelSeq = channelId.substring(channelId.lastIndexOf("$")+1);
        rtmpUrl.append("rtmp://"+ip+":"+port+"/live/cameraid="+deviceCode+"%24"+channelSeq+";substream="+streamType);
        if(!StringUtils.isEmpty(token)){
            rtmpUrl.append(";token="+token);
        }
        log.info("拼接的rtmp流地址:{}",rtmpUrl);
        return rtmpUrl.toString();
    }
}
