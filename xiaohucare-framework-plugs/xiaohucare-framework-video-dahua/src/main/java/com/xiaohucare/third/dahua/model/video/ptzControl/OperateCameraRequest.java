package com.xiaohucare.third.dahua.model.video.ptzControl;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-09 15:58
 * @Description: 云台镜头控制请求参数
 */
@Data
public class OperateCameraRequest {
    private Data data;
    @lombok.Data
    public static class Data{
        private String channelId;
        private String operateType;
        private String direct;
        private String step;
        private String command;
        private String extend;
    }
}
