package com.xiaohucare.third.dahua.model.event.eventQuery;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-06-11 13:53
 * @Description: alarm事件统计请求参数
 */
@Data
public class AlarmRecordCountRequest {
    /** 排序字段 */
    private String sort;
    /** 排序方式：ASC 升序；DESC 降序 */
    private String sortType;
    /** 报警状态 1：报警产生，2：报警消失 */
    private Integer alarmStat;
    /** 报警处理状态 1处理中，2已处理，3误报，4忽略，0未处理 */
    private List<Integer> handleStatList;
    /** 所属组织编码 */
    private List<String> orgCodeList;
    /** 设备编码或通道编码 */
    private List<String> nodeCodeList;
    /** 报警开始时间 */
    private String alarmStartDateString;
    /** 报警结束时间 */
    private String alarmEndDateString;
    /** 处理开始时间 */
    private String handleStartDateString;
    /** 处理结束时间 */
    private String handleEndDateString;
    /** 事件类型列表 */
    private List<Integer> alarmTypeList;
    /** 事件类型 */
    private Integer alarmType;
    /** 设备大类 */
    private Integer deviceCategory;
    /** 报警预案标识 */
    private Integer isEvent;
    /** 报警等级 */
    private List<Integer> alarmGradeList;
    /** 语言 */
    private String language;
    /** 查询类型 0代表普通查询，1代表云数据库查询，1.0.1版本新增 */
    private Integer dbType;
}
