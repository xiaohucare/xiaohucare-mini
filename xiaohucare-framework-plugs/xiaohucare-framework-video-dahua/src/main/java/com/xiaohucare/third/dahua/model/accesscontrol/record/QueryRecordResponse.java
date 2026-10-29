package com.xiaohucare.third.dahua.model.accesscontrol.record;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

@Data
public class QueryRecordResponse extends IccResponse {

    /**  */
    private Data data;

    @lombok.Data
    public static class Data {
        /** 当前页 */
        private Integer currentPage;
        /** 是否启用云数据库，false-不启用，true-启用 */
        private Boolean enableCloudDB;
        /** 分页大小 */
        private Integer pageSize;
        /** 总页码 */
        private Integer totalPage;
        /** 总条数 */
        private Integer totalRows;
        /** 分页数据 */
        private List<PageData> pageData;

        @lombok.Data
        public static class PageData{
            /** 卡号 */
            private String cardNumber;
            /** 卡状态，-1-空白卡，0-正常卡，1-挂失卡，2-注销卡 */
            private Integer cardStatus;
            /** 卡类型，0-IC卡, 1-有源RFID, 2-CPU卡 */
            private Integer cardType;
            /** 通道编码 */
            private String channelCode;
            /** 通道名称 */
            private String channelName;
            /** 部门名称 */
            private String deptName;
            /** 设备编码 */
            private String deviceCode;
            /** 设备名称 */
            private String deviceName;
            /** 进出门类型，1-进门, 2出门, 3-进/出门 */
            private Integer enterOrExit;
            /** 记录id */
            private Long id;
            /** 1-内部人员, 2-访客 */
            private Integer imageType;
            /** 开门结果，0-失败，1-成功 */
            private Integer openResult;
            /** 开门类型 */
            private Integer openType;
            /** 证件号码 */
            private String paperNumber;
            /** 人员编号 */
            private String personCode;
            /** 人员ID */
            private Long personId;
            /** 人员姓名 */
            private String personName;
            /** 抓图，相对路径 */
            private String recordImageUrl;
            /** 抓图，绝对路径 */
            private String recordImage;
            /** 开门失败原因 */
            private String remark;
            /** 刷卡时间 */
            private String swingTime;
            /** 入库时间 */
            private String createTime;
            /** 口罩状态(3-带口罩,2—没带口罩,1-未识别) */
            private Integer maskState;
            /** 是否超温 */
            private Boolean overTemp;
            /** 体温 */
            private Float curTemp;
        }
    }

}
