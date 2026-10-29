package com.xiaohucare.third.dahua.model.video.ptzControl;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-10 20:11
 * @Description: 保存巡航线接口返回
 */
@Data
public class SaveCruiseResponse extends IccResponse {
    /** 结果描述*/
    private String desc;
}
