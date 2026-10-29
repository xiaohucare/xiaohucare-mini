package com.xiaohucare.third.dahua.model.video.ptzControl;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-09 15:58
 * @Description: 云台方向控制请求参数
 */
@Data
public class OperateFunctionRequest {
    private Data data;
    @lombok.Data
    public static class Data{
        private String channelId;
        private String operateType;
        private String target;
        private String command;
        private String extend;
    }
}
