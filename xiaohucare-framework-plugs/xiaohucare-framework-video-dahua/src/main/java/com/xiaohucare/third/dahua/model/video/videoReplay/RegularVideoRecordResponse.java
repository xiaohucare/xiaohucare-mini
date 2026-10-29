package com.xiaohucare.third.dahua.model.video.videoReplay;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-07 10:31
 * @Description: 查询普通录像信息列表返回结果
 */
@Data
public class RegularVideoRecordResponse extends IccResponse {
    private Data data;
    @lombok.Data
    public static class Data{
        private List<RecordInfo> records;
        @lombok.Data
        public static class RecordInfo{
            private String channelId;
            private String recordSource;
            private String recordType;
            private String startTime;
            private String endTime;
            private String recordName;
            private String fileLength;
            private String planId;
            private String ssId;
            private String diskId;
            private String streamId;
            private String forgotten;
            private String streamType;
            private String videoRecordType;
        }
    }
}
