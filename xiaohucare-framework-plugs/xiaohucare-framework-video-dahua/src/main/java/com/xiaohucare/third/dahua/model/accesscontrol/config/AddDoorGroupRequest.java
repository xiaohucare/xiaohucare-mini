package com.xiaohucare.third.dahua.model.accesscontrol.config;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-22 19:42
 * @Description: 添加门组请求参数
 */
@Data
public class AddDoorGroupRequest {
    /** 名称，仅允许数字、字母、下划线、中文 */
    private String name;
    /** 组织编码 */
    private String orgCode;
    /** 备注，不能包含%或_ */
    private String memo;
    /** 门组详情列表 */
    private List<DoorGroupDetail> doorGroupDetail;
    @Data
    public static class DoorGroupDetail{
        /** 门禁通道编码 */
        private String channelCode;
    }
}
