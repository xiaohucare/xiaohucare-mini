package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 13:50
 * @Description: 人像以图搜图创建搜索任务返回结果
 */
@Data
public class CreateSearchTaskResponse extends IccResponse {
    private String data;
}
