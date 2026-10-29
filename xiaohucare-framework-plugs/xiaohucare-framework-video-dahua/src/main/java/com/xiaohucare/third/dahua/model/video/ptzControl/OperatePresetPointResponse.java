package com.xiaohucare.third.dahua.model.video.ptzControl;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-09 16:19
 * @Description: 预置点控制(添加/删除/定位)接口返回
 */
@Data
public class OperatePresetPointResponse extends IccResponse {

    /** 结果描述*/
    private String desc;
    private OperateCameraResponse.Data data;

    @lombok.Data
    public static class Data {
        /** 操作结果：0=失败，1=成功，失败时参考LockUser*/
        private String result;
        /** 锁定用户信息*/
        private LockUser lockUser;

        @lombok.Data
        public static class LockUser {
            /** 控制失败时，持有锁的用户名称*/
            private String userName;
            /** 控制失败时，持有锁的用户级别*/
            private String userLevel;
            /** 下级域地址，null或者空字符串表示是本级，有地址时表示下级*/
            private String lowerDomain;
        }
    }
}
