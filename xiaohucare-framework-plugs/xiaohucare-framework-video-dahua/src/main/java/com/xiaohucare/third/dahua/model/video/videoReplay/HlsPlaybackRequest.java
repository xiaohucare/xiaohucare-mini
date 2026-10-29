package com.xiaohucare.third.dahua.model.video.videoReplay;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-07 15:28
 * @Description: HLS、RTMP录像回放（FLV不支持）接口请求参数
 */
@Data
public class HlsPlaybackRequest {
    private Data data;
    @lombok.Data
    public static class Data{
        private String channelId;
        private String streamType;
        private String type;
        private String recordType;
        private String beginTime;
        private String endTime;
        private String recordSource;
    }
}
