package com.xiaohucare.third.dahua.model.accesscontrol.record;

import lombok.Data;

import java.util.List;

@Data
public class QueryRecordRequest  {

    /** 页码，正整数 */
    private Integer pageNum;
    /** 分页大小，正整数 */
    private Integer pageSize;
    /** 查询开始时间，格式：yyyy-MM-dd HH:mm:ss */
    private String startSwingTime;
    /** 查询结束时间，格式：yyyy-MM-dd HH:mm:ss */
    private String endSwingTime;
    /** 入库开始时间，格式：yyyy-MM-dd HH:mm:ss */
    private String startCreateTime;
    /** 入库结束时间，格式：yyyy-MM-dd HH:mm:ss */
    private String endCreateTime;
    /** 开门类型 */
    private Integer openType;
    /** 开门类型多选 */
    private List<Integer> openTypes;
    /** 卡片类型 */
    private String category;
    /** 人员名称 */
    private String personName;
    /** 人员编号 */
    private String personCode;
    /** 通道编码列表 */
    private List<String> channelCodes;
    /** 部门ID */
    private String deptIds;
    /** 卡号 */
    private String cardNumber;
    /** 事件类型 */
    private Integer enterOrExit;
    /** 开门结果 */
    private Integer openResult;
    /** 是否超温 */
    private Boolean overTemp;
    /** 体温低限 */
    private Float curTempStart;
    /** 体温高限 */
    private Float curTempEnd;
    /** 口罩状态 3-带口罩,2—没带口罩,1-未识别 */
    private Integer maskState;
    /** 访客筛选，1 - 只查询访客记录 2 - 只查询非访客记录 */
    private Integer visitorFilter;
}
