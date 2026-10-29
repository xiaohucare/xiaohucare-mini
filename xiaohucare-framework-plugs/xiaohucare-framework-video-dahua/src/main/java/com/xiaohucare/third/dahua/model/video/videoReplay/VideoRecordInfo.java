package com.xiaohucare.third.dahua.model.video.videoReplay;

import lombok.Data;

@Data
public class VideoRecordInfo {
    private String channelId;
    private String recordSource;
    private String videoRecordType;
    private long startTime;
    private long endTime;
    private String streamType;
}
