package com.xiaohucare.third.dahua.model.video.remoteCapture;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-12 10:43
 * @Description: 设备远程抓图请求参数
 */
@Data
public class RemoteCaptureRequest {
    /** 设备编号 */
    private String deviceCode;
    /** 固定值generalJsonTransport */
    private String operation="generalJsonTransport";
    /** 操作对象OperationVO json字符串 */
    private String params;
}
