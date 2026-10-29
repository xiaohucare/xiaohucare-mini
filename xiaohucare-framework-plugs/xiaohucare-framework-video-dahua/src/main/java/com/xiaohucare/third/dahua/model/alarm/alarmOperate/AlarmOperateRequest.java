package com.xiaohucare.third.dahua.model.alarm.alarmOperate;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-06 14:47
 * @Description: 报警主机操作控制请求参数
 */
@Data
public class AlarmOperateRequest {
    /** 操作节点信息 */
    private List<NodeInfo> nodeInfoList;
    /** 操作类型 */
    private Integer operate;
    /** 延迟时间 */
    private Long delayTime;

    @Data
    public static class NodeInfo{
        /** 设备编码 */
        private String nodeCode;
        /** 节点名称 */
        private String nodeName;
    }
}
