package com.xiaohucare.third.dahua.model.video.ptzControl;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-12 09:46
 * @Description: 巡航线信息
 */
@Data
public class CruiseInfo {
    /** 巡航线编码 */
    private String id;
    /** 巡航线名称 */
    private String name;
    /** 包含预置点个数 */
    private String pointId;
    /** 预置点信息列表 */
    private List<PrePoint> prePoints;
    @lombok.Data
    public static class PrePoint{
        /** 预置点id */
        private String id;
        /** 预置点编码 */
        private String code;
        /** 预置点停留时间 */
        private String stay;
    }
}
