package com.xiaohucare.third.dahua.model.event.eventQuery;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-06-11 14:10
 * @Description: alarm事件分页查询返回结果
 */
@Data
public class AlarmRecordPageResponse extends IccResponse {
    /** 返回数据 */
    private Data data;
    @lombok.Data
    public static class Data{
        /** 分页数据 */
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData{
            /** 数据库id */
            private Long id;
            /** 报警发生时间 */
            private String alarmDate;
            /** 报警发生地点 */
            private String alarmPosition;
            /** 报警事件名称 */
            private String alarmTypeName;
            /** 事件类型 */
            private Integer alarmType;
            /** 所属组织 */
            private String orgName;
            /** 报警状态 */
            private Integer alarmStat;
            /** 处理状态 */
            private Integer handleStat;
            /** 处理人 */
            private String handleUser;
            /** 处理时间 */
            private String handleDate;
            /** 报警编号 */
            private String alarmCode;
            /** 联动能力 */
            private String linkNames;
            /** 报警等级 */
            private Integer alarmGrade;
            /** 报警源编码 */
            private String nodeCode;
            /** 报警图片地址 */
            private String alarmPicture;
            /** 任务详情 */
            private String taskWebName;
            /** 参数 */
            private String alarmWebUrl;
            /** 参数 */
            private String alarmAppUrl;
            /** 参数 */
            private String taskWebUrl;
            /** 参数 */
            private String taskAppUrl;
        }
    }
}
