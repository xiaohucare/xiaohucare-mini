package com.xiaohucare.third.dahua.model.attendance.infoQuery;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-29 13:48
 * @Description: 分页获取考勤结果请求参数
 */
@Data
public class GetResultPageRequest {
    /** 第几页 */
    private Integer pageNum;
    /** 每页条数 */
    private Integer pageSize;
    /** 开始时间(yyyy-MM-dd) */
    private String dutyDateAfter;
    /** 结束时间(yyyy-MM-dd) */
    private String dutyDateBefore;
    /** 考勤结果(0-异常,1-正常) */
    private Integer result;
    /** 班次类型: 1-固定班, 2-弹性班, 3-签到班, 4-休班 */
    private String dailyType;
    /** 单个人员编号 */
    private String personCode;
    /** 部门id(部门间以逗号隔开) */
    private String deptIdsString;
    /** 人员id集合 */
    private List<Long> personIds;
    /** 班次中各时段状态: 0-异常, 1-正常 */
    private List<Integer> attendanceStatus;
    /** 选择的班次id集合 */
    private List<Long> classesIds;
    /** 排序字段 */
    private String sort;
    /** DESC-降序, ASC-升序 */
    private String sortType;
}
