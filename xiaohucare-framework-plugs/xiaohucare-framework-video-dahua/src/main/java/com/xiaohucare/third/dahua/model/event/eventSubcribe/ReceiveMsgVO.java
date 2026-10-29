package com.xiaohucare.third.dahua.model.event.eventSubcribe;

import com.dahuatech.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 * @author xiaohucare
 * @Date:2024-03-12 11:34
 * @Description: 事件订阅回调消息接收类
 */
@Data
public class ReceiveMsgVO {
    /**
     * 事件大类
     */
    private String category;
    /**
     * 方法名
     */
    private String method;
    /**
     * 具体业务信息
     */
    private JSONObject info;
    /**
     * 序号
     */
    private Long id;
    /**
     * 子系统名称
     */
    private String subsystem;
    /**
     * 域id
     */
    private String domainId;
    /**
     * 用户userId列表，用于发送方指定发送给哪些用户
     * 业务消息才有该字段
     */
    private List<String> userIds;
    /**
     * 发送websocket消息时指定发送给特定的websocket用户
     * 业务消息才有该字段
     */
    private String sid;

}
