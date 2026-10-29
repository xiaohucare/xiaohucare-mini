package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 13:52
 * @Description: 以图搜图任务进度查询返回结果
 */
@Data
public class QueryTaskProgressResponse extends IccResponse {
    private Data data;
    @lombok.Data
    public static class Data{
        /** 任务编码（查询的流水id） */
        private String taskCode;
        /** 任务状态：0=进行中，1=已完成，2=失败，3=等待中 */
        private Integer taskStatus;
        /** 当前的查询进度百分比 */
        private Float progress;
    }
}
