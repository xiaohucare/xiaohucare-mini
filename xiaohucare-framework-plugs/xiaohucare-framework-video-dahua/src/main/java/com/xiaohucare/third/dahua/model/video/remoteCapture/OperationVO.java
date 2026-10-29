package com.xiaohucare.third.dahua.model.video.remoteCapture;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-12 10:49
 * @Description: 设备远程抓图信息
 */
@Data
public class OperationVO {
    /** 消息序号,确保唯一 */
    private Long id;
    /** 固定值dev.snap */
    private String method="dev.snap";
    /** 设备信息 */
    private DeviceVO params;

    @Data
    public static class DeviceVO{
        /** 设备编号，同deviceCode */
        private String DevID;
        /** 通道编号,默认是0 */
        private String DevChannel="0";
        /** 抓图类型， 固定1 */
        private String SnapType="1";
        /** 命令源， 固定0 */
        private String CmdSrc="0";

    }
}
