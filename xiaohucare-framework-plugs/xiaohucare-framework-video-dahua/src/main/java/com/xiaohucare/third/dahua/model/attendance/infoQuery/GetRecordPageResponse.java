package com.xiaohucare.third.dahua.model.attendance.infoQuery;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 * @author xiaohucare
 * @Date:2024-04-28 14:13
 * @Description: 打卡记录查询返回参数
 */
@Data
public class GetRecordPageResponse extends IccResponse {
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
            /** 刷卡时间(yyyy-MM-dd HH:mm:ss) */
            private String actionTime;
            /** 考勤方式 */
            private String attendanceType;
            /** 卡号 */
            private String cardNo;
            /** 部门名称 */
            private String deptName;
            /** 人员编号 */
            private String personCode;
            /** 人员id */
            private Integer personId;
            /** 人员姓名 */
            private String personName;
            /** 上报时间(yyyy-MM-dd HH:mm:ss) */
            private String reportTime;
            /** 考勤点id */
            private String siteId;
            /** 考勤点名称 */
            private String siteName;
        }
    }
}
