package com.xiaohucare.third.dahua.model.brm.person;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-19 14:37
 * @Description: 人员添加返回结果
 */
@Data
public class PersonAddResponse extends IccResponse {
    private PersonAddData data;

    @Data
    public static class PersonAddData{
        /** 人员id */
        private Long id;
    }


}
