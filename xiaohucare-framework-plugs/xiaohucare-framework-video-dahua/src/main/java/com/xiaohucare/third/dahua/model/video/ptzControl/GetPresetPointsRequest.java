package com.xiaohucare.third.dahua.model.video.ptzControl;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-10 10:02
 * @Description: 获取云台预置点接口请求参数
 */
@Data
public class GetPresetPointsRequest {
    private Data data;

    @lombok.Data
    public static class Data{
        private String channelId;
    }
}
