package com.xiaohucare.third.dahua.model.brm.device;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-07 10:03
 * @Description: 设备通道分页查询返回参数
 */
@Data
public class ChannelPageResponse extends IccResponse {
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
        private List<ChannelInfoVO> pageData;

        @Data
        public static class ChannelInfoVO{
            /** 自增ID */
            private Long id;
            /** 设备编码 */
            private String deviceCode;
            /** 单元类型 */
            private Integer unitType;
            /** 单元序号 */
            private Integer unitSeq;
            /** 通道序号 */
            private Integer channelSeq;
            /** 通道编码 */
            private String channelCode;
            /** 通道标识码 */
            private String channelSn;
            /** 通道名称 */
            private String channelName;
            /** 通道类型 */
            private String channelType;
            /** 摄像头类型 */
            private String cameraType;
            /** 所属组织编码 */
            private String ownerCode;
            /** 经度 */
            private String gpsX;
            /** 纬度 */
            private String gpsY;
            /** z轴 */
            private String gpsZ;
            /** 光栅图ID */
            private Long mapId;
            /** 域ID */
            private Long domainId;
            /** 描述 */
            private String memo;
            /** 设备通道在线状态 */
            private Integer isOnline;
            /** 状态 0:关闭 1:开启 */
            private Integer stat;
            /** 通道能力集，详细说明请参考通道能力级说明 */
            private String capability;
            /** 是否已经接入 ：1-已接入， 0-未接入 */
            private Integer access;
            /** 通道详情扩展信息 */
            private String chExt;
            /** 是否是虚拟通道 */
            private String isVirtual;
        }
    }
}
