package com.xiaohucare.third.dahua.model.video.ptzControl;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-10 12:00
 * @Description:
 */
@Data
public class GetCruisesResponse extends IccResponse {
    private String desc;
    private Data data;
    @lombok.Data
    public static class Data{
        /** 通道编码*/
        private String channelId;
        /** 巡航线信息*/
        private List<Cruise> cruises;
        @lombok.Data
        public static class Cruise{
            /** 通道编码*/
            private String channelId;
            /** 巡航线编码*/
            private String cruiseCode;
            /** 巡航线名称*/
            private String cruiseName;
            private Long pointId;
            private List<Point> points;
            @lombok.Data
            public static class Point{
                private Integer id;
                private String code;
                private String stay;
            }
        }
    }
}
