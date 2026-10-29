package com.xiaohucare.third.dahua.model.event.eventQuery;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-06-11 13:54
 * @Description: alarm事件统计返回结果
 */
@Data
public class AlarmRecordCountResponse extends IccResponse {
    private TotalInfo data;
    @Data
    public static class TotalInfo{
        private Integer value;
    }
}
