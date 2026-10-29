package com.xiaohucare.third.dahua.model.brm.person;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-13 09:37
 * @Description:上传图片返回结果
 */
@Data
public class UploadImageResponse extends IccResponse {
    /** 返回数据对象 */
    private FileData data;

    @Data
    public static class FileData {
        /** 文件的路径 */
        private String fileUrl;

    }
}
