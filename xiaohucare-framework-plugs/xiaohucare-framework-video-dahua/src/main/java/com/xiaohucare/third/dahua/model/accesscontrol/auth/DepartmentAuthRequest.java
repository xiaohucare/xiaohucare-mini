package com.xiaohucare.third.dahua.model.accesscontrol.auth;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-22 17:30
 * @Description: 按部门新增授权请求参数
 */
@Data
public class DepartmentAuthRequest {
    /** 部门ID */
    private List<Long> deptIds;
    /** 通道编码 */
    private List<String> channelCodes;
    /** 门组ID */
    private List<Long> doorGroupIds;
    /** 时间计划ID */
    private Long timeQuantumId;
}
