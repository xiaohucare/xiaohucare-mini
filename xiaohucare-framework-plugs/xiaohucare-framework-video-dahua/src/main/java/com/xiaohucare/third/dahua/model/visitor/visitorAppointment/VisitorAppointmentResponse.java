package com.xiaohucare.third.dahua.model.visitor.visitorAppointment;

import java.util.List;

import com.dahuatech.icc.oauth.http.IccResponse;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-18 11:26
 * @Description: 访客预约返回参数
 */
@Data
public class VisitorAppointmentResponse extends IccResponse {
    /** 访客数据 */
    private VisitorResData data;
    @Data
    public static class VisitorResData{
        /** 访客预约id */
        private String id;
        /** 是否需要审批 */
        private Boolean needReview;
        /** 二维码加密串 */
        private String v_barCode;
        /** 访客预约码 */
        private String appointmentCode;
        /** 随访人信息 */
        private List<FollowInfoVO> followInfoList;
        @Data
        public static class FollowInfoVO{
            /** 随访人预约id */
            private String id;
            /** 随访人姓名 */
            private String v_name;
            /** 随访人二维码加密串 */
            private String v_barCode;
            /** 随访人预约码 */
            private String appointmentCode;
        }
    }

}
