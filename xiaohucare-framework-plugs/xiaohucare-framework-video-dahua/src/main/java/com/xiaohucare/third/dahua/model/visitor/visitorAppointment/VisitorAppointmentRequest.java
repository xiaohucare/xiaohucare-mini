package com.xiaohucare.third.dahua.model.visitor.visitorAppointment;


import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-17 15:17
 * @Description: 访客预约请求参数
 */
@Data
public class VisitorAppointmentRequest {
    /** 被访人id */
    private Long isv_id;
    /** 访客姓名 */
    private String v_name;
    /** 访客手机号码 */
    private String v_phone;
    /** 访客证件类型 */
    private String v_certificateType;
    /** 访客证件号码 */
    private String v_certificateNumber;
    /** 访客单位 */
    private String v_dw;
    /** 访客住址 */
    private String v_address;
    /** 来访人数 */
    private Integer v_personSum;
    /** 来访事由 */
    private String v_reason;
    /** 预约来访时间 ，格式：yyyy-MM-dd HH:mm:ss */
    private String v_timeStr;
    /** 预约离访时间，格式： yyyy-MM-dd HH:mm:ss */
    private String v_lvTimeStr;
    /** 车牌号 */
    private String v_plateNumber;
    /** 门禁/二维码通行次数 */
    private Integer times;
    /** 人像图片 base64 */
    private String faceFile;
    /** 访客类型 固定值：2，兼容老版本 */
    private Integer visitorType=2;
    /** 访客类型 */
    private Long secondVisitorType;
    /** 审批标识，1：跳过审批，0：审批，默认0 */
    private Integer userId=0;
    /** 访客区域ID集合 */
    private List<Long> areaIdList;
    /** 备注 */
    private String memo;
    /** 随访人员列表 */
    private List<FollowVisitor> followVisitors;

    @Data
    public static class FollowVisitor{
        /** 随访人姓名 */
        private String v_name;
        /** 随访人电话 */
        private String v_phone;
        /** 访客证件类型  */
        private String v_certificateType;
        /** 随访人证件号，不能与访客证件号相同 */
        private String v_certificateNumber;
        /** 随访人车牌号 */
        private String v_plateNumber;
        /** 人像图片 base64 */
        private String faceFile;

    }


}
