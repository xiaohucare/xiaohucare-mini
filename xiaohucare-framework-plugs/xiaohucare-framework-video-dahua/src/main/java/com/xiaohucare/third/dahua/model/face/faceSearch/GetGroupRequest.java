package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 10:16
 * @Description: 人像库搜图前置条件（根据设备编码查询人像库）请求参数
 */
@Data
public class GetGroupRequest {
    /** 选择的设备的编码 */
    private List<String> deviceCodes;
    /** 库名称 可通过库名称模糊查询 */
    private String groupNames;
    /** 每页条数 */
    private Integer pageNum=1;
    /** 页码 */
    private Integer pageSize=6;
}
