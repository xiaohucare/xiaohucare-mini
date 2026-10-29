package com.xiaohucare.third.dahua.model.alarm.alarmOperate;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-06 14:47
 * @Description: 报警主机操作控制返回参数
 */
@Data
public class AlarmOperateResponse extends IccResponse {
   private Object data;
}
