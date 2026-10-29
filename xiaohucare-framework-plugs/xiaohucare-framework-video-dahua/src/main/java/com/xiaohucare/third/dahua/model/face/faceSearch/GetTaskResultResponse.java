package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 14:07
 * @Description: 根据任务获取搜索结果返回结果
 */
@Data
public class GetTaskResultResponse extends IccResponse {
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
        /** 总条数 */
        private Long totalRows;
        /** 是否开启云数据库 */
        private Boolean enableCloudDB;
        /** 分页数据 */
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData{
            /** 记录ID */
            private Long id;
            /** 设备编码 */
            private String devId;
            /** 设备名称 */
            private String deviceName;
            /** 通道编码 */
            private String channelId;
            /** 通道名称 */
            private String channelName;
            /** 通道序号 */
            private String channelSeq;
            /** 人像抓图相对地址 */
            private String faceImgUrl;
            /** 场景图相对地址 */
            private String hitImgUrl;
            /** 抓拍时间 */
            private Long recTime;
            /** 口罩状态值 */
            private Integer mask;
            /** 胡子状值 */
            private Integer beard;
            /** 眼镜状态值 */
            private Integer glass;
            /** 口罩状态说明 */
            private String maskStr;
            /** 胡子状态说明 */
            private String beardStr;
            /** 眼镜状态说明 */
            private String glassStr;
            /** 设备上的人像库ID */
            private String faceLibId;
            /** 人像底图相对地址 */
            private String libImgUrl;
            /** 证件号码 */
            private String identity;
            /** 人员姓名 */
            private String name;
            /** 性别 */
            private String sex;
            /** 年龄 */
            private Integer age;
            /** 相似度 */
            private Float similarity;
            /** 人像特征 */
            private String featureStr;
            /** 人像库名称 */
            private String groupName;
            /** 人像库类型 */
            private Integer groupType;
            /** 人像库类型名称*/
            private String groupTypeStr;
            /** 证件类型 */
            private Integer identityType;
            /** 证件类型名 */
            private String identityTypeStr;
            /** 检索类型，1-抓拍库，2-人像库 */
            private String searchGroupType;
            /** 生日 */
            private String birthdayStr;
            /** 人员编码 */
            private String personCode;
        }
    }
}
