package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 09:30
 * @Description: 抓拍库以图搜图前置条件（根据设备编码获取通道列表）返回参数
 */
@Data
public class GetChannelCodeListResponse extends IccResponse {
    private List<ChannelInfo> data;
    @Data
    public static class ChannelInfo{
        /** 通道编码字符串 */
        private String channelCodeStr;
        /** 通道名称 */
        private String name;
        /** 设备编码 */
        private String deviceCode;
        /** 设备名称 */
        private String deviceName;
    }
}
