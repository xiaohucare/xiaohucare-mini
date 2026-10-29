package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-15 11:39
 * @Description: 以图搜图-同步返回参数
 */
@Data
public class FaceSearchSyncResponse extends IccResponse {
    private Data data;
    @lombok.Data
    public static class Data{
        private PageData pageData;
        @lombok.Data
        public static class PageData{
            /** 记录ID */
            private Long id;
            /** 设备编码 */
            private String devId;
            /** 设备名称 */
            private String deviceName;
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
            /** 相似度 */
            private Float similarity;
            /** 人像特征 */
            private String featureStr;
            /** 人像库名称 */
            private String groupName;
            /** 人像库类型 */
            private Integer groupType;
            /** 人像库类型名 */
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
