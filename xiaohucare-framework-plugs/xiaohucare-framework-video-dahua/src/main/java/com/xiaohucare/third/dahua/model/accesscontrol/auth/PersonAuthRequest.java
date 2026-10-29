package com.xiaohucare.third.dahua.model.accesscontrol.auth;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-22 14:13
 * @Description: 按人新增授权请求参数
 */
@Data
public class PersonAuthRequest {
    /** 人员编号 */
    private List<String> personCodes;
    /** 时间计划ID */
    private Long timeQuantumId;
    /** 权限开始时间,日期格式：yyyy-MM-dd HH:mm:ss */
    private String startDate;
    /** 权限结束时间，日期格式：yyyy-MM-dd HH:mm:ss */
    private String endDate;
    /** 授权对象 */
    private List<PrivilegeDetail> privilegeDetails;
    @Data
    public static class PrivilegeDetail{
        /** 授权方式：1-通道，2-门组 */
        private Integer privilegeType;
        /** 通道编码或门组ID */
        private String resourceCode;
        /** 时间计划，优先级高于外层时间计划 */
        private String timeQuantumId;
        /** 权限开始时间，优先级高于外层，日期格式：yyyy-MM-dd HH:mm:ss */
        private String startDate;
        /** 权限结束时间，优先级高于外层，日期格式：yyyy-MM-dd HH:mm:ss */
        private String endDate;
    }
}
