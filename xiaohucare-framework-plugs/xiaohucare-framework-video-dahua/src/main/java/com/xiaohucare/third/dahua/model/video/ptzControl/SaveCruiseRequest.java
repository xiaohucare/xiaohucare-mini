package com.xiaohucare.third.dahua.model.video.ptzControl;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-09 15:09
 * @Description: 保存巡航线请求参数
 */
@Data
public class SaveCruiseRequest {
    private Data data;

    @lombok.Data
    public static  class Data{
        /** 通道编码 */
        private String channelId;
        /** 巡航线数据 */
        private List<CruiseInfo> cruises;
        /** 巡航线XML数据 */
        private String data;
    }
}
