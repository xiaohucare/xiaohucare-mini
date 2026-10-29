package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 10:25
 * @Description: 人像库搜图前置条件（根据设备编码查询人像库）返回结果
 */
@Data
public class GetGroupResponse extends IccResponse {
    /** 请求结果项 */
    private Data data;
    @lombok.Data
    public static class Data{
        /** 当前页 */
        private Integer currentPage;
        /** 总页数 */
        private Integer totalPage;
        /** 每页条数 */
        private Integer pageSize;
        /** 总记录数 */
        private Long totalRows;
        /** 分页数据 */
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData{
            /** 人像库ID */
            private Long groupId;
            /** 人像库名称 */
            private String groupName;
        }
    }
}
