package com.xiaohucare.third.dahua.model.event.eventSubcribe;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-08 09:57
 * @Description:
 */
@Data
public class SubscribeRequest {
    private Param param;
    @Data
    public static class Param{
        private List<Monitor> monitors;
        private Subsystem subsystem;
        @Data
        public static class Monitor{
            private String monitor;
            private String monitorType="url";
            private List<Event> events;
            @Data
            public static class Event{
                private String category;
                private List<Authority>authorities;
                //报警等级列表，1>2>3 没有该字段就是所有，空数组代表不订阅,一般为订阅所有，所以该字段不声明
                private List<Integer> grades;
                private Integer subscribeAll=1;
                private Integer domainSubscribe=2;
                private Integer scheme;
                private Integer eventType;
                @Data
                public static class Authority{
                    private List<String> types;
                    private List<String> orgs;
                    private List<String> nodeCodes;
                }
            }
        }
        @Data
        public static class Subsystem{
            private Integer subsystemType=0;
            private String name;
            private String magic;
        }
    }

}
