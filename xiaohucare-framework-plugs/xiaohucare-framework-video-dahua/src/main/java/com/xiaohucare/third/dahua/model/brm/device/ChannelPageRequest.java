package com.xiaohucare.third.dahua.model.brm.device;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-07 10:02
 * @Description: 设备通道分页查询请求参数
 */
@Data
public class ChannelPageRequest {
    /** 当前页, 默认1 */
    private Integer pageNum=1;
    /** 每页记录数, 默认10 */
    private Integer pageSize=10;
    /** 排序属性 */
    private String sort;
    /** 排序方式 */
    private String sortType;
    /** 所属组织编码 */
    private String ownerCode;
    /** 设备编码列表 */
    private List<String> deviceCodeList;
    /** 通道编码列表 */
    private List<String> channelCodeList;
    /** 设备大类 */
    private Integer deviceCategory;
    /** 设备小类 */
    private Integer deviceType;
    /** 单元类型列表 */
    private List<Integer> unitTypeList;
    /** 通道类型列表 */
    private List<String> channelTypeList;
    /** 在线状态 */
    private Integer isOnline;
    /** 是否已经接入 */
    private Integer access;
    /** 是否是虚拟通道 */
    private Integer isVirtual;
    /** 状态 */
    private Integer stat;
    /** 是否包含子节点,默认true,展示下级通道 */
    private Boolean includeSubOwnerCodeFlag;

}
