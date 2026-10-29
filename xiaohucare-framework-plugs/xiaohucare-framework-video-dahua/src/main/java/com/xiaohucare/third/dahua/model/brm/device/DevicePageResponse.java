package com.xiaohucare.third.dahua.model.brm.device;

import java.util.List;

import com.dahuatech.icc.oauth.http.IccResponse;
import com.xiaohucare.third.dahua.model.video.realTimePreview.HlsUrlResponse;
import com.xiaohucare.third.dahua.model.video.realTimePreview.RtspUrlResponse;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-26 17:20
 * @Description: 分页查询设备信息返回
 */
@Data
public class DevicePageResponse extends IccResponse {

    /** 分页数据 */
    private PageVO data;

    @Data
    public static class PageVO{
        /** 当前页码 */
        private Integer currentPage;
        /** 总页数 */
        private Integer totalPage;
        /** 页面大小 */
        private Integer pageSize;
        /** 总数 */
        private Integer totalRows;
        /** 数据列表 */
        private List<DeviceInfoVO> pageData;

        @Data
        public static class DeviceInfoVO{
            /** 设备编码 */
            private String deviceCode;
            /** 设备名称 */
            private String deviceName;
            /** 设备唯一标识码 */
            private String deviceSn;
            /** 设备大类 */
            private Integer deviceCategory;
            /** 设备小类 */
            private Integer deviceType;
            /** 厂商类型 */
            private String deviceManufacturer;
            /** 设备IP */
            private String deviceIp;
            /** 设备端口 */
            private Integer devicePort;
            /** 设备所属组织编码 */
            private String ownerCode;
            /** 在线状态 */
            private Integer isOnline;
            /** 设备离线原因 */
            private String offlineReason;
            /** 设备所属子系统 */
            private String subSystem;
            /** 设备扩展属性 */
            private Object devExt;
            /** 经度 */
            private String gpsX;
            /** 纬度 */
            private String gpsY;
            /** 单元信息 */
            private List<UnitVO> units;

            private String showUrl;
            private RtspUrlResponse rtspUrlResponse;
            private HlsUrlResponse hlsUrlResponse;
            
            @Data
            public static class UnitVO{
                /** 单元类型 */
                private Integer unitType;
                /** 单元序号 */
                private Integer unitSeq;
                /** 排序码 */
                private Integer sort;
                /** 单元描述 */
                private String memo;
                /** 能力集 */
                private String capability;
                /** 单元扩展信息 */
                private Object unitExt;
                /** 单元通道信息 */
                private List<ChannelVO> channels;

                @Data
                public static class ChannelVO{
                    /** 通道编码 */
                    private String channelCode;
                    /** 通道名称 */
                    private String channelName;
                    /** 通道序号 */
                    private Integer channelSeq;
                    /** 通道唯一标识码 */
                    private String channelSn;
                    /** 通道类型 */
                    private String channelType;
                    /** 通道能力集 */
                    private String capability;
                    /** 是否已接入：0-未接入，1-接入 */
                    private Integer access;
                    /** 经度 */
                    private String gpsX;
                    /** 纬度 */
                    private String gpsY;
                    /** Z轴 */
                    private String gpsZ;
                    /** 通道描述 */
                    private String memo;
                    /** 状态 ：0-不启用，1-启用 */
                    private Integer stat;
                    /** 通道扩展属性  */
                    private Object chExt;
                }
            }

        }
    }

}
