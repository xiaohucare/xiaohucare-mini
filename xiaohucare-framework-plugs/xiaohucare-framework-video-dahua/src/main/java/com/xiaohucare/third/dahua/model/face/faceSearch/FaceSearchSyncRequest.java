package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-15 11:33
 * @Description: 以图搜图-同步请求参数
 */
@Data
public class FaceSearchSyncRequest {
    /** 搜索库类型，固定值2，仅支持人像库*/
    private Integer searchGroupType=2;
    /** base64格式图片数据 */
    private String base64Img;
    /** 相似度(60-100区间的正整数) */
    private Integer threshold;
    /** 人像库ids 同步接口只支持一个设备搜 */
    private List<String> groupIds;
    /** 设备编码集合 */
    private List<String> deviceCodes;
    /** 此处定为1，ivss类型设备 */
    private Integer devType=1;
}
