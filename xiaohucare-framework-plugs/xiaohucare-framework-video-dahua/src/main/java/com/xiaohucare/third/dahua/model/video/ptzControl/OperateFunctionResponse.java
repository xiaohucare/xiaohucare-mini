package com.xiaohucare.third.dahua.model.video.ptzControl;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-09 16:19
 * @Description: 云台方向控制请求结果
 */
@Data
public class OperateFunctionResponse extends IccResponse {

    private String desc;
    private OperateCameraResponse.Data data;

    @lombok.Data
    public static class Data {
        private String result;
        private LockUser lockUser;

        @lombok.Data
        public static class LockUser {
            private String userName;
            private String userLevel;
            private String lowerDomain;
        }
    }
}
