package com.xiaohucare.third.dahua.model.attendance.infoQuery;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-29 14:21
 * @Description: 分页获取考勤结果请求参数
 */
@Data
public class GetResultPageResponse extends IccResponse {
    private Data data;
    @lombok.Data
    public static class Data{
        /** 当前页 */
        private Integer currentPage;
        /** 每页条数 */
        private Integer pageSize;
        /** 总页数 */
        private Integer totalPage;
        /** 总条数 */
        private Integer totalRows;
        /** 分页数据 */
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData{
            /** 考勤时间(yyyy-MM-dd) */
            private String dutyDate;
            /** 员工姓名 */
            private String personName;
            /** 员工编号 */
            private String personCode;
            /** 员工id */
            private Long personId;
            /** 部门id */
            private Integer departmentId;
            /** 部门名称 */
            private String deptName;
            /** 班次id */
            private String dailyId;
            /** 班次名称 */
            private String dailyName;
            /** 班次类型 */
            private Integer dailyType;
            /** 考勤结果 */
            private Integer result;
            /** 时间段1签入情况:0-异常,1-正常 */
            private Integer timeOneSignIn;
            /** 时间段1签出情况 */
            private Integer timeOneSignOut;
            /** 时间段2签入情况 */
            private Integer timeTwoSignIn;
            /** 时间段2签出情况 */
            private Integer timeTwoSignOut;
            /** 时间段3签入情况 */
            private Integer timeThreeSignIn;
            /** 时间段3签出情况 */
            private Integer timeThreeSignOut;
            /** 时间段4签入情况 */
            private Integer timeFourSignIn;
            /** 时间段4签出情况 */
            private Integer timeFourSignOut;
            /** 时间段5签入情况 */
            private Integer timeFiveSignIn;
            /** 时间段5签出情况 */
            private Integer timeFiveSignOut;
            /** 应勤时长，单位小时 */
            private Float plannedWorkHours;
            /** 实际时长，单位小时 */
            private Float actualWorkHours;
            /** 缺勤时长，单位小时 */
            private Float absenceWorkHours;
            /** 旷工时长，单位小时 */
            private Float absenteeismWorkHours;
            /** 迟到时长，单位小时 */
            private Float lateWorkHours;
            /** 早退时长，单位小时 */
            private Float earlyLeaveWorkHours;
            /** 出差时长，单位小时 */
            private Float travelWorkHours;
            /** 周内加班时长 ，单位小时 */
            private Float overtimeWeekday;
            /** 周末加班时长，单位小时 */
            private Float overtimeWeekend;
            /** 节假日加班时长，单位小时 */
            private Float overtimeHoliday;
            /** 无薪时长，单位小时 */
            private Float unpaidWorkHours;
            /** 有薪时长，单位小时 */
            private Float paidWorkHours;
            /** 单据编号 */
            private String documentNumber;

        }
    }
}
