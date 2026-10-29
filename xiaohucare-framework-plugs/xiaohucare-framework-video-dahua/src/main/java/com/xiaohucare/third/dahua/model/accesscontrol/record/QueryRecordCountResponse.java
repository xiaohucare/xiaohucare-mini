package com.xiaohucare.third.dahua.model.accesscontrol.record;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-01 11:54
 * @Description: 门禁记录数量返回类
 */
@Data
public class QueryRecordCountResponse extends IccResponse {
    /** 刷卡记录数量 */
    private Integer data;
}
