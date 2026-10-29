package com.xiaohucare.third.dahua.model.video.realTimePreview;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * 封装实时拉流返回
 */
@Data
public class HlsUrlResponse extends IccResponse {

  private HlsData data;

  public HlsData getData() {
    return data;
  }

  @Data
  public static class HlsData{

    private String url;

  }
}
