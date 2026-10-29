package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 09:28
 * @Description: 抓拍库以图搜图前置条件（根据设备编码获取通道列表）请求参数
 */
@Data
public class GetChannelCodeListRequest {
    /** 设备编码，以,隔开 */
    private String deviceCodeList;
}
