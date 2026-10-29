package com.xiaohucare.third.dahua.model.visitor.visitorArea;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-18 16:43
 * @Description: 访问区域分页查询请求参数
 */
@Data
public class VisitorAreaPageRequest {
    /** 页面编号 */
    private Integer pageNum;
    /** 每页数量 */
    private Integer pageSize;
    /** 区域名称，全模糊匹配 */
     private String areaName;
}
