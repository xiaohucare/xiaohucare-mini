package com.xiaohucare.third.dahua.model.passengerflow.passengerflowConfig;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-08 13:56
 * @Description: 分页查询区域配置列表请求参数
 */
@Data
public class RegionConfigPageRequest {
    /** 组织编码 */
    private String parentCode;
    /** 查询条件，根据区域编码或区域名称模糊匹配 */
    private String singleCondition;
    /** 页码 */
    private Integer pageNum=1;
    /** 每页条数 */
    private Integer pageSize=10;
}
