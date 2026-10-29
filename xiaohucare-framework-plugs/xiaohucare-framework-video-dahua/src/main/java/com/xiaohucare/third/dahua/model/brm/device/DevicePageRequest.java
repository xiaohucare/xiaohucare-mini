package com.xiaohucare.third.dahua.model.brm.device;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-26 14:50
 * @Description:设备分页查询请求参数
 */
@Data
public class DevicePageRequest {

    /** 当前页, 默认1 */
    private Integer pageNum=1;
    /** 每页记录数, 默认10 */
    private Integer pageSize=10;
    /** 设备所属组织编码集合 */
    private List<String> ownerCodes;
    /** 是否获取设备所属组织子节点下设备记录 */
    private Integer showChildNodeData=1;
    /** 单元类型,多个 */
    private List<Integer> unitTypes;
    /** 设备大类 */
    private List<Integer> categorys;
    /** 设备小类, */
    private List<Integer> types;
    /** 设备编码列表 最大支持500 */
    private List<String> deviceCodes;
    /** 设备标识码列表 最大支持500 */
    private List<String> deviceSns;
    /** 设备地址 最大支持500 */
    private List<String> deviceIps;
    /** 在线状态 */
    private Integer isOnline;
    /** 0:获取非虚拟设备 1 只获取虚拟设备 */
    private Integer syncVirtualData;


}
