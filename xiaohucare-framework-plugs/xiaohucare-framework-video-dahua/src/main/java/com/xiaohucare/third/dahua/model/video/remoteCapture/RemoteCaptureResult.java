package com.xiaohucare.third.dahua.model.video.remoteCapture;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-12 14:50
 * @Description: 抓图信息返回结果
 */
@Data
public class RemoteCaptureResult {
    /** 	消息序号 */
    private Long id;
    /** 固定值 dev.snap */
    private String method;
    /** 截图信息 */
    private PicInfo params;

    @Data
    public static class PicInfo{
        /** 通道号 */
        private Long DevChannel;
        /** 设备编码 */
        private String DevID;
        /** 图片相对地址 */
        private String PicInfo;
        /** 固定为1 */
        private String PicInfoType;
        /** 固定为0 */
        private String cmdSource;
    }
}
