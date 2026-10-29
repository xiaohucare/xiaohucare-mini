package com.xiaohucare.third.dahua.model.brm.device;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-07 16:39
 * @Description: 设备树查询请求参数
 */
@Data
public class DeviceTreeRequest {
    /** 上级节点编码 */
    private String id;
    /** 组织/设备树展示的节点类型 */
    private String type;
    /** 业务类型:1-基本组织，2-逻辑组织 */
    private Integer busiType;
    /** 查询逻辑组织根组织标识，默认true */
    private Boolean checkLogicRootOrgNodeFlag;
    /** 通道归属于组织还是归属于设备 1：归属于设备 0：归属于组织 默认0 */
    private Integer channelBelong;
    /** 操作类型 */
    private String act;
    /** 上级组织/设备节点是否有权限 */
    private Integer checkStat;
    /** act=search时模糊搜索的关键字 */
    private String searchKey;
    /** 通道能力集 */
    private List<String> chCapability;
    /** 设备能力集 */
    private List<String> devCapability;
    /** 初始化加载树时需要勾选的节点id列表 */
    private List<String> checkNodes;
    /** 是否展示级联节点,默认是1 */
    private Integer showCascadeNode;
    /** 是否展示虚拟设备、通道,默认0 */
    private Integer showVirtualNode;
    /** 是否展示出没有通道或设备的组织节点,默认0 */
    private Integer showEmptyOrgNode;
    /** 设备通道状态 -1:查询全部 0:查询关闭 1:查询已经开启 默认:1 */
    private Integer stat;
}
