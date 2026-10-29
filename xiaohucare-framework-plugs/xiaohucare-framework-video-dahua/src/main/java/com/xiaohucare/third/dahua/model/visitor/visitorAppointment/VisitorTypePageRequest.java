package com.xiaohucare.third.dahua.model.visitor.visitorAppointment;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-19 10:13
 * @Description: 查询访客类型列表请求参数
 */
@Data
public class VisitorTypePageRequest {
    /** 每页条数 */
    private Integer pageSize;
    /** 页码 */
    private Integer pageNum;
}
