package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-15 15:34
 * @Description:
 */
@Data
public class GetDeviceTreeResult {
        /** 节点id */
        private String id;
        /** 父节点id */
        private String pId;
        /** 节点名称 */
        private String name;
        /** 节点图标 */
        private String icon;
        /** 类型 */
        private String type;
        /** 是否是父节点 */
        private String isParent;
        /** 是否被勾选 */
        private Boolean checked;
        private Integer checkStat;
        private Boolean halfCheck;
        /** 节点类型 org：组织编码，dev：设备编码，ch：通道编码 */
        private String nodeType;
        /** 排序 */
        private Integer sort;
        /** 所属设备大类 */
        private Integer belongedDeviceCategory;
        /** 所属设备大类 */
        private Integer deviceType;

}
