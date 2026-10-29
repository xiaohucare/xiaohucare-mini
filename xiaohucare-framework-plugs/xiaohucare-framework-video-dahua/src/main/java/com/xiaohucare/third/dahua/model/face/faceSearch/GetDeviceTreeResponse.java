package com.xiaohucare.third.dahua.model.face.faceSearch;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-15 15:16
 * @Description: 查询设备树返回
 */
@Data
public class GetDeviceTreeResponse extends IccResponse {
    private String  data;
}
