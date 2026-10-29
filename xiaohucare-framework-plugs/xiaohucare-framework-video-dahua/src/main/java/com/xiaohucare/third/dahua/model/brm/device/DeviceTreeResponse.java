package com.xiaohucare.third.dahua.model.brm.device;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-07 16:39
 * @Description: 设备树查询返回参数
 */
@Data
public class DeviceTreeResponse extends IccResponse {
    private ValueVO data;

    @Data
    public static class ValueVO{
        /** 数据列表 */
        private List<TreeItemVO> value;

        @Data
        public static class TreeItemVO{
            /** 节点ID */
            private String id;
            /** 节点名称 */
            private String name;
            /** 上级节点ID */
            private String pId;
            /** 节点类型 */
            private String type;
            /** 设备树节点类型 */
            private String nodeType;
            /** 节点图标 */
            private String iconType;
            /** 是否存在子节点 */
            private Boolean isParent;
            /** 是否勾选 */
            private Boolean isCheck;
            /** 上级组织/设备节点是否有权限 */
            private Integer checkStat;
            /** 设备大类 */
            private Integer deviceCategory;
            /** 设备型号 */
            private String deviceModel;
            /** 设备小类 */
            private Integer deviceType;
            /** 所属设备编码（通道节点属性） */
            private String deviceCode;
            /** 通道单元类型（通道节点属性） */
            private Integer unitType;
            /** 所属组织（设备节点/通道节点属性） */
            private String ownerCode;
            /** 摄像头类型（通道节点属性） */
            private Integer cameraType;
            /** 通道能力集 */
            private String capability;
            /** 组织序列号 */
            private String sn;
            /** 是否虚拟设备(null的话代表否） */
            private Integer isVirtual;
            /** 通道类型 */
            private String channelType;
            /** 通道编号 */
            private String channelCode;
            /** 通道状态 0:关闭 1:开启 */
            private Integer stat;
        }
    }
}
