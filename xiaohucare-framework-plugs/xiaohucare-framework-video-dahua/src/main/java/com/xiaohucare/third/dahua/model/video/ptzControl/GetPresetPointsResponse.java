package com.xiaohucare.third.dahua.model.video.ptzControl;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-10 09:34
 * @Description: 获取云台预置点接口返回
 */
@Data
public class GetPresetPointsResponse extends IccResponse {
    private String desc;
    private Data data;
    @lombok.Data
    public static class Data{
        private String channelId;
        private List<PresetPoint> presetPoints;
        @lombok.Data
        public static class PresetPoint{
            private Integer id;
            private String channelId;
            private String presetPointCode;
            private String presetPointName;
        }
    }
}
