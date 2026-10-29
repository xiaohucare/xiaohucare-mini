package com.xiaohucare.third.dahua.model.visitor.visitorArea;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-18 16:43
 * @Description: 访问区域分页查询返回结果
 */
@Data
public class VisitorAreaPageResponse extends IccResponse {
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
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData{
            /** ID  */
            private Long id;
            /** 区域名称 */
            private String areaName;
            /** 创建时间 */
            private String createTime;
            /** 更新时间 */
            private String updateTime;
            /** 是否默认区域：1 - 是，0 - 否。V5.0.10及以上版本存在 */
            private Integer isDefault;
        }
    }
}
