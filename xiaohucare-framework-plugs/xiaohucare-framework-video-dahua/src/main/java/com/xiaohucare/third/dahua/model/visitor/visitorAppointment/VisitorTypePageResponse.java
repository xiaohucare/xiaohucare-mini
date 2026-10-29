package com.xiaohucare.third.dahua.model.visitor.visitorAppointment;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-19 10:16
 * @Description: 查询访客类型列表返回结果
 */
@Data
public class VisitorTypePageResponse extends IccResponse {
    /** 访问区域信息  */
    private Data data;
    @lombok.Data
    public static class Data{
        /** 当前页码 */
        private Integer currentPage;
        /** 总页数 */
        private Integer totalPage;
        /** 每页条数 */
        private Integer pageSize;
        /** 总条数 */
        private Integer totalRows;
        /** 是否启用云数据库 */
        private Boolean enableCloudDB;
        /** 分页数据 */
        private List<VisitorType> pageData;
        @lombok.Data
        public static class VisitorType{
            /** 访客类型编码（主键） */
            private Long id;
            /** 访客类型名称 */
            private String typeName	;
            /** 是否移动端预约审核，1：是，0：否 */
            private String isChecked;
            /** 是否认证核验，1：是，0：否 */
            private Integer isReviewed;
            /** 审核对象，1：被访对象 2：固定对象 */
            private Integer reviewedObject;
            /** 创建时间，格式yyyy-MM-dd HH:mm:ss */
            private String createTime;
            /** 更新时间，格式yyyy-MM-dd HH:mm:ss */
            private String updateTime;
        }
    }
}
