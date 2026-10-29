package com.xiaohucare.third.dahua.model.video.videoReplay;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-07 13:48
 * @Description: rtsp以时间形式回放录像返回结果
 */
@Data
public class PlayBackByTimeResponse extends IccResponse {

    private Data data;
    @lombok.Data
    public static class Data{
        private String url;
        private String session;
        private String protocol;
        private String token;
        private String fromDevice;
    }

}
